package Dto;

import World.instance.SimulationStatusType;

import java.util.HashMap;
import java.util.Map;

public class SimulationDetailsDto {
    private String tick;
    private String runningTimeInSeconds;
    private Map<String, String> entitiesPopulation;
    private String status;

    public SimulationDetailsDto() {
        // Default constructor
    }

    public SimulationDetailsDto(Integer tick, Long runningTimeInSeconds, Map<String, Integer> entitiesPopulation, SimulationStatusType status) {
        this.tick = tick.toString();
        this.runningTimeInSeconds = runningTimeInSeconds.toString();
        this.entitiesPopulation = new HashMap<>();
        for (Map.Entry<String, Integer> entityPopulation : entitiesPopulation.entrySet()){
            this.entitiesPopulation.put(entityPopulation.getKey(),entityPopulation.getValue().toString());
        }
        this.status = status.toString();
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

    public String getRunningTimeInSeconds() {
        return runningTimeInSeconds;
    }

    public void setRunningTimeInSeconds(String runningTimeInSeconds) {
        this.runningTimeInSeconds = runningTimeInSeconds;
    }

    public Map<String, String> getEntitiesPopulation() {
        return entitiesPopulation;
    }

    public void setEntitiesPopulation(Map<String, String> entitiesPopulation) {
        this.entitiesPopulation = entitiesPopulation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SimulationDetailsDto{" +
                "tick='" + tick + '\'' +
                ", runningTimeInSeconds='" + runningTimeInSeconds + '\'' +
                ", entitiesPopulation=" + entitiesPopulation +
                ", status='" + status + '\'' +
                '}';
    }
}
