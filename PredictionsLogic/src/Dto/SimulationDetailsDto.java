package Dto;

import java.util.Map;

public class SimulationDetailsDto {

    Map<String,String> entitiesDetails;

    public SimulationDetailsDto(Map<String, String> entitiesDetails) {
        this.entitiesDetails = entitiesDetails;
    }

    public Map<String, String> getEntitiesDetails() {
        return entitiesDetails;
    }
}
