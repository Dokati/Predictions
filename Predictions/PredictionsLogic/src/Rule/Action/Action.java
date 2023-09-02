package Rule.Action;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.SecondaryEntity;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
import Expression.Expression;
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
                    +" by a value of type ");
        };
    }

    public boolean IsSecondaryEntityExist()
    {
        return secondaryEntity != null;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public String getType() {
        return type;
    }

    public abstract ActionDetailsDto getDetails();

    public abstract EntityDefinition getMainEntity();

    public EntityInstance getEntityForAction(Context context) {
        if(context instanceof ContextSecondaryEntity &&
                ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().getEntityDef().equals(getMainEntity()))
        {
            return ((ContextSecondaryEntity) context).getSecondaryActiveEntityInstance();
        }

        return context.getActiveEntityInstance();
    }

    public String getSecondryEntityDetails(){
        return  secondaryEntity!= null? "\nSecondery entity: " + this.secondaryEntity.getEntityDefinition().getName():"\nNo Secondery entity";
    }

    @Override
    public String toString() {
        return "type: " + type;
    }
}
