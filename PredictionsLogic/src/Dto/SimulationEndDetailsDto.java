package Dto;

import EndSimulationDetails.EntitySimulationEndDetails;

import java.util.HashMap;
import java.util.Map;

public class SimulationEndDetailsDto {
    private final String SimulationId;
    public Map<String,EntitySimulationEndDetailsDto> simulationEndDetails;

    public SimulationEndDetailsDto(Integer simulationId, HashMap<String, EntitySimulationEndDetails> simulationEndDetails) {
        this.SimulationId = simulationId.toString();

        this.simulationEndDetails = new HashMap<>();
        for(Map.Entry<String, EntitySimulationEndDetails> entityEndSimulationDetails : simulationEndDetails.entrySet()){
            this.simulationEndDetails.put(entityEndSimulationDetails.getKey(),new EntitySimulationEndDetailsDto(entityEndSimulationDetails.getValue()));
        }
    }
}
