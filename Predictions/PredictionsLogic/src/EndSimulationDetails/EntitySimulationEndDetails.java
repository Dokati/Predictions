package EndSimulationDetails;

import Entity.definition.EntityDefinition;
import Property.definition.PropertyDefinition;

import java.util.HashMap;
import java.util.Map;

public class EntitySimulationEndDetails {
    private Map<Integer, Integer> populationByTick;
    Integer CurrentPopulation;
    Map<String, PropertySimulationEndDetails> endSimulationPropertiesDetails;

    public EntitySimulationEndDetails(EntityDefinition entityDefinition) {
        this.populationByTick = new HashMap<>();

        this.endSimulationPropertiesDetails = new HashMap<>();
        for (Map.Entry<String, PropertyDefinition> propertyDefinition: entityDefinition.getProperties().entrySet()){
            this.endSimulationPropertiesDetails.put(propertyDefinition.getKey(),new PropertySimulationEndDetails());
        }
    }

    public void InitEntitySimulationEndDetails() {
        Map<String, PropertySimulationEndDetails> newEndSimulationPropertiesDetails = new HashMap<>();

        for(Map.Entry<String, PropertySimulationEndDetails> propertySimulationEndDetailsEntry : endSimulationPropertiesDetails.entrySet()) {
            newEndSimulationPropertiesDetails.put(propertySimulationEndDetailsEntry.getKey(),new PropertySimulationEndDetails());
        }

        this.endSimulationPropertiesDetails = newEndSimulationPropertiesDetails;
    }

    public Map<Integer, Integer> getPopulationByTick() {
        return populationByTick;
    }

    public void setPopulationByTick(Map<Integer, Integer> populationByTick) {
        this.populationByTick = populationByTick;
    }

    public Map<String, PropertySimulationEndDetails> getEndSimulationPropertiesDetails() {
        return endSimulationPropertiesDetails;
    }

    public void setEndSimulationPropertiesDetails(Map<String, PropertySimulationEndDetails> endSimulationPropertiesDetails) {
        this.endSimulationPropertiesDetails = endSimulationPropertiesDetails;
    }

    public void setCurrentPopulation(Integer currentPopulation) {
        CurrentPopulation = currentPopulation;
    }

    public Integer getCurrentPopulation() {
        return CurrentPopulation;
    }
}
