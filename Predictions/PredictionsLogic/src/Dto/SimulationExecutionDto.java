package Dto;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class SimulationExecutionDto {

    String id;
    String Status;
    IntegerProperty tick;
    IntegerProperty runningTimeInSeconds;
    private Thread timerThread;
    Map<String, IntegerProperty> EntitiesPopulation;

    public SimulationExecutionDto(String id, String status) {
        this.id = id;
        Status = status;
        tick = new SimpleIntegerProperty(0);
        runningTimeInSeconds = new SimpleIntegerProperty(0);
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return Status;
    }

    public void IncrementTick(){
        Platform.runLater(() -> this.tick.set(this.tick.get()+1));
    }
    public void DecrementEntityPopulation(String entity){
        IntegerProperty population = this.EntitiesPopulation.get(entity);
        Platform.runLater(() -> population.set(population.get()-1));
    }


    public void startTimer() {
        if (timerThread == null || !timerThread.isAlive()) {
            timerThread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(1000);
                        runningTimeInSeconds.set(runningTimeInSeconds.get() + 1);
                    }
                } catch (InterruptedException e) {
                    // Handle interruption (thread stopped gracefully)
                }
            });
            timerThread.setDaemon(true);
            timerThread.start();
        }
    }
    public void stopTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
    }
}
