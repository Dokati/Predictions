package Dto;

import EndSimulationDetails.EntitySimulationEndDetails;
import EndSimulationDetails.PropertySimulationEndDetails;

import java.util.HashMap;
import java.util.Map;

public class EntitySimulationEndDetailsDto {

    private Map<String, String> populationByTick;
    Map<String, PropertySimulationEndDetailsDto> endSimulationPropertiesDetails;

    public EntitySimulationEndDetailsDto(EntitySimulationEndDetails EntitySimulationEndDetails) {

        this.populationByTick = new HashMap<>();
        for(Map.Entry<Integer, Integer> populationByTick: EntitySimulationEndDetails.getPopulationByTick().entrySet()){
            this.populationByTick.put(populationByTick.getKey().toString(),populationByTick.getValue().toString());
        }

        this.endSimulationPropertiesDetails = new HashMap<>();
        for(Map.Entry<String, PropertySimulationEndDetails> endSimulationPropertiesDetails: EntitySimulationEndDetails.getEndSimulationPropertiesDetails().entrySet()){
            this.endSimulationPropertiesDetails.put(endSimulationPropertiesDetails.getKey(),new PropertySimulationEndDetailsDto(endSimulationPropertiesDetails.getValue()));
        }
    }
}
