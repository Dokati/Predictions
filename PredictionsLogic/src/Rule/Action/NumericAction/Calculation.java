package Rule.Action.NumericAction;

import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
import PRD.PRDAction;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import Property.definition.NumericPropertyDefinition;
import Property.instance.PropertyInstance;
import Rule.Action.Action;

import java.util.HashMap;

public abstract class Calculation extends Action {
    protected String resultProp;

    protected EntityDefinition entity;

    public Calculation(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.resultProp = prdAction.getResultProp();
        CheckIfResultPropertyNumeric();
    }

    public void CheckIfResultPropertyNumeric()
    {
        if(!entity.getProperties().get(resultProp).getType().equals(PropertyType.FLOAT)){
            throw new IllegalArgumentException("The result property: "+ resultProp +" of the entity: " + entity.getName()+ " is not a numeric type");
        }
    }

    public void CheckIfTypeOfArgumentsMatchesForNumericAction(PropertyType arg1Type, PropertyType arg2Type) {
        if (!arg1Type.equals(PropertyType.FLOAT)) {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("It is not possible to perform the " + this.getClass().getSimpleName() +
                    " operation because The variable arg1 is not of numeric value type");
        }
        if (!arg2Type.equals(PropertyType.FLOAT)) {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("It is not possible to perform the " + this.getClass().getSimpleName() +
                    " operation because The variable arg2 is not of numeric value type");
        }
    }
}
