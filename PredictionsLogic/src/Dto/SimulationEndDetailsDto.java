package Dto;

import EndSimulationDetails.EntitySimulationEndDetails;

import java.util.HashMap;
import java.util.Map;

public class SimulationEndDetailsDto {
    private final String simulationId;
    private Map<String, EntitySimulationEndDetailsDto> simulationEndDetails;
    private String userName;
    private String requestId;

    public SimulationEndDetailsDto(Integer simulationId, String userName, Integer requestId, HashMap<String, EntitySimulationEndDetails> simulationEndDetails) {
        this.simulationId = simulationId.toString();
        this.userName = userName;
        this.requestId = requestId.toString();

        this.simulationEndDetails = new HashMap<>();
        for (Map.Entry<String, EntitySimulationEndDetails> entityEndSimulationDetails : simulationEndDetails.entrySet()) {
            this.simulationEndDetails.put(entityEndSimulationDetails.getKey(), new EntitySimulationEndDetailsDto(entityEndSimulationDetails.getValue()));
        }
    }

    public String getSimulationId() {
        return simulationId;
    }

    public Map<String, EntitySimulationEndDetailsDto> getSimulationEndDetails() {
        return simulationEndDetails;
    }

    public String getUserName() {
        return userName;
    }

    public String getRequestId() {
        return requestId;
    }
}
