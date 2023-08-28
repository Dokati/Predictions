package Property.definition;

import PRD.PRDProperty;
import Property.PropertyType;
import java.lang.*;

public class PropertyDefinition {
    private PropertyType type;
    private String name;
    private final Boolean randomInitialize;
    private Object initValue;

    public PropertyDefinition(PRDProperty propertyDef) {
        this.name = propertyDef.getPRDName();
        this.randomInitialize = propertyDef.getPRDValue().isRandomInitialize();

        try {
            this.type = PropertyType.valueOf(propertyDef.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid property type. A property cannot be of type " + propertyDef.getType());
        }

        if(!propertyDef.getPRDValue().isRandomInitialize()) {
            this.initValue = this.type.convert(propertyDef.getPRDValue().getInit());
        }
    }

    public Boolean getRandomInitialize(){ return randomInitialize;}

    public PropertyType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getInitValue() {
        return initValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.initValue = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Property: ")
                .append("type: ").append(type)
                .append(", name: ").append(name)
                .append(", randomInitialize: ").append(randomInitialize);

        if (initValue != null) {
            sb.append(", value: ").append(initValue);
        }

        return sb.toString();
    }
}
