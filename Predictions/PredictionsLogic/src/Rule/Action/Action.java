package Rule.Action;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
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

    public void CheckTypeOfBy(PropertyType byType)
    {
        if (!byType.equals(PropertyType.FLOAT)){
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("It is not possible to perform the " + this.getClass().getSimpleName()
                    +" by a value of type "  + byType.name().toLowerCase());
        };
    }

    public String getType() {
        return type;
    }

    public abstract ActionDetailsDto getDetails();

    public abstract EntityDefinition getMainEntity();
    @Override
    public String toString() {
        return "type: " + type;
    }
}
