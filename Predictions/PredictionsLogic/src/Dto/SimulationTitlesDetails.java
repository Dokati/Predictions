package Dto;

import java.util.List;

public class SimulationTitlesDetails {

    List<String> entitiesNames;
    List<RuleTitleDto> rulesTitleDto;
    List<String> envVariableNames;

    public SimulationTitlesDetails(List<String> entitiesNames, List<RuleTitleDto> rulesDto, List<String> envVariableNames) {
        this.entitiesNames = entitiesNames;
        this.rulesTitleDto = rulesDto;
        this.envVariableNames = envVariableNames;
    }

    public List<String> getEntitiesNames() {
        return entitiesNames;
    }

    public List<RuleTitleDto> getRulesTitleDto() {
        return rulesTitleDto;
    }

    public List<String> getEnvVariableNames() {
        return envVariableNames;
    }
}
