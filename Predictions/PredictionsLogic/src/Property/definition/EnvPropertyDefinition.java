package Property.definition;

import PRD.PRDEnvProperty;
import Property.PropertyType;
import Property.Range;

public class EnvPropertyDefinition {
    private String name;
    private Range range;
    private PropertyType type;

    public EnvPropertyDefinition(PRDEnvProperty envPropertyDef) {
        if(envPropertyDef.getPRDRange() != null) {
            range = new Range(envPropertyDef.getPRDRange());
        }

        try {
            this.type = PropertyType.valueOf(envPropertyDef.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid property type. A property cannot be of type " + envPropertyDef.getType());
        }

        this.name = envPropertyDef.getPRDName();
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range value) {
        this.range = value;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType value) {
        this.type = value;
    }

    public boolean hasRange(){
        return this.range != null;
    }

    @Override
    public String toString() {
        return "EnvPropertyDefinition: " +
                "name: '" + name + '\'' +
                ", range: " + range +
                ", type: '" + type + '\'' +
                "\n";
    }
}

