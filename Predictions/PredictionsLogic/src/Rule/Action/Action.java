package Rule.Action;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import PRD.PRDAction;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;

import java.util.HashMap;

public abstract class Action {

    protected String type;
    protected SecondaryEntity secondaryEntity;

    public Action(PRDAction prdAction, HashMap<String, EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        this.type = prdAction.getType();
        if(prdAction.getPRDSecondaryEntity() != null) {
            this.secondaryEntity = new SecondaryEntity(prdAction.getPRDSecondaryEntity(), environmentProperties, entities);
        }
    }
    abstract public void Activate(Context context);

    public static boolean CheckIfValueIsNumeric(PropertyType val)
    {
        return val.equals(PropertyType.DECIMAL) || val.equals(PropertyType.FLOAT);
    }

    public Boolean CheckIfTypeOfByNotMatchesTypeOfProperty(PropertyType byType, PropertyType propertyType)
    {
        return (!CheckIfValueIsNumeric(propertyType) || !CheckIfValueIsNumeric(byType))
                || (propertyType.equals(PropertyType.DECIMAL) && !byType.equals(PropertyType.DECIMAL));
    }

    public String getType() {
        return type;
    }
    public abstract ActionDetailsDto getDetails();
    @Override
    public String toString() {
        return "type: " + type;
    }
}
