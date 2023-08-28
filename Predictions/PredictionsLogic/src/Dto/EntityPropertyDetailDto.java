package Dto;

import java.util.List;

public class EntityPropertyDetailDto {

    List<String> propertiesDetails;
    String propertyName;
    String propertyType;
    String randomInit;
    String range;

    public EntityPropertyDetailDto(List<String> propertiesDetails) {
        this.propertiesDetails = propertiesDetails;
    }

    public List<String> getPropertiesDetails() {
        return propertiesDetails;
    }
}
