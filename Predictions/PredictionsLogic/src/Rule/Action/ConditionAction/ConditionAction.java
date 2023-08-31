package Rule.Action.ConditionAction;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import PRD.PRDAction;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.Action;
import Rule.Action.Condition.Condition;
import Rule.Action.Condition.MultipleCondition;
import Rule.Action.Condition.SingleCondition;

import java.util.HashMap;

public class ConditionAction extends Action {

    private final Then then;
    private final Else elseAct;
    private final Condition condition;
    private EntityDefinition entity;

    public ConditionAction(PRDAction prdAction, Then then, Else elseAct, Condition condition, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
        this.then = then;
        this.elseAct = elseAct;
        this.condition = condition;
    }

    @Override
    public void Activate(Context context) {
        if(condition.conditionIsTrue(context)) {
            then.InvokeThen(context);
        }
        else{
            if (elseAct.getActionList() != null){
                elseAct.InvokeElse(context);
            }
        }
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type +
                "\nEntity: " + entity.getName()+ "\n"
                + condition.getDetails());
    }

    @Override
    public EntityDefinition getMainEntity() {
        return entity;
    }

    @Override
    public EntityInstance getEntityForAction(Context context) {
        return null;
    }
}
