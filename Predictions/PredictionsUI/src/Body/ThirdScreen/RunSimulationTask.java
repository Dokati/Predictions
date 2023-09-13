package Body.ThirdScreen;

import Dto.SimulationExecutionDto;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import World.instance.SimulationStatusType;
import World.instance.WorldInstance;
import javafx.application.Platform;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RunSimulationTask extends Task<Boolean> {

    WorldInstance worldInstance;
    SimulationExecutionDto simulationExecutionDto;
    public RunSimulationTask(WorldInstance worldInstance, SimulationExecutionDto simulationExecutionDto) {
        this.worldInstance = worldInstance;
        this.simulationExecutionDto = simulationExecutionDto;
    }

    @Override
    protected Boolean call() throws Exception {


        while(worldInstance.getStatus().equals(SimulationStatusType.Running)){

            Integer tick = worldInstance.getTick();
            Integer time = Math.toIntExact(worldInstance.getRunningTimeInSeconds());
//            Map<String, IntegerProperty> entitiesPopulation = worldInstance.getEntities().stream()
//                    .collect(Collectors.toMap(
//                            entity -> entity.getEntityDef().getName(),
//                            entity -> new SimpleIntegerProperty(entity.getEntityDef().getPopulation())));

            Map<String, IntegerProperty> entitiesPopulation = new HashMap<>();
            worldInstance.getSimulationDetailsMap().entrySet().stream().forEach(entry -> {
                entitiesPopulation.put(entry.getKey(),new SimpleIntegerProperty(entry.getValue().getCurrentPopulation()));
            });
//            List<EntityInstance> a = worldInstance.getEntities();
//            for (EntityInstance entity : worldInstance.getEntities()) {
//                EntityDefinition definition = entity.getEntityDef();
//                String name = definition.getName();
//                int population = definition.getPopulation();
//                entitiesPopulation.put(name, new SimpleIntegerProperty(population));
//            }

            Platform.runLater(()->simulationExecutionDto.setTick(tick));
            Platform.runLater(()->simulationExecutionDto.setRunningTimeInSeconds(time));
            Platform.runLater(()->simulationExecutionDto.UpdateEntitiesPopulation(entitiesPopulation));


            try {
                // Sleep for 2 seconds (2000 milliseconds)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
                e.printStackTrace();
            }
        }

        return null;
    }
}
