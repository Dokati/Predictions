package Rule.Action.NumericAction;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
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

    public String getProperty(){return propertyName;}

    public String getBy(){return propertyName;}

    public Decrease(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.propertyName = prdAction.getProperty();
        this.by = new Expression(prdAction.getBy());
        if(CheckIfTypeOfByNotMatchesTypeOfProperty(this.by.GetTranslatedValueType(entity,environmentProperties),entity.getProperties().get(propertyName).getType()))
        {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("It is not possible to perform the Decrease " +
                    "operation on a property of type " + entity.getProperties().get(propertyName).getType().name().toLowerCase() + " by a value of type "
                    + this.by.GetTranslatedValueType(entity,environmentProperties).name().toLowerCase());
        }
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = context.getActiveEntityInstance().getProperties().get(propertyName);
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
                "\nby: " + by.getExpression());
    }

}
