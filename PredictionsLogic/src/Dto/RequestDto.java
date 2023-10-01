package Dto;

import java.util.HashMap;

public class RequestDto extends BaseDto {
    private String userName;
    private String simulationName;
    private Integer requestedRuns;
    private HashMap<String, String> terminationConditionMap;

    public RequestDto(String simulationName, Integer requestedRuns, HashMap<String, String> terminationConditionMap,String userName) {
        this.simulationName = simulationName;
        this.requestedRuns = requestedRuns;
        this.terminationConditionMap = terminationConditionMap;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public Integer getRequestedRuns() {
        return requestedRuns;
    }

    public HashMap<String, String> getTerminationConditionMap() {
        return terminationConditionMap;
    }
}
