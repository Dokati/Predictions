package Dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationExecutionDto {

    String id;
    Integer numberId;
    String Status;
    IntegerProperty tick;
    IntegerProperty runningTimeInSeconds;
    Map<String, IntegerProperty> entitiesPopulation;
    boolean isRunning;

    public SimulationExecutionDto(String id, String status, int numberId, Map<String, Integer> entitiesPopulationMap) {
        this.id = id;
        Status = status;
        tick = new SimpleIntegerProperty(0);
        runningTimeInSeconds = new SimpleIntegerProperty(0);
        isRunning = true;
        this.numberId = numberId;
        this.entitiesPopulation = entitiesPopulationMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new SimpleIntegerProperty(entry.getValue())
                ));

    }

    public void setStatus(String status) {
        Status = status;
    }

    public void FinishRunning(){
        this.isRunning = false;
    }

    public void setTick(int tick) {
        this.tick.set(tick);
    }

    public Integer getNumberId() {
        return numberId;
    }

    public void setRunningTimeInSeconds(int runningTimeInSeconds) {
        this.runningTimeInSeconds.set(runningTimeInSeconds);
    }

    public void UpdateEntitiesPopulation(Map<String, IntegerProperty> entitiesPopulation2) {
        for (Map.Entry<String, IntegerProperty> entry : entitiesPopulation2.entrySet()) {
            String key = entry.getKey();
            IntegerProperty Newvalue = entry.getValue();
            IntegerProperty oldValue = this.entitiesPopulation.get(key);
            oldValue.set(Newvalue.get());
        }
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

    public Map<String, IntegerProperty> getEntitiesPopulation() {
        return entitiesPopulation;
    }



}
