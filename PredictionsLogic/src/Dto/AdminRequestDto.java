package Dto;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AdminRequestDto {
    String id;
    String simulationName;
    String userName;
    String reqNumOfRuns;
    String terminationConditiom;
    String status;
    String runningSimulations;
    String finishedSimulations;

    public AdminRequestDto(String id, String simulationName, String userName, String reqNumOfRuns,
                           String terminationConditiom, String status, String runningSimulations, String finishedSimulations) {
        this.id = id;
        this.simulationName = simulationName;
        this.userName = userName;
        this.reqNumOfRuns = reqNumOfRuns;
        this.terminationConditiom = terminationConditiom;
        this.status = status;
        this.runningSimulations = runningSimulations;
        this.finishedSimulations = finishedSimulations;
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

    public String getTerminationConditiom() {
        return terminationConditiom;
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
