package Manager;

import Dto.*;
import Grid.Grid;
import PRD.PRDWorld;
import Property.Range;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Rule.Rule;
import Terminition.Termination;
import Terminition.TerminationType;
import UserRequest.UserRequest;
import World.definition.WorldDefinition;
import World.instance.WorldInstance;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PredictionManager {

    WorldDefinition worldDefinition;
    HashMap<Integer, WorldInstance> simulationList;
    List<UserRequest> requests;

    public PredictionManager() {
        simulationList = new HashMap<>();
        worldDefinition = null;
        requests = new ArrayList<UserRequest>();
    }

    public void addRequest(UserRequest request){
        requests.add(request);
    }

    public Integer initPredictionManager(WorldDefinition worldDef , Map<String, Integer> entitiesPopulationMap, HashMap<TerminationType, Termination> terminationCondition, Map<String, String> envPropValue, UserRequest request){
        worldDefinition = new WorldDefinition(worldDef,terminationCondition);
        InitializePopulation(entitiesPopulationMap);
        Integer simulationNumber = simulationList.size() + 1;
        simulationList.put(simulationNumber,new WorldInstance(worldDefinition,envPropValue,request));
        return simulationNumber;
    }

    public List<FullRequestDto> getRequestslist(){
        List<FullRequestDto> res = new ArrayList<>();

        for (UserRequest request : requests){
            res.add(new FullRequestDto(request));
        }

        return res;
    }

    public HashMap<Integer, WorldInstance> getSimulationList() {
        return simulationList;
    }

    public SimulationTitlesDetails loadSimulation(String xmlContent)
    {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            // Create an Unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Create a StringReader to read from the XML content String
            StringReader stringReader = new StringReader(xmlContent);

            // Unmarshal from the StringReader
            PRDWorld pRDworld = (PRDWorld) unmarshaller.unmarshal(stringReader);
            worldDefinition = new WorldDefinition(pRDworld);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Loading the XML file failed");
        }


        // create SimulationDetailsDto

        List<String> entitiesNames = worldDefinition.getEntities().keySet().stream().collect(Collectors.toList());
        List<RuleTitleDto> ruleDtos = worldDefinition.getRules().stream()
                .map(rule -> new RuleTitleDto(
                        rule.getName(),
                        rule.getTicks().toString(),
                        rule.getProbability().toString(),
                        rule.getActions().stream().map(action -> action.getType()).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        List<String> EnvVariableNames = worldDefinition.getEnvironmentProperties().values().stream()
                .map(EnvPropertyDefinition::getName).collect(Collectors.toList());
        Integer populationSpace = worldDefinition.getGrid().getColumns()*worldDefinition.getGrid().getRows();
        return new SimulationTitlesDetails("simulatiom",entitiesNames, ruleDtos,EnvVariableNames, populationSpace, worldDefinition.getThreadCount());

    }

    public EntityPropertyDetailDto getEntityPropertiesDetail(String value) {
            return new EntityPropertyDetailDto(this.worldDefinition.getEntities().get(value).getProperties().values().stream()
                    .map(PropertyDefinition::toString).collect(Collectors.toList()));
    }

    public RuleActivationDto getActivationDetails(String ruleName) {
        Rule rule = this.worldDefinition.getRules().stream()
                .filter(rule1 -> ruleName.equals(rule1.getName()))
                .findFirst().get();
        return new RuleActivationDto(rule.getTicks().toString(),rule.getProbability().toString());
    }

    public TerminationDto getTerminationDetails() {
        return new TerminationDto(this.worldDefinition.getTerminationConditions().values().stream().collect(Collectors.
                toMap(termination -> termination.getType().toString(),
                        termination -> {
                            return termination.getType().equals(TerminationType.BYUSER) ?  0:termination.getCount();
                        })));
    }

    public EnviormentVariablesDto getEnvVariableDetails() {
        return  new EnviormentVariablesDto(this.worldDefinition.getEnvironmentProperties().values().stream()
                .map(env -> new EnvVariableDto(
                        env.getName(),
                        env.getType().toString(),
                        env.hasRange()? env.getRange().toString():null))
                .collect(Collectors.toList()));
    }

    public ActionDetailsDto getActionDetails(int actionIndex, String ruleName) {
        return  new ActionDetailsDto(this.worldDefinition.getRules().stream().filter(rule -> rule.getName().equals(ruleName)).findFirst()
                .get().getActions().get(actionIndex).getDetails().getActionDetails());
    }

    public GridDto getGridDetails() {
        Grid grid = this.worldDefinition.getGrid();
        return new GridDto(grid.getRows().toString(),grid.getColumns().toString());
    }

    public EnvPropDto getEnvProp(String envPropName) {
        EnvPropertyDefinition envProp = this.worldDefinition.getEnvironmentProperties().get(envPropName);
        Range range = envProp.hasRange()? envProp.getRange():null;
        return new EnvPropDto(envPropName,envProp.getType().toString().toLowerCase(), range);
    }

    public List<EnvPropDto> getAllEnvProps() {
        List<EnvPropDto> envPropDtoList = new ArrayList<>();

        for (Map.Entry<String, EnvPropertyDefinition> entry : this.worldDefinition.getEnvironmentProperties().entrySet()) {
            String envPropName = entry.getKey();
            EnvPropertyDefinition envProp = entry.getValue();
            Range range = envProp.hasRange() ? envProp.getRange() : null;

            EnvPropDto envPropDto = new EnvPropDto(envPropName, envProp.getType().toString().toLowerCase(), range);
            envPropDtoList.add(envPropDto);
        }

        return envPropDtoList;
    }

    public WorldDefinition getWorldDefinition() {
        return worldDefinition;
    }

    public void InitializePopulation(Map<String, Integer> entitiesPopulationMap)
    {
        for (Map.Entry<String, Integer> entityPopulation : entitiesPopulationMap.entrySet()) {
            worldDefinition.getEntities().get(entityPopulation.getKey()).setPopulation(entityPopulation.getValue());
        }
    }

    public void resetSimulationList() {
        simulationList = new HashMap<>();
    }

    public void setWorldDefinition(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
    }
}
