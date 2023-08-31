package Context;

import Entity.instance.EntityInstance;
import Expression.Expression;
import Property.PropertyType;
import Property.instance.EnvPropertyInstance;
import World.instance.WorldInstance;

import java.util.Map;
import java.util.Random;

public class ContextSecondaryEntity extends Context {

    private EntityInstance secondaryActiveEntityInstance;

    public ContextSecondaryEntity(Context context,EntityInstance secondaryActiveEntityInstance) {
        super(context.getActiveEntityInstance(), context.getWorldInstance(), context.getEnvVariables(), context.getCurrentTick());
        this.secondaryActiveEntityInstance = secondaryActiveEntityInstance;
    }

    public ContextSecondaryEntity(EntityInstance activeEntityInstance,EntityInstance secondaryActiveEntityInstance, WorldInstance worldInstance, Map<String, EnvPropertyInstance> envVariables, Integer currentTick) {
        super(activeEntityInstance, worldInstance, envVariables, currentTick);
        this.secondaryActiveEntityInstance = secondaryActiveEntityInstance;
    }

    @Override
    public void removeActiveEntity() {
        secondaryActiveEntityInstance.setAlive(false);
        secondaryActiveEntityInstance = null;
    }

    public EntityInstance getSecondaryActiveEntityInstance() {
        return secondaryActiveEntityInstance;
    }

    @Override
    public Object evaluate(String EntityPropertyFormat){
        String[] parts = EntityPropertyFormat.split("\\.");

        String entityName = parts[0];
        String propertyName = parts[1];

        if(activeEntityInstance.getEntityDef().getName().equals(entityName)) {
            if(!activeEntityInstance.getProperties().containsKey(propertyName)){
                throw new IllegalArgumentException("The evaluate operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
            }
            return activeEntityInstance.getProperties().get(propertyName).getValue();
        }

        if(secondaryActiveEntityInstance.getEntityDef().getName().equals(entityName)){
            if(!secondaryActiveEntityInstance.getProperties().containsKey(propertyName)){
                throw new IllegalArgumentException("The evaluate operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
            }
            return secondaryActiveEntityInstance.getProperties().get(propertyName).getValue();
        }

        throw new IllegalArgumentException("The evaluate operation cannot be performed on an entity of type " + entityName + " because this entity does not exist exist in the context");
    }


    @Override
    public Float ticks(String EntityPropertyFormat){
        String[] parts = EntityPropertyFormat.split("\\.");

        String entityName = parts[0];
        String propertyName = parts[1];

        if(activeEntityInstance.getEntityDef().getName().equals(entityName)) {
            if(!activeEntityInstance.getProperties().containsKey(propertyName)){
                throw new IllegalArgumentException("The ticks operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
            }
            return currentTick.floatValue() - activeEntityInstance.getProperties().get(propertyName).getChangeTick().floatValue();
        }

        if(secondaryActiveEntityInstance.getEntityDef().getName().equals(entityName)){
            if(!secondaryActiveEntityInstance.getProperties().containsKey(propertyName)){
                throw new IllegalArgumentException("The ticks operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
            }
            return currentTick.floatValue() - secondaryActiveEntityInstance.getProperties().get(propertyName).getChangeTick().floatValue();
        }

        throw new IllegalArgumentException("The ticks operation cannot be performed on an entity of type " + entityName + " because this entity does not exist exist in the context");
    }
}
