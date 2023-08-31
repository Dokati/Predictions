package Rule.Action;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import PRD.PRDAction;
import Property.definition.EnvPropertyDefinition;

import java.util.HashMap;

public class Kill extends Action{

    private EntityDefinition entity;

    public Kill(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.entity = entities.get(prdAction.getEntity());
    }

    @Override
    public void Activate(Context context) {
        if (context instanceof ContextSecondaryEntity &&  ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().equals(getEntityForAction(context))) {
            ((ContextSecondaryEntity)context).removeActiveEntity();
        }

        context.removeActiveEntity();
    }

    @Override
    public ActionDetailsDto getDetails() {
        String seconderyEntity = this.secondaryEntity!= null? "\nSecondery entity: " + this.secondaryEntity.getEntityDefinition().getName():"";
        return new ActionDetailsDto("Type: " + this.type
                +"\nEntity: " + entity.getName()+
                seconderyEntity);
    }

    @Override
    public EntityDefinition getMainEntity() {
        return entity;
    }

    @Override
    public EntityInstance getEntityForAction(Context context) {
        if(context instanceof ContextSecondaryEntity &&
                ((ContextSecondaryEntity)context).getSecondaryActiveEntityInstance().getEntityDef().equals(entity))
        {
            return ((ContextSecondaryEntity) context).getSecondaryActiveEntityInstance();
        }

        return context.getActiveEntityInstance();
    }
}
