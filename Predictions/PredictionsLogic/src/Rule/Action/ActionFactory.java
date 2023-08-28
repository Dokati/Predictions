package Rule.Action;

import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataInvalidActionEntityExceptions;
import PRD.PRDAction;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.Condition.MultipleCondition;
import Rule.Action.Condition.SingleCondition;
import Rule.Action.ConditionAction.ConditionAction;
import Rule.Action.ConditionAction.Else;
import Rule.Action.ConditionAction.Then;
import Rule.Action.NumericAction.Decrease;
import Rule.Action.NumericAction.Divide;
import Rule.Action.NumericAction.Increase;
import Rule.Action.NumericAction.Multiply;
import Validation.Validation;

import java.util.HashMap;

import static Validation.Validation.CheckPRDConditionEntity;


public class ActionFactory {

    public static Action ActionCreator(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        Validation.CheckPRDActionDef(prdAction, entities);
        Action result = null;
        String actionType = prdAction.getType();
        switch (actionType) {
            case "calculation":
                if (prdAction.getPRDDivide() != null) {
                    result = new Divide(prdAction, entities, environmentProperties);
                }

                if (prdAction.getPRDMultiply() != null) {
                    result = new Multiply(prdAction, entities, environmentProperties);
                }
                break;

            case "condition":
                if (prdAction.getPRDCondition().getSingularity().equals("multiple")) {
                    result = new ConditionAction(prdAction , new Then(prdAction.getPRDThen(), entities,environmentProperties), new Else(prdAction.getPRDElse(), entities, environmentProperties),
                            new MultipleCondition(prdAction.getPRDCondition(), entities, environmentProperties), entities, environmentProperties);
                } else if (prdAction.getPRDCondition().getSingularity().equals("single")) {
                    result = new ConditionAction(prdAction, new Then(prdAction.getPRDThen(), entities, environmentProperties), new Else(prdAction.getPRDElse(), entities, environmentProperties),
                            new SingleCondition(prdAction.getPRDCondition(), entities, environmentProperties), entities, environmentProperties);
                }
                else
                {
                    throw new IllegalArgumentException("The Singularity is invalid. The system does not support Singularity of the type: " + prdAction.getPRDCondition().getSingularity());
                }
                break;

            case "decrease":
                result = new Decrease(prdAction, entities, environmentProperties);
                break;
            case "increase":
                result = new Increase(prdAction, entities, environmentProperties);
                break;
            case "kill":
                result = new Kill(prdAction, entities, environmentProperties);
                break;
            case "set":
                result = new Set(prdAction, entities, environmentProperties);
                break;
            case "replace":
                result = new Replace(prdAction, entities, environmentProperties);
                break;
            case "proximity":
                result = new Proximity(prdAction, entities, environmentProperties);
                break;
            default:
                break;
        }
        return result;
    }

}
