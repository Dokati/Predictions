package Dto;

import java.util.List;

public class RuleTitleDto {

    String name;
    RuleActivationDto activation;
    List<String> actionNames;

    public RuleTitleDto(String name, String tick, String probability, List<String> actionNames) {
        this.name = name;
        this.activation = new RuleActivationDto(tick, probability);
        this.actionNames = actionNames;
    }

    public String getName() {
        return name;
    }

    public RuleActivationDto getActivation() {
        return activation;
    }

    public List<String> getActionNames() {
        return actionNames;
    }
}
