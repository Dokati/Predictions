package Rule.Action;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import PRD.PRDAction;
import Property.definition.EnvPropertyDefinition;
import Property.definition.PropertyDefinition;
import Property.instance.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class Replace extends Action{

    private EntityDefinition createEntity;
    private EntityDefinition killEntity;
    private String mode;

    public Replace(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        this.createEntity = entities.get(prdAction.getCreate());
        this.killEntity = entities.get(prdAction.getKill());
        CheckReplaceMode(prdAction.getMode());
        this.mode = prdAction.getMode();
    }

    void CheckReplaceMode(String mode)
    {
        if(!mode.equals("scratch") && !mode.equals("derived")) {
            throw new IllegalArgumentException("The mode is invalid. The system does not support "+ mode +" mode");
        }
    }

    @Override
    public void Activate(Context context) {
        if(mode.equals("scratch")){
            context.getActiveEntityInstance().initializeEntityInstanceFromScratch(createEntity,context.getCurrentTick());
        }

        else if(mode.equals("derived")){
            context.getActiveEntityInstance().initializeEntityInstanceByDerived(createEntity,context.getCurrentTick());
        }
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type
                +"\nCreate entity: " + createEntity.getName() +
                "\nKill entity: " + killEntity.getName() +
                "\nMode: " + mode);
    }
}
