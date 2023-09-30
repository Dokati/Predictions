package Dto;

import java.util.List;

public class EnviormentVariablesDto {

    List<EnvVariableDto> envVariableDtoList;

    public EnviormentVariablesDto(List<EnvVariableDto> envVariableDtoList) {
        this.envVariableDtoList = envVariableDtoList;
    }

    public List<EnvVariableDto> getEnvVariableDtoList() {
        return envVariableDtoList;
    }
}
