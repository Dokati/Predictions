package Rule.Action.Condition;

import Context.Context;

public interface Condition {
    Boolean conditionIsTrue(Context context);

    String getDetails();
}
