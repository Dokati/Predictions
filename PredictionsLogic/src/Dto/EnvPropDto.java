package Dto;

import Property.Range;

public class EnvPropDto {

    String name;
    String Type;
    Range range;

    public EnvPropDto(String name, String type, Range range) {
        this.name = name;
        Type = type;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return Type;
    }

    public Range getRange() {
        return range;
    }

    public boolean hasRange(){
        return this.range != null;
    }
}
