package Rule.Action.Condition;

import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
import PRD.PRDCondition;
import Context.*;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import java.util.HashMap;


public class SingleCondition implements Condition{

    private final Expression expressionValue;
    private final String operator;
    private final String singularity;
    private final Expression property;
    private final EntityDefinition entity;

    public SingleCondition(PRDCondition prdCondition, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        this.expressionValue = new Expression(prdCondition.getValue());
        this.operator = prdCondition.getOperator();
        this.singularity = prdCondition.getSingularity();
        this.property = new Expression(prdCondition.getProperty());
        this.entity = entities.get(prdCondition.getEntity());
        CheckCondition(entities,environmentProperties);
    }

    private void CheckCondition(HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties){

        if(!operator.equals("bt") && !operator.equals("lt") && !operator.equals("!=") && !operator.equals("="))
        {
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The XML file contains " +  operator + " operator. The system does not support this operator");
        }

        if ((!expressionValue.GetTranslatedValueType(entity,entities,environmentProperties).equals(PropertyType.FLOAT)
            || !property.GetTranslatedValueType(entity,entities,environmentProperties).equals(PropertyType.FLOAT))
            && (operator.equals("bt") || operator.equals("lt"))){
            throw new IllegalArgumentException("The "+ operator +" operation cannot be performed on non-numeric values");
        }

        if(!expressionValue.GetTranslatedValueType(entity,entities,environmentProperties).equals(property.GetTranslatedValueType(entity,entities,environmentProperties))){
            throw new IllegalArgumentException("The "+ operator +" operation cannot be performed on values of different types");
        }
    }

    @Override
    public Boolean conditionIsTrue(Context context) {
        Object cmpToValue =  this.expressionValue.getTranslatedValue(context);
        Object propertyValue = this.property.getTranslatedValue(getEntityForAction(context));

        if ((!(cmpToValue instanceof Float) || !(propertyValue instanceof Float))
        && (operator.equals("bt") || operator.equals("lt"))) {
            throw new IllegalArgumentException("The "+ operator +" operation cannot be performed on non-numeric values");
        }

        if(!cmpToValue.getClass().getSimpleName().equals(propertyValue.getClass().getSimpleName())){
            throw new IllegalArgumentException("The "+ operator +" operation cannot be performed on values of different types");
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

    public Context getEntityForAction(Context context) {

        if(context instanceof ContextSecondaryEntity && ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().getEntityDef().equals(this.entity)) {
            return new ContextSecondaryEntity(((ContextSecondaryEntity) context).getSecondaryActiveEntityInstance(),context.getActiveEntityInstance(), context.getWorldInstance(), context.getCurrentTick());
        }

        return context;
    }

    public EntityDefinition getEntity() {
        return entity;
    }

    @Override
    public String getDetails() {
        return "Singularity: " + singularity+
                "\nProperty name: " + property.getExpression()+
                "\nOperator: " + operator+
                "\nValue: " + expressionValue.getExpression();
    }

}
