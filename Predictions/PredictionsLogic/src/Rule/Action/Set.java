package Rule.Action;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import Expression.Expression;
import PRD.PRDAction;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import Property.instance.PropertyInstance;
import Rule.Action.NumericAction.Calculation;

import java.util.HashMap;

public class Set extends Action{

    private final String propertyName;
    private final Expression expressionValue;
    private EntityDefinition entity;

    public Set(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity =  entities.get(prdAction.getEntity());
        this.propertyName = prdAction.getProperty();
        this.expressionValue = new Expression(prdAction.getValue());
        if(!expressionValue.GetTranslatedValueType(entity,environmentProperties).equals(entity.getProperties().get(propertyName).getType())
        && !CheckIfValueIsNumeric(entity.getProperties().get(propertyName).getType()))
        {
            throw new IllegalArgumentException("The set operation cannot be performed on a property of type: " + entity.getProperties().get(propertyName).getType().name().toLowerCase()+
                    " by value of type: " + expressionValue.GetTranslatedValueType(entity,environmentProperties).name().toLowerCase());
        }
        else if(entity.getProperties().get(propertyName).getType().equals(PropertyType.DECIMAL) && !expressionValue.GetTranslatedValueType(entity,environmentProperties).equals(PropertyType.DECIMAL))
        {
            throw new IllegalArgumentException("The set operation cannot be performed on a property of type: " + entity.getProperties().get(propertyName).getType().name().toLowerCase()+
                    " by value of type: " + expressionValue.GetTranslatedValueType(entity,environmentProperties).name().toLowerCase());
        }
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = context.getActiveEntityInstance().getProperties().get(propertyName);
        Object setVal = this.expressionValue.getTranslatedValue(context);

        if (setVal.getClass() != prop.getValue().getClass()){
            throw new Exceptions.NotSameTypeException();
        }

        prop.setValue(setVal, context.getCurrentTick());
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type+
                "\nEntity" + entity.getName()
                +"\nProperty name: " + propertyName +
                "\nValue: " + expressionValue.getExpression());
    }
}
