package Dto;

public class RuleActivationDto {

    String tick;
    String probability;

    public RuleActivationDto(String tick, String probability) {
        this.tick = tick;
        this.probability = probability;
    }

    public String getTick() {
        return tick;
    }

    public String getProbability() {
        return probability;
    }
}
