package Entity.instance;

import Entity.definition.EntityDefinition;
import Coordinate.Coordinate;
import Property.definition.PropertyDefinition;
import Property.instance.PropertyInstance;

import java.util.HashMap;
import java.util.Map;

public class EntityInstance {
    private EntityDefinition entityDef;
    final private Integer id;
    private Map<String, PropertyInstance> properties;
    private Boolean isAlive;
    private Coordinate coordinate;

    public EntityInstance(Integer id, EntityDefinition entityDef, EntityInstance[][] grid) {
        this.entityDef = entityDef;
        this.id = id;
        this.isAlive = true;
        this.coordinate = new Coordinate(grid);
        properties = new HashMap<>();
        for (Map.Entry<String, PropertyDefinition> propertyDef : entityDef.getProperties().entrySet()) {
           properties.put(propertyDef.getKey(),new PropertyInstance(propertyDef.getValue()));
        }

        grid[coordinate.getX()][coordinate.getY()] = this;
    }

    public void initializeEntityInstanceFromScratch(EntityDefinition entityDef,Integer tick)
    {
        this.entityDef = entityDef;
        this.properties = new HashMap<>();
        for (Map.Entry<String, PropertyDefinition> propertyDef : entityDef.getProperties().entrySet()) {
            properties.put(propertyDef.getKey(),new PropertyInstance(propertyDef.getValue(),tick));
        }
    }

    public void initializeEntityInstanceByDerived(EntityDefinition entityDef,Integer tick)
    {
        this.entityDef = entityDef;
        Map<String, PropertyInstance> tempProperties = properties;
        this.properties = new HashMap<>();

        for (Map.Entry<String, PropertyDefinition> propertyDef : entityDef.getProperties().entrySet()) {
            if(tempProperties.containsKey(propertyDef.getKey()) && propertyDef.getValue().getType().equals(tempProperties.get(propertyDef.getKey()).getType())){
                properties.put(propertyDef.getKey(),tempProperties.get(propertyDef.getKey()));
            }
            else{
                properties.put(propertyDef.getKey(),new PropertyInstance(propertyDef.getValue(),tick));
            }
        }
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public Integer getId(){
        return  this.id;
    }

    public EntityDefinition getEntityDef() {
        return entityDef;
    }

    public Map<String, PropertyInstance> getProperties() {
        return this.properties;
    }

    public void killEntity(){
        this.isAlive = false;
    }

    public void setEntityDef(EntityDefinition entityDef) {
        this.entityDef = entityDef;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }
}
