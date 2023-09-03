package Body.ThirdScreen;

import javafx.beans.property.IntegerProperty;

public class EntityPopulation {
    String entityName;
    IntegerProperty popualtion;

    public EntityPopulation(String entityName, IntegerProperty popualtion) {
        this.entityName = entityName;
        this.popualtion.bind(popualtion);
    }

    public String getEntityName() {
        return entityName;
    }

    public int getPopualtion() {
        return popualtion.get();
    }

    public IntegerProperty popualtionProperty() {
        return popualtion;
    }
}
