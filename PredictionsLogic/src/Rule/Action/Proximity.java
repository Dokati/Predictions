package Rule.Action;

import Context.*;
import Dto.ActionDetailsDto;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import Exceptions.IllegalXmlDataArgOfNumericActionAreNotNumericExceptions;
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
        this.of = new Expression(prdAction.getPRDEnvDepth().getOf());
        this.sourceEntity = entities.get(prdAction.getPRDBetween().getSourceEntity());
        this.targetEntity = entities.get(prdAction.getPRDBetween().getTargetEntity());
        CheckTypeOf(of.GetTranslatedValueType(sourceEntity,entities,environmentProperties));
        this.actions = new ArrayList<>();
        for (PRDAction action : prdAction.getPRDActions().getPRDAction()) {
            this.actions.add(ActionFactory.ActionCreator(action, entities, environmentProperties));
        }
    }

    public void CheckTypeOf(PropertyType ofType)
    {
        if (!ofType.equals(PropertyType.FLOAT)){
            throw new IllegalXmlDataArgOfNumericActionAreNotNumericExceptions("The proximity operation cannot be performed when the \"of\" argument is of type: " + ofType.name().toLowerCase());
        };
    }

    @Override
    public void Activate(Context context) {
        Integer sourceEntityX = getEntityForAction(context).getCoordinate().getX();
        Integer sourceEntityY = getEntityForAction(context).getCoordinate().getY();
        Integer ofValue = PropertyType.DECIMAL.convert(of.getTranslatedValue(context));
        Integer cols = context.getWorldInstance().getGridCols();
        Integer rows = context.getWorldInstance().getGridRows();

        int startX = (sourceEntityX - Math.min(ofValue,rows) + rows) % rows;
        int startY = (sourceEntityY - Math.min(ofValue,cols) + cols) % cols;
        int searchSize = ofValue * 2 + 1;

        for (int i = 0; i < searchSize; i++) {
            for (int j = 0; j < searchSize; j++) {
                Integer currentX = (startX + i) % rows;
                Integer currentY = (startY + j) % cols;
                if(context.getWorldInstance().getEntitYByPoint(currentX,currentY) != null &&
                   context.getWorldInstance().getEntitYByPoint(currentX,currentY).getEntityDef().equals(targetEntity))
                {
                    Context newContext = new ContextSecondaryEntity(context.getActiveEntityInstance(), context.getWorldInstance().getEntitYByPoint(currentX,currentY), context.getWorldInstance(), context.getCurrentTick());
                    for (Action action: this.actions)
                            action.Activate(newContext);
                    }
                    return;
                }
            }
        }

    public EntityDefinition getSourceEntity() {
        return sourceEntity;
    }

    @Override
    public ActionDetailsDto getDetails() {
        return new ActionDetailsDto("Type: " + this.type
                +"\nSource entity: " + sourceEntity.getName() +
                "\nTarget entity: " + targetEntity.getName()+
                "\nEnv depth: " + of.getExpression()+
                "\nNumber of actions: " + this.actions.size()
                +getSecondryEntityDetails()) ;
    }

    @Override
    public EntityDefinition getMainEntity() {
        return sourceEntity;
    }

}