package World.instance;

import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Grid.Grid;
import Property.definition.EnvPropertyDefinition;
import Property.instance.EnvPropertyInstance;
import Rule.Rule;
import Terminition.Termination;
import Terminition.TerminationType;
import World.definition.WorldDefinition;

import java.util.*;

public class WorldInstance {

    private List<EntityInstance> entities;
    private HashMap<String, EnvPropertyInstance> environmentProperties;
    private List<Rule> rules;
    private HashMap<TerminationType,Termination> terminationConditions;
    private EntityInstance[][] grid;
    private Integer threadCount;

    public WorldInstance(WorldDefinition worldDef){
        //Get EnvProperties
        environmentProperties = new HashMap<>();
        for (Map.Entry<String, EnvPropertyDefinition> envProperty : worldDef.getEnvironmentProperties().entrySet()) {
            this.environmentProperties.put(envProperty.getKey(),new EnvPropertyInstance(envProperty.getValue()));
        }

        //Get grid
        grid = new EntityInstance[worldDef.getGrid().getRows()][worldDef.getGrid().getColumns()];
        //Get entities
        int entityId = 1;
        entities = new ArrayList<>();
        for (Map.Entry<String, EntityDefinition> entity : worldDef.getEntities().entrySet()) {
            for (int i = 0; i < entity.getValue().getPopulation(); i++) {
                this.entities.add(new EntityInstance(entityId,entity.getValue(),grid));
                entityId++;
            }
        }

        //Get termination
        terminationConditions = worldDef.getTerminationConditions();

        //Get rules
        rules = worldDef.getRules();

        //Get threads
        this.threadCount = worldDef.getThreadCount();

    }

    public EntityInstance[][] getGrid() {
        return grid;
    }

    public Integer getGridRows()
    {
        if(grid != null){
            return grid.length;
        }
        return 0;
    }

    public Integer getGridCols()
    {
        if(grid != null){
            return grid[0].length;
        }
        return 0;
    }

    public List<EntityInstance> getEntities() {
        return entities;
    }

    public HashMap<String, EnvPropertyInstance> getEnvironmentProperties() {
        return environmentProperties;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public HashMap<TerminationType,Termination> getTerminationConditions() {
        return terminationConditions;
    }

    public Termination getTicksTermination(){
        if(terminationConditions.containsKey(TerminationType.TICK)){
            return terminationConditions.get(TerminationType.TICK);
        }
        return null;
    }

    public Termination getSecTermination(){
        if(terminationConditions.containsKey(TerminationType.SECOND)){
            return terminationConditions.get(TerminationType.SECOND);
        }
        return null;
    }

    public Set<EntityDefinition> getUniqueEntityDefinitions() {
        return null;
    }

    public Map<Integer, EntityInstance> getAliveEntityInstances() {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }
}
