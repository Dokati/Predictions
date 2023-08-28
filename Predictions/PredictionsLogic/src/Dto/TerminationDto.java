package Dto;

import java.util.Map;

public class TerminationDto {

     Map<String, Integer> terminations;

    public TerminationDto(Map<String, Integer> terminations) {
        this.terminations = terminations;
    }

    public Map<String, Integer> getTerminations() {
        return terminations;
    }
}
