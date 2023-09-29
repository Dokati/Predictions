package Body.ThirdScreen;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class EntityPopulation {
    String entityName;
    IntegerProperty population;


    public EntityPopulation(String entityName, IntegerProperty population2) {
        this.entityName = entityName;
        this.population = new SimpleIntegerProperty(0);
        this.population.bind(population2);
    }

    public int getPopulation() {
        return population.get();
    }

    public void setPopulation(int population) {
        this.population.set(population);
    }

    public IntegerProperty populationProperty() {
        return population;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopualtion() {
        return population.get();
    }

    public IntegerProperty popualtionProperty() {
        return population;
    }
}
