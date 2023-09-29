package Manager;

import Dto.RuleTitleDto;
import Dto.SimulationTitlesDetails;
import PRD.PRDWorld;
import Property.definition.EnvPropertyDefinition;
import World.definition.WorldDefinition;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AdminManager {
    private Map<String, WorldDefinition> worldDefinitions;
    private ExecutorService threadPool;
    private Integer threadPoolSize;
    private Map<String, PredictionManager> users;

    public AdminManager() {
        this.worldDefinitions = new HashMap<>();
        this.threadPoolSize = null;
        this.threadPool = null;
        this.users = new HashMap<>();
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


    public Map<String, WorldDefinition> getWorldDefinitions() {
        return worldDefinitions;
    }

    public void setWorldDefinitions(Map<String, WorldDefinition> worldDefinitions) {
        this.worldDefinitions = worldDefinitions;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public Map<String, PredictionManager> getUsers() {
        return users;
    }

    public void setUsers(Map<String, PredictionManager> users) {
        this.users = users;
    }
}
