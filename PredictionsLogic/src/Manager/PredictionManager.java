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
import World.instance.SimulationStatusType;
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
        requests = new ArrayList<>();
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

    public SimulationDetailsDto getSimulationDetailsDto(Integer simulationId){
        return simulationList.get(simulationId).getSimulationDetailsDto();
    }

    public void setSimulationStatus(Integer simulationId, SimulationStatusType status){
        simulationList.get(simulationId).setStatus(status);
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

    public void InitializePopulation(Map<String, Integer> entitiesPopulationMap)
    {
        for (Map.Entry<String, Integer> entityPopulation : entitiesPopulationMap.entrySet()) {
            worldDefinition.getEntities().get(entityPopulation.getKey()).setPopulation(entityPopulation.getValue());
        }
    }

    public void resetSimulationList() {
        simulationList = new HashMap<>();
    }

}
