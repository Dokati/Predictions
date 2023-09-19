package Body.ThirdScreen;

import Dto.SimulationExecutionDto;
import Manager.PredictionManager;
import PrimaryContreoller.PrimaryController;
import World.instance.SimulationStatusType;
import World.instance.WorldInstance;
import javafx.application.Platform;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;

public class RunSimulationTask extends Task<Boolean> {

    WorldInstance worldInstance;
    SimulationExecutionDto simulationExecutionDto;
    PrimaryController primaryController;
    PredictionManager predictionManager;
    public RunSimulationTask(WorldInstance worldInstance, SimulationExecutionDto simulationExecutionDto,
                             PrimaryController primaryController, PredictionManager predictionManager) {
        this.worldInstance = worldInstance;
        this.simulationExecutionDto = simulationExecutionDto;
        this.primaryController = primaryController;
        this.predictionManager = predictionManager;
    }


    @Override
    protected Boolean call() throws Exception {


        while(worldInstance.getStatus().equals(SimulationStatusType.Running) ||
                worldInstance.getStatus().equals(SimulationStatusType.Pause ) ||
                (!simulationExecutionDto.isProgressable() && simulationExecutionDto.isRunning())){


            while(worldInstance.getStatus().equals(SimulationStatusType.Pause)){
                Thread.sleep(200);
            }

            sampleEngineAndUpdateUi();

            try {
                // Sleep for 0.2 seconds (200 milliseconds)
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Handle the InterruptedException if needed
                e.printStackTrace();
            }


        }
        sampleEngineAndUpdateUi();
        simulationExecutionDto.setEndSimulationDetails(worldInstance.getSimulationDetailsMap());
        Thread.sleep(200);
        Platform.runLater(this::ToDoWhenSimulationHasFinished);


        return null;
    }

    private void sampleEngineAndUpdateUi() {
        Integer tick = worldInstance.getTick();
        Integer time = Math.toIntExact(worldInstance.getRunningTimeInSeconds());

        Map<String, IntegerProperty> entitiesPopulation = new HashMap<>();
        worldInstance.getSimulationDetailsMap().entrySet().stream().forEach(entry -> {
            entitiesPopulation.put(entry.getKey(),new SimpleIntegerProperty(entry.getValue().getCurrentPopulation()));
        });


        Platform.runLater(()->simulationExecutionDto.setTick(tick));
        Platform.runLater(()->simulationExecutionDto.setRunningTimeInSeconds(time));
        Platform.runLater(()->simulationExecutionDto.UpdateEntitiesPopulation(entitiesPopulation));
        Platform.runLater(()->primaryController.getThirdScreenControler().RefreshEntityPopTable());

        if(simulationExecutionDto.isProgressable()){
            Integer maxTick = worldInstance.getTicksTermination().getCount();
            Integer maxSeconds = worldInstance.getSecTermination().getCount();
            double tickProgress = maxTick!=null? (double)tick/maxTick:0.0;
            double secondsProgress = maxSeconds!=null? (double)time/maxSeconds:0.0;
            double progress = Math.max(tickProgress,secondsProgress);
            Platform.runLater(()->simulationExecutionDto.UpdateProgress(progress));
        }
    }

    private void ToDoWhenSimulationHasFinished() {
        this.simulationExecutionDto.FinishRunning();
        this.primaryController.SimulationFinished(simulationExecutionDto.getNumberId());
    }
}
