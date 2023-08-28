package Rule.Action.Condition;

import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
import PRD.PRDCondition;
import Property.PropertyType;
import Context.Context;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.Action;
import Validation.Validation;

import java.util.HashMap;


public class SingleCondition implements Condition{

    private final Expression expressionValue;
    private final String operator;
    private final String singularity;
    private final String propertyName;
    private final EntityDefinition entity;

    public SingleCondition(PRDCondition prdCondition, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        this.expressionValue = new Expression(prdCondition.getValue());
        CheckOperator(prdCondition.getOperator());
        this.operator = prdCondition.getOperator();
        this.singularity = prdCondition.getSingularity();
        this.propertyName = prdCondition.getProperty();
        this.entity = entities.get(prdCondition.getEntity());
    }

    //need a fix!
    private void CheckIfTypeOfValueMatchesTypeOfProperty(PropertyType valueType, PropertyType propertyType)
    {
        if(Action.CheckIfValueIsNumeric(propertyType)) {
            if(!Action.CheckIfValueIsNumeric(valueType)){
                throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The property type is numeric and the expression value type is" + valueType.name().toLowerCase());
            }
        }
        else if(!propertyType.equals(valueType))
        {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The property type is "+ propertyType.name().toLowerCase() +" and the expression value type is" + valueType.name().toLowerCase());
        }
    }

    private void CheckOperator(String pRDoperator)
    {
        if(!pRDoperator.equals("bt") && !pRDoperator.equals("lt") && !pRDoperator.equals("!=") && !pRDoperator.equals("="))
        {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The XML file contains " +  pRDoperator + " operator. The system does not support this operator");
        }
    }

    @Override
    public Boolean conditionIsTrue(Context context) {
        EntityInstance activeEntity = context.getActiveEntityInstance();
        Object cmpToValue =  this.expressionValue.getTranslatedValue(context);
        Object propertyValue = activeEntity.getProperties().get(propertyName).getValue();

        if (activeEntity.getProperties().get(propertyName).getType().equals(PropertyType.FLOAT)
        || activeEntity.getProperties().get(propertyName).getType().equals(PropertyType.DECIMAL)) {
            cmpToValue = PropertyType.FLOAT.convert(cmpToValue);
            propertyValue = PropertyType.FLOAT.convert(propertyValue);
        }

        boolean result = false;

        switch (operator){
            case "=":
                result = propertyValue.equals(cmpToValue);
                break;
            case  "!=":
                result = (!propertyValue.equals(cmpToValue));
                break;
            case "bt":
                result = (Float)propertyValue > (Float)cmpToValue;
                break;
            case "lt":
                result = (Float)propertyValue < (Float)cmpToValue;
                break;

        }

        return result;
    }

    @Override
    public String getDetails() {
        return "Singularity: " + singularity+
                "\nEntity: "  + entity.getName()+
                "\nProperty name: " + propertyName+
                "\nOperator: " + operator+
                "\n Value: " + expressionValue.getExpression();
    }

}
