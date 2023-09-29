package Validation;

import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataInvalidActionEntityExceptions;
import Exceptions.IllegalXmlDataInvalidActionPropExceptions;
import PRD.PRDAction;
import PRD.PRDCondition;

import java.util.ArrayList;
import java.util.HashMap;

public class Validation {
    public static void CheckPRDActionDef(PRDAction actionDef, HashMap<String, EntityDefinition> entities) {

        //Check If Entity Already Exists - all action types
        CheckIfEntityAlreadyExists(actionDef, entities);

        //Check If Property Exist In Entity
        switch (actionDef.getType()) {
            case "calculation":
                if (!entities.get(actionDef.getEntity()).getProperties().containsKey(actionDef.getResultProp())) {
                    throw new IllegalXmlDataInvalidActionPropExceptions("Xml contain Action that its property: " +
                            actionDef.getResultProp() + " unexisted in related entity");
                }
                break;
            case "increase":
            case "decrease":
            case "set":
                if (!entities.get(actionDef.getEntity()).getProperties().containsKey(actionDef.getProperty())) {
                    throw new IllegalXmlDataInvalidActionPropExceptions("Xml contain Action that its property: " +
                            actionDef.getProperty() + " unexisted in related entity");
                }
                break;
            case "condition":
            case "proximity":
            case "replace":
            case "kill":
                break;
            default:
                throw new IllegalXmlDataInvalidActionPropExceptions("The XML file contains an action of the "+ actionDef.getType() +" type." +
                        " This action is not supported by the system");
        }
    }

    public static void CheckPRDConditionEntity(String EntityName, HashMap<String, EntityDefinition> entities)
    {
        if(!entities.containsKey(EntityName))
        {
            throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Condition that related to unexisted entity: " + EntityName);
        }
    }

    public static void CheckIfEntityAlreadyExists(PRDAction actionDef, HashMap<String, EntityDefinition> entities) {

        if (actionDef.getType().equals("replace")) {
            if (!entities.containsKey(actionDef.getKill())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getKill());
            }
            if (!entities.containsKey(actionDef.getCreate())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getCreate());
            }
        } else if (actionDef.getType().equals("proximity")) {
            if (!entities.containsKey(actionDef.getPRDBetween().getSourceEntity())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getPRDBetween().getSourceEntity());
            }
            if (!entities.containsKey(actionDef.getPRDBetween().getTargetEntity())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getPRDBetween().getTargetEntity());
            }
        } else if (actionDef.getType().equals("condition")) {
            if (!entities.containsKey(actionDef.getEntity())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getEntity());
            }
             if (actionDef.getPRDCondition().getSingularity().equals("single") && !entities.containsKey(actionDef.getPRDCondition().getEntity())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getPRDCondition().getEntity());
            }
        } else {
            if (!entities.containsKey(actionDef.getEntity())) {
                throw new IllegalXmlDataInvalidActionEntityExceptions("Xml contain Action that related to unexisted entity: " + actionDef.getEntity());

            }
        }
    }

}
