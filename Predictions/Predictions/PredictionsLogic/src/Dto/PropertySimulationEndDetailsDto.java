package Dto;

import EndSimulationDetails.PropertySimulationEndDetails;

import java.util.HashMap;
import java.util.Map;

public class PropertySimulationEndDetailsDto {

    Map<String,String> propertyHistogram;
    String consistency;
    String average;

    public PropertySimulationEndDetailsDto(PropertySimulationEndDetails propertySimulationEndDetails) {

        this.propertyHistogram = new HashMap<>();
        for (Map.Entry<Object,Integer> propertyHistogram : propertySimulationEndDetails.getPropertyHistogram().entrySet()){
            this.propertyHistogram.put(propertyHistogram.getKey().toString(),propertyHistogram.getValue().toString());
        }

        this.consistency = propertySimulationEndDetails.getConsistency().toString();

        //if the property is not numeric type - average == null
        if(propertySimulationEndDetails.getAverage() != null){
            this.average = propertySimulationEndDetails.getAverage().toString();
        }

    }
}
