package Rule.Action.Condition;

import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
import PRD.PRDCondition;
import Context.*;
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
        CheckOperator(prdCondition.getOperator());
        this.operator = prdCondition.getOperator();
        this.singularity = prdCondition.getSingularity();
        this.property = new Expression(prdCondition.getProperty());
        this.entity = entities.get(prdCondition.getEntity());
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
        Object cmpToValue =  this.expressionValue.getTranslatedValue(context);
        Object propertyValue = this.property.getTranslatedValue(getEntityForAction(context));

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

        if(context instanceof ContextSecondaryEntity &&
                ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().getEntityDef().equals(this.entity)) {
            return new ContextSecondaryEntity(((ContextSecondaryEntity) context).getSecondaryActiveEntityInstance(),context.getActiveEntityInstance(),
            context.getWorldInstance(),context.getEnvVariables(),context.getCurrentTick());
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
