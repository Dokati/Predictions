package Dto;

import UserRequest.UserRequest;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class AdminRequestDto {
    String id;
    String simulationName;
    String userName;
    String reqNumOfRuns;
    List<String> terminationConditions;
    String status;
    String runningSimulations;
    String finishedSimulations;

    public AdminRequestDto(UserRequest request) {
        this.id = request.getRequestNumber().toString();
        this.simulationName = request.getSimulationName();
        this.userName = request.getUsername();
        this.reqNumOfRuns = request.getRequestedNumOfSimulationRuns().toString();
        this.terminationConditions = request.getTerminationConditionStringList();
        this.status = request.getRequestStatus().toString().toLowerCase();
        this.runningSimulations = request.getNumOfRunningSimulation().toString();
        this.finishedSimulations = request.getNumOfTerminateSimulations().toString();
    }

    public String getId() {
        return id;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getUserName() {
        return userName;
    }

    public String getReqNumOfRuns() {
        return reqNumOfRuns;
    }

    public List<String> getTerminationConditiom() {
        return terminationConditions;
    }

    public String getStatus() {
        return status;
    }

    public String getRunningSimulations() {
        return runningSimulations;
    }

    public String getFinishedSimulations() {
        return finishedSimulations;
    }
}
