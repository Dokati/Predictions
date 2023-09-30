package Rule.Action;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
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

        if(!entity.getProperties().get(propertyName).getType().equals(expressionValue.GetTranslatedValueType(entity,entities,environmentProperties))){
            throw new IllegalArgumentException("The set operation cannot be performed with values of different types");
        }
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = getEntityForAction(context).getProperties().get(propertyName);
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
                "\nValue: " + expressionValue.getExpression()
                + getSecondryEntityDetails());
    }

    @Override
    public EntityDefinition getMainEntity() {
        return entity;
    }

}
