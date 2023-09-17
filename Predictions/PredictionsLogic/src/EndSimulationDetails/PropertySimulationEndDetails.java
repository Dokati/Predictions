package EndSimulationDetails;

import java.util.Hashtable;
import java.util.Map;

public class PropertySimulationEndDetails {
    Map<Object,Integer> propertyHistogram;
    Float consistency;
    Float average;
    Integer numOfEntitiesForAverageCalc;

    public PropertySimulationEndDetails() {
        this.propertyHistogram = new Hashtable<>();
        this.consistency = 0.f;
        this.numOfEntitiesForAverageCalc = 0;
        this.average = null;
    }

    public Map<Object, Integer> getPropertyHistogram() {
        return propertyHistogram;
    }

    public void setPropertyHistogram(Map<Object, Integer> propertyHistogram) {
        this.propertyHistogram = propertyHistogram;
    }

    public Float getConsistency() {
        return consistency;
    }

    public void addEntitiesForAverageCalc(){
        numOfEntitiesForAverageCalc++;
    }

    public void addToConsistency(Float consistency) {

        if(numOfEntitiesForAverageCalc.equals(1)){
            this.consistency = consistency;
        }

        else{
            Float temp = this.consistency * (numOfEntitiesForAverageCalc - 1);
            this.consistency = (temp + consistency)/numOfEntitiesForAverageCalc;
        }

    }

    public void addToAverage(Float average) {

        if(numOfEntitiesForAverageCalc.equals(1)){
            this.average = average;
        }
        else{
            Float temp = this.average * (numOfEntitiesForAverageCalc - 1);
            this.average = (temp + average)/numOfEntitiesForAverageCalc;
        }
    }

    public void setConsistency(Float consistency) {
        this.consistency = consistency;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }
}
