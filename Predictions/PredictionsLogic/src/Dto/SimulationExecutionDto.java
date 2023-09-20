package Dto;

import EndSimulationDetails.EntitySimulationEndDetails;
import World.definition.WorldDefinition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Body.SecondScreen.EnvPropTableItem;


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
    DoubleProperty progress;
    boolean isRunning;
    boolean isPaused;
    boolean isProgressable;
    private HashMap<String, EntitySimulationEndDetails> endSimulationDetails;
    WorldDefinition simulationWorldDefinition;
    ObservableList<EnvPropTableItem> EnvPropTableItemList;
    Map<String, String> envPropValues;
    Map<String, Integer> populationMap;


    public SimulationExecutionDto(String id, String status, int numberId, Map<String, Integer> entitiesPopulationMap,
                                  boolean isProgressable, WorldDefinition worldDefinition, ObservableList<EnvPropTableItem> items, Map<String, String> envPropValues, Map<String, Integer> populationMap) {
        this.id = id;
        Status = status;
        tick = new SimpleIntegerProperty(0);
        runningTimeInSeconds = new SimpleIntegerProperty(0);
        isRunning = true;
        isPaused = false;
        this.numberId = numberId;
        this.entitiesPopulation = entitiesPopulationMap.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> new SimpleIntegerProperty(entry.getValue())));
        progress = new SimpleDoubleProperty(0);
        this.isProgressable = isProgressable;
        this.simulationWorldDefinition = worldDefinition;
        this.EnvPropTableItemList = FXCollections.observableArrayList();;
        for (EnvPropTableItem item : items) {
            this.EnvPropTableItemList.add(new EnvPropTableItem(item.getName(), item.getType(), item.getValue()));
        }
        this.envPropValues = new HashMap<>();
        this.envPropValues.putAll(envPropValues);
        this.populationMap = new HashMap<>();
        this.populationMap.putAll(populationMap);
    }

    public ObservableList<EnvPropTableItem> getEnvPropTableItemList() {
        return EnvPropTableItemList;
    }

    public HashMap<String, EntitySimulationEndDetails> getEndSimulationDetails() {
        return endSimulationDetails;
    }

    public void setEndSimulationDetails(HashMap<String, EntitySimulationEndDetails> endSimulationDetails) {
        this.endSimulationDetails = endSimulationDetails;
    }

    public DoubleProperty getProgress() {
        return progress;
    }

    public DoubleProperty progressProperty() {
        return progress;
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


    public boolean isProgressable() {
        return isProgressable;
    }

    public void UpdateProgress(double progress) {
        this.progress.set(progress);
    }

    public WorldDefinition getSimulationWorldDefinition() {
        return simulationWorldDefinition;
    }

    public Map<String, String> getEnvPropValues() {
        return envPropValues;
    }

    public Integer getSpace()
    {
        return this.simulationWorldDefinition.getGrid().getSpace();
    }

    public Map<String, Integer> getPopulationMap() {
        return populationMap;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
