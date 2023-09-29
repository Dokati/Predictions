package Rule.Action.Condition;

import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataInvalidActionEntityExceptions;
import PRD.PRDCondition;
import Property.definition.EnvPropertyDefinition;

import java.util.HashMap;

import static Validation.Validation.CheckPRDConditionEntity;

public class ConditionFactory {
    public static Condition ConditionCreator(PRDCondition prdCondition, HashMap<String, EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties){

        if (prdCondition.getSingularity().equals("multiple")){
            return new MultipleCondition(prdCondition, entities, environmentProperties);
        }
        else if (prdCondition.getSingularity().equals("single")) {
            CheckPRDConditionEntity(prdCondition.getEntity(),entities);
            return new SingleCondition(prdCondition, entities, environmentProperties);
        }
        else
        {
            throw new IllegalArgumentException("The Singularity is invalid. The system does not support Singularity of the type: " + prdCondition.getSingularity());
        }
    }
}
