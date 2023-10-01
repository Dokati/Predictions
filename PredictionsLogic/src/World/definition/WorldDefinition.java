package World.definition;

import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataDupEnvVarNameExceptions;
import Exceptions.IllegalXmlDataInvalidActionEntityExceptions;
import Grid.Grid;
import PRD.*;
import Property.definition.EnvPropertyDefinition;
import Rule.Rule;
import Terminition.Termination;
import Terminition.TerminationType;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldDefinition {

    private String name;
    private final HashMap<String, EntityDefinition> entities;
    private final HashMap<String, EnvPropertyDefinition> environmentProperties;
    private final ArrayList<Rule> rules;
    private final HashMap<TerminationType,Termination> terminationConditions;
    private final Grid grid;
    private Integer sleep;


    public WorldDefinition(PRDWorld pRDworld) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Get name
        name = pRDworld.getName();

        //Get EnvProperties
        environmentProperties = new HashMap<>();
        for(PRDEnvProperty envProperty : pRDworld.getPRDEnvironment().getPRDEnvProperty()) {
            CheckIfEnvPropertyAlreadyExistsInPRD(envProperty.getPRDName());
            this.environmentProperties.put(envProperty.getPRDName(),new EnvPropertyDefinition(envProperty));
        }

        //Get entities
        entities = new HashMap<>();
        for(PRDEntity entity : pRDworld.getPRDEntities().getPRDEntity()) {
            this.entities.put(entity.getName(),new EntityDefinition(entity));
        }

        //Get termination
        terminationConditions = new HashMap<>();

        //Get rules
        rules = new ArrayList<>();
        for (PRDRule rule : pRDworld.getPRDRules().getPRDRule()) {
            rules.add(new Rule(rule, this.entities, this.environmentProperties));
        }

        //Get grid
        grid = new Grid(pRDworld.getPRDGrid());

        //Get sleep
        sleep = pRDworld.getSleep();
    }

    public WorldDefinition(WorldDefinition worldDef ,HashMap<TerminationType,Termination> terminationConditions){
        this.name = worldDef.getName();

        this.environmentProperties = worldDef.getEnvironmentProperties();

        this.entities = new HashMap<>();
        for (Map.Entry<String, EntityDefinition> entity : entities.entrySet()){
            entities.put(entity.getKey(),new EntityDefinition(entity.getValue()));
        }

        this.terminationConditions = terminationConditions;

        this.rules = worldDef.getRules();

        this.grid = worldDef.getGrid();

        this.sleep = worldDef.getSleep();
    }

    public String getName() {
        return name;
    }

    public Integer getSleep() {
        return sleep;
    }

    public HashMap<String, EntityDefinition> getEntities() {
        return entities;
    }

    public Grid getGrid() {
        return grid;
    }


    public HashMap<String, EnvPropertyDefinition> getEnvironmentProperties() {
        return environmentProperties;
    }
    public ArrayList<Rule> getRules() {
        return rules;
    }

    private void CheckIfEnvPropertyAlreadyExistsInPRD(String envPropertyName)
    {
        if(this.environmentProperties.containsKey(envPropertyName)){
            throw new IllegalXmlDataDupEnvVarNameExceptions("Xml file contain two environment variables with the same name");
        }
    }

    public HashMap<TerminationType,Termination> getTerminationConditions() {
        return terminationConditions;
    }

    @Override
    public String toString() {
        StringBuilder entitiesstringBuilder = new StringBuilder();
        for(Map.Entry<String, EntityDefinition> entity : this.entities.entrySet()) {
            entitiesstringBuilder.append(entity.getValue());
        }

        StringBuilder rulesStringBuilder = new StringBuilder();
        for(Rule rule : this.rules)
            rulesStringBuilder.append(rule);

        StringBuilder terminationConditions = new StringBuilder();
        for(Map.Entry<TerminationType, Termination> terminationCondition : this.terminationConditions.entrySet())
            terminationConditions.append(terminationCondition.getValue());

        return  "entities:\n__________\n" + entitiesstringBuilder + "\n" +
                "rules:\n_______\n" + rulesStringBuilder + "\n" +
                "terminationConditions:\n______________________\n" + terminationConditions + "\n";
    }

    public Integer getThreadCount() {
        return 0;
    }
}



