package Dto;

import java.util.List;

public class SimulationTitlesDetails {

    String  simulationName;
    List<String> entitiesNames;
    List<RuleTitleDto> rulesTitleDto;
    List<String> envVariableNames;
    Integer populationSpace;
    Integer threadCount;

    public SimulationTitlesDetails(String simulationName, List<String> entitiesNames, List<RuleTitleDto> rulesDto, List<String> envVariableNames,Integer populationSpace, Integer threadCount) {
        this.entitiesNames = entitiesNames;
        this.rulesTitleDto = rulesDto;
        this.envVariableNames = envVariableNames;
        this.populationSpace = populationSpace;
        this.simulationName = simulationName;
        this.threadCount = threadCount;
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

    public int getThreadsCount() {
        return threadCount;
    }

    public String getWorldName() {
        return simulationName;
    }

    public String getSimulationName() {
        return simulationName;
    }
}
