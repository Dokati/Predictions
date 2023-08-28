package Rule.Action;

import Context.Context;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import Expression.Expression;
import PRD.PRDAction;
import PRD.PRDActions;
import Property.PropertyType;
import Property.definition.EnvPropertyDefinition;
import java.util.ArrayList;
import java.util.HashMap;

public class Proximity extends Action {

    private EntityDefinition sourceEntity;
    private EntityDefinition targetEntity;
    private Expression of;
    private ArrayList<Action> actions;

    public Proximity(PRDAction prdAction, HashMap<String,EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) {
        super(prdAction, entities, environmentProperties);
        //check if of float
        this.of = new Expression(prdAction.getPRDEnvDepth().getOf());
        this.sourceEntity = entities.get(prdAction.getPRDBetween().getSourceEntity());
        this.targetEntity = entities.get(prdAction.getPRDBetween().getTargetEntity());
        this.actions = new ArrayList<>();
        for (PRDAction action : prdAction.getPRDActions().getPRDAction()) {
            this.actions.add(ActionFactory.ActionCreator(action, entities, environmentProperties));
        }
    }

    @Override
    public void Activate(Context context) {
        Integer sourceEntityX = context.getActiveEntityInstance().getCoordinate().getX();
        Integer sourceEntityY = context.getActiveEntityInstance().getCoordinate().getY();
        Integer ofValue = PropertyType.DECIMAL.convert(of.getTranslatedValue(context));
        Integer cols = context.getWorldInstance().getGridCols();
        Integer rows = context.getWorldInstance().getGridRows();

        Integer startX = (sourceEntityX - ofValue) % rows;
        Integer startY = (sourceEntityY - ofValue) % cols;
        Integer searchSize = ofValue * 2 + 1;

        for (int i = 0; i < ofValue; i++) {
            for (int j = 0; j < ofValue; j++) {
                Integer currentX = (sourceEntityX + i) % rows;
                Integer currentY = (sourceEntityY + j) % cols;
                if(context.getWorldInstance().getGrid()[currentX][currentY].getEntityDef() == targetEntity)
                {
                    Context newContext = new Context(context.getWorldInstance().getGrid()[currentX][currentY], context.getWorldInstance(), context.getEnvVariables(), context.getCurrentTick());
                    for (Action action: this.actions)
                        //need to chaeck if entity is good
                        action.Activate(newContext);
                    }
                }
            }
        }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type
                +"\nSource entity: " + sourceEntity.getName() +
                "\nTarget entity: " + targetEntity.getName()+
                "\nOf: " + of.getExpression());
    }


}