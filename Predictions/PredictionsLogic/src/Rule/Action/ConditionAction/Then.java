package Rule.Action.ConditionAction;

import Context.Context;
import Entity.definition.EntityDefinition;
import PRD.PRDAction;
import PRD.PRDThen;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.Action;
import Rule.Action.ActionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Then{

    private List<Action> actionList;

    public Then(PRDThen prdThen, HashMap<String, EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        if (prdThen!=null) {
            this.actionList = new ArrayList<>();
            for (PRDAction action : prdThen.getPRDAction()) {
                actionList.add(ActionFactory.ActionCreator(action, entities, environmentProperties));
            }
        }
    }

    public void InvokeThen(Context context) {
        for (Action action : actionList) {
            action.Activate(context);
        }
    }

    public Integer getActionListSize() {
        return actionList.size();
    }
}
