package Rule.Action.NumericAction;

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
import Rule.Action.Action;

import java.util.HashMap;

public class Increase extends Action {

    private final String propertyName;
    private Expression by;
    private EntityDefinition entity;

    public Increase(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.propertyName = prdAction.getProperty();
        this.by = new Expression(prdAction.getBy());
        CheckTypeOfBy(by.GetTranslatedValueType(entity,entities,environmentProperties));
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = getEntityForAction(context).getProperties().get(propertyName);
        Float val = PropertyType.FLOAT.convert(prop.getValue());
        Float increaseByValue = PropertyType.FLOAT.convert(by.getTranslatedValue(context));
        Float res = val + increaseByValue;
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

}
