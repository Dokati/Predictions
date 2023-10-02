package Manager;


import Dto.*;
import Grid.Grid;
import PRD.PRDWorld;
import Property.Range;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Rule.Rule;
import Terminition.TerminationType;
import UserRequest.*;
import World.definition.WorldDefinition;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AdminManager {
    private Map<String, WorldDefinition> worldDefinitions;
    private ExecutorService threadPool;
    private Integer threadPoolSize;
    private Map<Integer, UserRequest> requests;
    private Map<String, PredictionManager> users;

    public AdminManager() {
        this.worldDefinitions = new HashMap<>();
        this.threadPoolSize = null;
        this.threadPool = null;
        this.requests = new HashMap<>();
        this.users = new HashMap<>();
    }

    public EntityPropertyDetailDto getEntityPropertiesDetail(String defName,String value) {
        return new EntityPropertyDetailDto(worldDefinitions.get(defName).getEntities().get(value).getProperties().values().stream()
                .map(PropertyDefinition::toString).collect(Collectors.toList()));
    }

    public RuleActivationDto getActivationDetails(String defName,String ruleName) {
        Rule rule = worldDefinitions.get(defName).getRules().stream()
                .filter(rule1 -> ruleName.equals(rule1.getName()))
                .findFirst().get();
        return new RuleActivationDto(rule.getTicks().toString(),rule.getProbability().toString());
    }

    public TerminationDto getTerminationDetails(String defName) {
        return new TerminationDto(worldDefinitions.get(defName).getTerminationConditions().values().stream().collect(Collectors.
                toMap(termination -> termination.getType().toString(),
                        termination -> {
                            return termination.getType().equals(TerminationType.BYUSER) ?  0:termination.getCount();
                        })));
    }

    public EnviormentVariablesDto getEnvVariableDetails(String defName) {
        return  new EnviormentVariablesDto(worldDefinitions.get(defName).getEnvironmentProperties().values().stream()
                .map(env -> new EnvVariableDto(
                        env.getName(),
                        env.getType().toString(),
                        env.hasRange()? env.getRange().toString():null))
                .collect(Collectors.toList()));
    }



    public ActionDetailsDto getActionDetails(String defName,int actionIndex, String ruleName) {
        return  new ActionDetailsDto(worldDefinitions.get(defName).getRules().stream().filter(rule -> rule.getName().equals(ruleName)).findFirst()
                .get().getActions().get(actionIndex).getDetails().getActionDetails());
    }

    public GridDto getGridDetails(String defName) {
        Grid grid = worldDefinitions.get(defName).getGrid();
        return new GridDto(grid.getRows().toString(),grid.getColumns().toString());
    }

    public EnvPropDto getEnvProp(String defName, String envPropName) {
        EnvPropertyDefinition envProp = worldDefinitions.get(defName).getEnvironmentProperties().get(envPropName);
        Range range = envProp.hasRange()? envProp.getRange():null;
        return new EnvPropDto(envPropName,envProp.getType().toString().toLowerCase(), range);
    }

    public List<EnvPropDto> getAllEnvProps(String defName) {
        List<EnvPropDto> envPropDtoList = new ArrayList<>();

        for (Map.Entry<String, EnvPropertyDefinition> entry :worldDefinitions.get(defName).getEnvironmentProperties().entrySet()) {
            String envPropName = entry.getKey();
            EnvPropertyDefinition envProp = entry.getValue();
            Range range = envProp.hasRange() ? envProp.getRange() : null;

            EnvPropDto envPropDto = new EnvPropDto(envPropName, envProp.getType().toString().toLowerCase(), range);
            envPropDtoList.add(envPropDto);
        }

        return envPropDtoList;
    }

    public WorldDefinition getWorldDefinition(String defName) {
        return worldDefinitions.get(defName);
    }

    public void runSimulationByRequestNumber(Integer requestNum, Map<String, Integer> entitiesPopulationMap, Map<String, String> envPropValue){
        Integer simulationNumber = users.get(requests.get(requestNum).getUsername()).initPredictionManager(worldDefinitions.get(requests.get(requestNum).getSimulationName()),
                entitiesPopulationMap,requests.get(requestNum).getTerminationConditions(),envPropValue,requests.get(requestNum));

        threadPool.submit(users.get(requests.get(requestNum).getUsername()).getSimulationList().get(simulationNumber));
    }

    public void addRequestToList(RequestDto requestDto){
        UserRequest request = new UserRequest(requests.size(),requestDto.getSimulationName(),requestDto.getUserName()
                ,requestDto.getRequestedRuns(),requestDto.getTerminationConditionMap());
        requests.put(requests.size(),request);
        users.get(requestDto.getUserName()).addRequest(request);
    }

    public List<AdminRequestDto> getRequestslist(){
        List<AdminRequestDto> res = new ArrayList<>();

        for (Map.Entry<Integer, UserRequest> request : requests.entrySet()){
            res.add(new AdminRequestDto(request.getValue()));
        }

        return res;
    }

    public void SetRequestStatus(Integer requestNum, UserRequestStatusType status){
        requests.get(requestNum).setRequestStatus(status);
    }

    public List<FullRequestDto> getUserRequestslist(String userName){
        return users.get(userName).getRequestslist();
    }

    public SimulationTitlesDetails addSimulationToList(String xmlContent)
    {
        WorldDefinition worldDefinition = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PRDWorld.class);

            // Create an Unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Create a StringReader to read from the XML content String
            StringReader stringReader = new StringReader(xmlContent);

            // Unmarshal from the StringReader
            PRDWorld pRDworld = (PRDWorld) unmarshaller.unmarshal(stringReader);
            if(worldDefinitions.containsKey(pRDworld.getName())){
                throw new IllegalArgumentException("File with this name system already exists");
            }
            worldDefinitions.put(pRDworld.getName(),new WorldDefinition(pRDworld));
            worldDefinition = worldDefinitions.get(pRDworld.getName());
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Loading the XML file failed");
        }

        if(worldDefinition != null){
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
            return new SimulationTitlesDetails(worldDefinition.getName(), entitiesNames, ruleDtos,EnvVariableNames, populationSpace, worldDefinition.getThreadCount());
        }
        return null;
    }

    public void AddUserToList(String userName){

        if(users.containsKey(userName)){
            throw new IllegalArgumentException("User with this name system already exists");
        }

        users.put(userName,new PredictionManager());
    }

    public void RemoveUserFromList(String userName){
            users.remove(userName);
    }

    public List<SimulationTitlesDetails> getAllSimulationsDetails(){

        List<SimulationTitlesDetails> res = new ArrayList<>();

        for (Map.Entry<String, WorldDefinition> worldDefinition : worldDefinitions.entrySet() ){
            List<String> entitiesNames = worldDefinition.getValue().getEntities().keySet().stream().collect(Collectors.toList());
            List<RuleTitleDto> ruleDtos = worldDefinition.getValue().getRules().stream()
                    .map(rule -> new RuleTitleDto(
                            rule.getName(),
                            rule.getTicks().toString(),
                            rule.getProbability().toString(),
                            rule.getActions().stream().map(action -> action.getType()).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());
            List<String> EnvVariableNames = worldDefinition.getValue().getEnvironmentProperties().values().stream()
                    .map(EnvPropertyDefinition::getName).collect(Collectors.toList());
            Integer populationSpace = worldDefinition.getValue().getGrid().getColumns()*worldDefinition.getValue().getGrid().getRows();
            res.add(new SimulationTitlesDetails(worldDefinition.getValue().getName(), entitiesNames, ruleDtos,EnvVariableNames, populationSpace, worldDefinition.getValue().getThreadCount()));
        }

        return res;
    }

    public Integer getNumOfSimulations() { return worldDefinitions.size(); }

    public Map<String, WorldDefinition> getWorldDefinitions() {
        return worldDefinitions;
    }

    public void setWorldDefinitions(Map<String, WorldDefinition> worldDefinitions) {
        this.worldDefinitions = worldDefinitions;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public Map<String, PredictionManager> getUsers() {
        return users;
    }

    public void setUsers(Map<String, PredictionManager> users) {
        this.users = users;
    }
}
