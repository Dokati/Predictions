package Dto;

import java.util.Map;

public class RunSimulationDto {
    private Integer requestNum;
    private Map<String, Integer> entitiesPopulationMap;
    private Map<String, String> envPropValue;

    public RunSimulationDto() {
        // Default constructor
    }

    public RunSimulationDto(Integer requestNum, Map<String, Integer> entitiesPopulationMap, Map<String, String> envPropValue) {
        this.requestNum = requestNum;
        this.entitiesPopulationMap = entitiesPopulationMap;
        this.envPropValue = envPropValue;
    }

    public Integer getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public Map<String, Integer> getEntitiesPopulationMap() {
        return entitiesPopulationMap;
    }

    public void setEntitiesPopulationMap(Map<String, Integer> entitiesPopulationMap) {
        this.entitiesPopulationMap = entitiesPopulationMap;
    }

    public Map<String, String> getEnvPropValue() {
        return envPropValue;
    }

    public void setEnvPropValue(Map<String, String> envPropValue) {
        this.envPropValue = envPropValue;
    }

    @Override
    public String toString() {
        return "RunSimulationDto{" +
                "requestNum=" + requestNum +
                ", entitiesPopulationMap=" + entitiesPopulationMap +
                ", envPropValue=" + envPropValue +
                '}';
    }
}
