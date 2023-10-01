package UserRequest;

import Terminition.Termination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserRequest {
    private Integer requestNumber;
    private String simulationName;
    private String username;
    private int requestedNumOfSimulationRuns;
    private List<Termination> terminationConditions;
    private boolean openRequest;
    private UserRequestStatusType requestStatus;
    private Integer numOfRunningSimulation;
    private Integer numOfTerminateSimulations;

    public UserRequest(Integer requestNumber,String simulationName, String username, int requestedLectures, HashMap<String, String> terminationConditions) {
        this.requestNumber = requestNumber;
        this.simulationName = simulationName;
        this.username = username;
        this.requestedNumOfSimulationRuns = requestedLectures;
        this.openRequest = true;
        this.requestStatus = UserRequestStatusType.Waiting;
        this.numOfRunningSimulation = 0;
        this.numOfTerminateSimulations = 0;
        this.terminationConditions = new ArrayList<>();
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestStatus(UserRequestStatusType requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean isOpenRequest() {
        return openRequest;
    }

    public UserRequestStatusType getRequestStatus() {
        return requestStatus;
    }

    public Integer getNumOfRunningSimulation() {
        return numOfRunningSimulation;
    }

    public Integer getNumOfTerminateSimulations() {
        return numOfTerminateSimulations;
    }

    public void setRequestApproved(UserRequestStatusType requestStatus) {
        this.requestStatus = requestStatus;
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
