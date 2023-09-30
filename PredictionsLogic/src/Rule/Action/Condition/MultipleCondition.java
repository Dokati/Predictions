package Rule.Action.Condition;
import Context.Context;
import Entity.definition.EntityDefinition;
import PRD.PRDAction;
import PRD.PRDCondition;
import Property.definition.EnvPropertyDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultipleCondition implements Condition {
    private final String logical;
    private final String singularity;
    private final List<Condition> conditions;

    public MultipleCondition(PRDCondition prdCondition, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        CheckLogical(prdCondition.getLogical());
        this.logical = prdCondition.getLogical();
        this.singularity = prdCondition.getSingularity();
        this.conditions = new ArrayList<>();

        for(PRDCondition cond : prdCondition.getPRDCondition()){
            this.conditions.add(ConditionFactory.ConditionCreator(cond,entities,environmentProperties));
        }
    }

    public void CheckLogical(String logical)
    {
        if(!logical.equals("or") && !logical.equals("and"))
        {
            throw new IllegalArgumentException("Invalid logical. The system does not support logical of the type: " + logical);
        }
    }

    @Override
    public Boolean conditionIsTrue(Context context) {

        boolean resultCanonizedConditionsOr = false;
        boolean resultCanonizedConditionsAnd = true;

        for(Condition cond: conditions){
            if(logical.equals("or")){
                resultCanonizedConditionsOr = resultCanonizedConditionsOr || cond.conditionIsTrue(context);
            }
            else if (logical.equals("and")) {
                resultCanonizedConditionsAnd = resultCanonizedConditionsAnd && cond.conditionIsTrue(context);
            }
        }

        if(logical.equals("or")){
            return resultCanonizedConditionsOr;
        }
        else{
            return resultCanonizedConditionsAnd;
        }
    }
    @Override
    public String getDetails() {
        return "Singularity: " + singularity+
                "\nLogical: " + logical+
                "\nNumber sub-conditions: " +this.conditions.size();
    }

}
