package Dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class SimulationExecutionDto {

    String id;
    String Status;
    IntegerProperty tick;
    IntegerProperty runningTimeInSeconds;
    private Thread timerThread;
    Map<String, IntegerProperty> entitiesPopulation;
    boolean isRunning;

    public SimulationExecutionDto(String id, String status) {
        this.id = id;
        Status = status;
        tick = new SimpleIntegerProperty(0);
        runningTimeInSeconds = new SimpleIntegerProperty(0);
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public IntegerProperty getTickProperty() {
        return tick;
    }
    public IntegerProperty getTimeProperty() {
        return runningTimeInSeconds;
    }


    public IntegerProperty tickProperty() {
        return tick;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return Status;
    }

    public void IncrementTick(){
        this.tick.set(this.tick.get()+1);
    }
    public void DecrementEntityPopulation(String entity){
        IntegerProperty population = this.entitiesPopulation.get(entity);
        population.set(population.get()-1);
    }
    public void UpdateEntitiesPopulation(Map<String, Integer> entitiesPopulation2){
        for (Map.Entry<String, Integer> entry : entitiesPopulation2.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            if (this.entitiesPopulation.containsKey(key)) {
                entitiesPopulation.get(key).set(value);
            }
        }
    }

    public Map<String, IntegerProperty> getEntitiesPopulation() {
        return entitiesPopulation;
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
