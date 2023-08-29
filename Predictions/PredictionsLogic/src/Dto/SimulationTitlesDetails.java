package Dto;

import java.util.List;

public class SimulationTitlesDetails {

    List<String> entitiesNames;
    List<RuleTitleDto> rulesTitleDto;
    List<String> envVariableNames;

    Integer populationSpace;

    public SimulationTitlesDetails(List<String> entitiesNames, List<RuleTitleDto> rulesDto, List<String> envVariableNames,Integer populationSpace) {
        this.entitiesNames = entitiesNames;
        this.rulesTitleDto = rulesDto;
        this.envVariableNames = envVariableNames;
        this.populationSpace = populationSpace;
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

    public Integer getPopulationSpace() {
        return populationSpace;
    }
}
