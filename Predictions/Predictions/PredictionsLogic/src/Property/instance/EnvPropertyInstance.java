package Property.instance;

import Property.definition.EnvPropertyDefinition;

public class EnvPropertyInstance {

    private final EnvPropertyDefinition propertyDef;
    private Object value;

    public EnvPropertyDefinition getPropertyDef() {
        return propertyDef;
    }

    public EnvPropertyInstance(EnvPropertyDefinition propertyDef) {
        this.propertyDef = propertyDef;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}