package Dto;

public class EnvVariableDto {
    String name;
    String type;
    String range;

    public EnvVariableDto(String name, String type, String range) {
        this.name = name;
        this.type = type;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRange() {
        return range;
    }
    public boolean hasRange() {
        return this.range != null;
    }

}
