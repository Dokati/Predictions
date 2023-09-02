package Rule.Action.NumericAction;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
import PRD.PRDAction;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import Property.instance.PropertyInstance;
import Rule.Action.Action;
import java.util.HashMap;

public class Decrease extends Action {
    private final String propertyName;
    private final Expression by;
    private EntityDefinition entity;

    public Decrease(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.propertyName = prdAction.getProperty();
        this.by = new Expression(prdAction.getBy());
        CheckTypeOfBy(by.GetTranslatedValueType(entity,entities,environmentProperties));
    }

    public String getProperty(){return propertyName;}

    public String getBy(){return propertyName;}

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = getEntityForAction(context).getProperties().get(propertyName);
        Float val = PropertyType.FLOAT.convert(prop.getValue());
        Float decreaseByValue = PropertyType.FLOAT.convert(by.getTranslatedValue(context));
        Float res = val - decreaseByValue;
        prop.setValue(prop.getType().convert(res),context.getCurrentTick());
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type+
                "\nEntity: " + entity.getName()
                +"\nProperty name: " + propertyName +
                "\nby: " + by.getExpression()+
                getSecondryEntityDetails());
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
