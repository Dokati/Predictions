package Context;

import Entity.instance.EntityInstance;
import Expression.Expression;
import Property.PropertyType;
import Property.instance.EnvPropertyInstance;
import World.definition.WorldDefinition;
import World.instance.WorldInstance;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Context {
    protected EntityInstance activeEntityInstance;
    protected WorldInstance worldInstance;
    protected Integer currentTick;

    public Context(EntityInstance activeEntityInstance,WorldInstance worldInstance,Integer currentTick) {
        this.activeEntityInstance = activeEntityInstance;
        this.worldInstance = worldInstance;
        this.currentTick = currentTick;
    }

    public EntityInstance getActiveEntityInstance(){
        return this.activeEntityInstance;
    }

    public WorldInstance getWorldInstance() {
        return worldInstance;
    }

    public Integer getCurrentTick() {
        return currentTick;
    }

    public Map<String, EnvPropertyInstance> getEnvVariables() {
        return worldInstance.getEnvironmentProperties();
    }

    public Object environment(String envPropertyName) {
        try {
            return this.worldInstance.getEnvironmentProperties().get(envPropertyName).getValue();
        } catch (Exception e) {
            System.out.println("Invalid Input");
            throw new RuntimeException(e);
        }
    }

    public Float random(Integer maxRange) {
        if (maxRange < 0) {
            throw new IllegalArgumentException("Argument must be a non-negative integer.");
        }
        int min = 0; // Minimum value of the range (inclusive)
        Random random = new Random();
        int res = random.nextInt(maxRange - min + 1) + min;

        return (float) res;
    }

    public Object evaluate(String EntityPropertyFormat){
        String[] parts = EntityPropertyFormat.split("\\.");

        String entityName = parts[0];
        String propertyName = parts[1];

        if(!activeEntityInstance.getEntityDef().getName().equals(entityName)) {
            throw new IllegalArgumentException("The evaluate operation cannot be performed on an entity of type " + entityName + " because this entity does not exist exist in the context");
        }

        if(!activeEntityInstance.getProperties().containsKey(propertyName)) {
            throw new IllegalArgumentException("The evaluate operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
        }

        return activeEntityInstance.getProperties().get(propertyName).getValue();
    }

    public Float percent(Expression arg1,Expression arg2){
        Float theWholePart = PropertyType.FLOAT.convert(arg1.getTranslatedValue(this));
        Float percent = PropertyType.FLOAT.convert(arg2.getTranslatedValue(this));
        return (percent / 100) * theWholePart;
    }

    public Float ticks(String EntityPropertyFormat){
        String[] parts = EntityPropertyFormat.split("\\.");

        String entityName = parts[0];
        String propertyName = parts[1];

        if(!activeEntityInstance.getEntityDef().getName().equals(entityName)) {
            throw new IllegalArgumentException("The evaluate operation cannot be performed on an entity of type " + entityName + " because this entity does not exist exist in the context");
        }

        if(!activeEntityInstance.getProperties().containsKey(propertyName)) {
            throw new IllegalArgumentException("The evaluate operation cannot be performed because the property " + propertyName +" does not exist in the context of the requested entity");
        }

        return currentTick.floatValue() - activeEntityInstance.getProperties().get(propertyName).getChangeTick().floatValue();
    }
}
