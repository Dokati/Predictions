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
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = getEntityForAction(context).getProperties().get(propertyName);
        Object setVal = this.expressionValue.getTranslatedValue(new Context(getEntityForAction(context),context.getWorldInstance(),context.getEnvVariables(),context.getCurrentTick()));

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

    @Override
    public EntityInstance getEntityForAction(Context context) {
        if(context instanceof ContextSecondaryEntity &&
                ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().getEntityDef().equals(entity))
        {
            return ((ContextSecondaryEntity) context).getSecondaryActiveEntityInstance();
        }

        return context.getActiveEntityInstance();
    }
}
