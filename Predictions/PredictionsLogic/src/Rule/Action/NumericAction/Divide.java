package Rule.Action.NumericAction;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import Expression.Expression;
import PRD.PRDAction;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import Property.instance.PropertyInstance;
import java.util.HashMap;

public class Divide extends Calculation{

    private final Expression arg1;
    private final Expression arg2;

    public Divide(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.arg1 = new Expression(prdAction.getPRDDivide().getArg1());
        this.arg2 = new Expression(prdAction.getPRDDivide().getArg2());
        CheckIfTypeOfArgumentsMatchesForNumericAction(this.arg1.GetTranslatedValueType(entities.get(prdAction.getEntity()),environmentProperties),this.arg2.GetTranslatedValueType(entities.get(prdAction.getEntity()),environmentProperties));
    }

    @Override
    public void Activate(Context context) {
        PropertyInstance prop = context.getActiveEntityInstance().getProperties().get(resultProp);
        Float numericArg1 = PropertyType.FLOAT.convert(arg1.getTranslatedValue(context));
        Float numericArg2 = PropertyType.FLOAT.convert(arg2.getTranslatedValue(context));
        Float res = numericArg1/numericArg2;
        prop.setValue(prop.getType().convert(res),context.getCurrentTick());
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type
                +"\narg1: " + arg1.getExpression() +
                "\narg2: " + arg2.getExpression()+
                "Result-prop:" + resultProp);
    }


}
