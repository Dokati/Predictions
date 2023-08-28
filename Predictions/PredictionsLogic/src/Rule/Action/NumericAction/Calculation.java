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
    private EntityDefinition entity;

    public Calculation(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.resultProp = prdAction.getResultProp();
    }

    public void CheckIfTypeOfArgumentsMatchesTypeOfPropertyForCalculation(PropertyType arg1Type, PropertyType arg2Type,PropertyType propertyType) {
        if(!propertyType.equals(PropertyType.DECIMAL) && !propertyType.equals(PropertyType.FLOAT))
        {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("It is not possible to perform a Divide operation and place it into a property of type " + propertyType.name().toLowerCase());
        }
        if(!arg1Type.equals(PropertyType.DECIMAL) && !arg1Type.equals(PropertyType.FLOAT)) {

            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The variable arg1 is not of numeric value type");
        }
        if(!arg2Type.equals(PropertyType.DECIMAL) && !arg2Type.equals(PropertyType.FLOAT)) {

            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The variable arg2 is not of numeric value type");
        }
        if (propertyType.equals(PropertyType.DECIMAL) && (!arg1Type.equals(PropertyType.DECIMAL) || !arg2Type.equals(PropertyType.DECIMAL))) {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The property type is integer and one of the arguments type is float");
        }
    }

    public static boolean BothArgsAreNumeric(Object arg1, Object arg2) {
        return (arg2 instanceof Integer || arg2 instanceof Float) && (arg1 instanceof Integer || arg1 instanceof Float);
    }
}
