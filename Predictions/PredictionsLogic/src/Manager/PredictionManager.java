package Manager;

import Dto.*;
import Grid.Grid;
import PRD.PRDWorld;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Rule.Rule;
import World.definition.WorldDefinition;
import World.instance.WorldInstance;

import javax.xml.bind.JAXB;
import java.io.File;
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
        List<String> EnvVariableNames = worldDefinition.getEnvironmentProperties().values().stream().map(EnvPropertyDefinition::getName).collect(Collectors.toList());
        return new SimulationTitlesDetails(entitiesNames, ruleDtos,EnvVariableNames);

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
}
