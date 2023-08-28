package Entity;

import Entity.definition.EntityDefinition;
import PRD.PRDAction;
import PRD.PRDCondition;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.Condition.Condition;
import Rule.Action.Condition.ConditionFactory;

import java.util.HashMap;

public class SecondaryEntity {
    EntityDefinition entityDefinition;
    Integer count;
    Condition condition;

    public SecondaryEntity(PRDAction.PRDSecondaryEntity secondaryEntity, HashMap<String, EnvPropertyDefinition> environmentProperties, HashMap<String, EntityDefinition> entities) {
        this.entityDefinition = entities.get(secondaryEntity.getEntity());

        this.condition = ConditionFactory.ConditionCreator(secondaryEntity.getPRDSelection().getPRDCondition(),entities,environmentProperties);

        if(secondaryEntity.getPRDSelection().getCount().equals("all")) {
            this.count = entityDefinition.getPopulation();
        }
        else{
            this.count = Integer.getInteger(secondaryEntity.getPRDSelection().getCount());
        }
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public void setEntityDefinition(EntityDefinition entityDefinition) {
        this.entityDefinition = entityDefinition;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
