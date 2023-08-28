package Entity.definition;

import Exceptions.IllegalXmlDataDupEntityPropNameExceptions;
import PRD.PRDEntity;
import PRD.PRDProperty;
import Property.PropertyType;
import Property.definition.NumericPropertyDefinition;
import Property.definition.PropertyDefinition;

import java.util.*;

public class EntityDefinition {
    private String name;
    private Integer population;
    final private HashMap<String, PropertyDefinition> properties;

    public EntityDefinition(PRDEntity entityDef)
    {
        this.name = entityDef.getName();
        this.population = null;
        this.properties = new HashMap<>();

        for (PRDProperty property : entityDef.getPRDProperties().getPRDProperty()) {
            if(this.properties.containsKey(property.getPRDName())){
                throw new IllegalXmlDataDupEntityPropNameExceptions("Xml contain two Properties with the same name for " + this.name);
            }

            PropertyType propertyType = null;

            try{
                propertyType = PropertyType.valueOf(property.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid property type. A property cannot be of type " + property.getType());
            }

            if(propertyType  == PropertyType.FLOAT){
                this.properties.put(property.getPRDName(),new NumericPropertyDefinition(property));
            }
            else{
                this.properties.put(property.getPRDName(),new PropertyDefinition(property));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public HashMap<String, PropertyDefinition> getProperties() {
        return properties;
    }

    public PropertyDefinition getPropertyByName(String propertyName) {
        return properties.get(propertyName);
    }

    public void addProperty(String propertyName, PropertyDefinition property) {
        properties.put(propertyName,property);
    }

    public void removeProperty(String propertyName) {
            properties.remove(propertyName);
    }

    public List<String> getPropertiesNameList()
    {
        return new ArrayList<>(properties.keySet());
    }

    @Override
    public String toString() {
        StringBuilder propStringBuilder = new StringBuilder();
        for(Map.Entry<String, PropertyDefinition> entry : this.properties.entrySet()) {
            propStringBuilder.append(entry.getValue());
            propStringBuilder.append("\n");
        }

        return "Entity name: " + name + '\n' +
                "population: " + population + '\n' +
                "properties:\n" + propStringBuilder ;
    }
}
