package Manager;

import Dto.*;
import Grid.Grid;
import PRD.PRDWorld;
import Property.Range;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Rule.Rule;
import Terminition.TerminationType;
import World.definition.WorldDefinition;
import World.instance.WorldInstance;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PredictionManager {

    WorldDefinition worldDefinition;
    public ExecutorService threadPool;
    HashMap<Integer, WorldInstance> simulationList;
    Integer simulationIdNumber;

    public PredictionManager() {
        simulationList = new HashMap<>();
        simulationIdNumber = 1;
        worldDefinition = null;
    }
    public HashMap<Integer, WorldInstance> getSimulationList() {
        return simulationList;
    }

    public Integer getSimulationIdNumber() {
        return simulationIdNumber;
    }

    public void setSimulationIdNumber(Integer simulationIdNumber) {
        this.simulationIdNumber = simulationIdNumber;
    }

    public SimulationTitlesDetails loadSimulation(String filePath)
    {

        try {
            PRDWorld pRDworld = JAXB.unmarshal(new File(filePath), PRDWorld.class);
            worldDefinition = new WorldDefinition(pRDworld);
            threadPool = Executors.newFixedThreadPool(worldDefinition.getThreadCount());
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
        return new SimulationTitlesDetails(entitiesNames, ruleDtos,EnvVariableNames, populationSpace);

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

    public void setWorldDefinition(WorldDefinition worldDefinition) {
        this.worldDefinition = worldDefinition;
    }

    public void InitializePopulation(Map<String, Integer> entitiesPopulationMap)
    {
        for (Map.Entry<String, Integer> entityPopulation : entitiesPopulationMap.entrySet()) {
            worldDefinition.getEntities().get(entityPopulation.getKey()).setPopulation(entityPopulation.getValue());
        }
    }

}
