package UserRequest;

import Terminition.Termination;

import java.util.List;

public class UserRequest {
    private String simulationName;
    private String username;
    private int requestedNumOfSimulationRuns;
    private List<Termination> terminationConditions;

    public UserRequest(String simulationName, String username, int requestedLectures, List<Termination> terminationConditions) {
        this.simulationName = simulationName;
        this.username = username;
        this.requestedNumOfSimulationRuns = requestedLectures;
        this.terminationConditions = terminationConditions;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRequestedLectures() {
        return requestedNumOfSimulationRuns;
    }

    public void setRequestedLectures(int requestedLectures) {
        this.requestedNumOfSimulationRuns = requestedLectures;
    }

    public List<Termination> getTerminationConditions() {
        return terminationConditions;
    }

    public void setTerminationConditions(List<Termination> terminations) {
        this.terminationConditions = terminations;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "simulationName='" + simulationName + '\'' +
                ", username='" + username + '\'' +
                ", requestedLectures=" + requestedNumOfSimulationRuns +
                ", terminationConditions=" + terminationConditions +
                '}';
    }
}
