package Manager;

import Context.Context;
import Dto.*;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Grid.Grid;
import PRD.PRDWorld;
import Property.Range;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Property.instance.EnvPropertyInstance;
import Rule.Rule;
import Terminition.TerminationType;
import World.definition.WorldDefinition;
import World.instance.WorldInstance;

import javax.xml.bind.JAXB;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PredictionManager {

    WorldDefinition worldDefinition;

    WorldInstance worldInstance;

    HashMap<String,WorldInstance> previousActivations;


    public PredictionManager() {
        worldDefinition = null;
        worldInstance = null;
        previousActivations = new HashMap<>();
    }

    public SimulationTitlesDetails loadSimulation(String filePath)
    {

        try {
            PRDWorld pRDworld = JAXB.unmarshal(new File(filePath), PRDWorld.class);
            worldDefinition = new WorldDefinition(pRDworld);
        }
        catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Loading the XML file failed");
        }

        ///System.out.println("The XML file has been loaded successfully\n");

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

    public void runSimulationDEMO()
    {
        if(worldDefinition == null)
        {
            throw new RuntimeException("There is no simulation loaded in the system");
        }
        InitializePopulationDEMO(); //every population is 10
        worldInstance = new WorldInstance(worldDefinition);
        InitializeEnvVariablesValueDEMO();
        boolean simulationTermBySecond = false;
        boolean simulationTermByTicks = false;
        boolean simulationFinished = false;
        Instant endTime = null;
        Context context;
        int tick = 1;

        if(worldInstance.getTerminationConditions().containsKey(TerminationType.TICK)){
            simulationTermByTicks = true;
        }
        if(worldInstance.getTerminationConditions().containsKey(TerminationType.SECOND)){
            simulationTermBySecond = true;
            endTime = Instant.now().plusSeconds(worldInstance.getSecTermination().getCount());
        }

        while (!simulationFinished) {

            MoveEntitiesOneStepRandomly(); // 1.move all entities one step

            for (EntityInstance entity : this.worldInstance.getEntities()) {
                if (!entity.getAlive()) {
                    continue;
                }
                context = new Context(entity, this.worldInstance, this.worldInstance.getEnvironmentProperties(),tick);

                for (Rule rule : worldInstance.getRules()) {
                    if (rule.RuleIsRunnable(tick)) {
                        rule.RunRule(context);
                    }
                }
            }

            if (simulationTermBySecond && Instant.now().isAfter(endTime)) {
                simulationFinished = true;
                System.out.println("The Activation ended after " + worldDefinition.getTerminationConditions().get(TerminationType.SECOND).getCount() + " seconds");
            }
            if (simulationTermByTicks && tick >= worldInstance.getTicksTermination().getCount()) {
                simulationFinished = true;
                System.out.println("The Activation ended after " + worldDefinition.getTerminationConditions().get(TerminationType.TICK).getCount() + " ticks");
            }
            tick++;
        }
    }

    private void MoveEntitiesOneStepRandomly() {
        for (EntityInstance entity : worldInstance.getEntities()) {
            worldInstance.getGrid()[entity.getCoordinate().getX()][entity.getCoordinate().getY()] = null;
            entity.getCoordinate().chooseRandomNeighbor(worldInstance.getGrid());
            worldInstance.getGrid()[entity.getCoordinate().getX()][entity.getCoordinate().getY()] = entity;
        }
    }

    private void InitializeEnvVariablesValueDEMO() {
        for (Map.Entry<String, EnvPropertyInstance> entry : this.worldInstance.getEnvironmentProperties().entrySet()) {
            entry.getValue().setValue(entry.getValue().getPropertyDef().getType().randomInitialization(entry.getValue().getPropertyDef().getRange()));
        }
    }

    private void InitializePopulationDEMO(){
        for (Map.Entry<String, EntityDefinition> entityDef : this.worldDefinition.getEntities().entrySet()) {
            entityDef.getValue().setPopulation(10);
        }
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
                            return termination.getType().toString()== "BYUSER"?  0:termination.getCount();
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

    public ActionDetailsDto getActionDetails(String actionName, String ruleName) {
        return  new ActionDetailsDto(this.worldDefinition.getRules().stream().filter(rule -> rule.getName().equals(ruleName)).findFirst()
                .get().getActions().stream().filter(action -> action.getType().equals(actionName)).findFirst().get().getDetails().getActionDetails());
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

    public void runSimulation(Map<String, Integer> entitiesPopulationMap, Map<String, String> envPropValues) {
    }
}
