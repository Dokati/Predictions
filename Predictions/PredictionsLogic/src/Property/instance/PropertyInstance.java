package Property.instance;

import Property.PropertyType;
import Property.definition.NumericPropertyDefinition;
import Property.definition.PropertyDefinition;

public class PropertyInstance {
    private final PropertyDefinition propertyDef;
    private Object value;
    private Integer changeTick = 0;

    public PropertyInstance(PropertyDefinition propertyDef) {
        this.propertyDef = propertyDef;

       if(propertyDef.getRandomInitialize()) {
           if(propertyDef instanceof NumericPropertyDefinition && ((NumericPropertyDefinition) propertyDef).hasRange()) {
               this.value = propertyDef.getType().randomInitialization(((NumericPropertyDefinition) propertyDef).getRange());
           }
           else {
               this.value = propertyDef.getType().randomInitialization(null);
           }
       }
       else {
           this.value = this.propertyDef.getInitValue();
       }

       changeTick = 0;
    }

    public PropertyInstance(PropertyDefinition propertyDef,Integer tick) {
        this.propertyDef = propertyDef;

        if(propertyDef.getRandomInitialize()) {
            if(propertyDef instanceof NumericPropertyDefinition && ((NumericPropertyDefinition) propertyDef).hasRange()) {
                this.value = propertyDef.getType().randomInitialization(((NumericPropertyDefinition) propertyDef).getRange());
            }
            else {
                this.value = propertyDef.getType().randomInitialization(null);
            }
        }
        else {
            this.value = this.propertyDef.getInitValue();
        }

        changeTick = tick;
    }

    public Integer getChangeTick() {
        return changeTick;
    }

    public Object getName() {
        return propertyDef.getName();
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object val,Integer tick) {
        if(propertyDef instanceof NumericPropertyDefinition)
        {
            Float numericVal = PropertyType.FLOAT.convert(val);

            if(((NumericPropertyDefinition) propertyDef).hasRange()){
                if(((NumericPropertyDefinition) propertyDef).getRange().getFrom() > numericVal)
                {
                    this.value = propertyDef.getType().convert(((NumericPropertyDefinition) propertyDef).getRange().getFrom());
                }
                else if(((NumericPropertyDefinition) propertyDef).getRange().getTo() < numericVal)
                {
                    this.value = propertyDef.getType().convert(((NumericPropertyDefinition) propertyDef).getRange().getTo());
                }
                else
                {
                    this.value = val;
                }
            }
            else {
                this.value = val;
            }
        }
        else{
            this.value = val;
        }

        this.changeTick = tick;
    }

    public PropertyType getType() {
        return propertyDef.getType();
    }
}
