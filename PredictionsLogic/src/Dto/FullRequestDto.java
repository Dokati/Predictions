package Dto;

import Terminition.Termination;
import UserRequest.UserRequest;
import UserRequest.UserRequestStatusType;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class FullRequestDto {
    private StringProperty requestNumber = new SimpleStringProperty();
    private StringProperty simulationName = new SimpleStringProperty();
    private StringProperty requestedNumOfSimulationRuns = new SimpleStringProperty();
    private StringProperty requestStatus = new SimpleStringProperty();
    private StringProperty numOfRunningSimulation = new SimpleStringProperty();
    private StringProperty numOfTerminateSimulations = new SimpleStringProperty();

    public FullRequestDto(UserRequest request){
        this.setRequestNumber(request.getRequestNumber().toString());
        this.setSimulationName(request.getSimulationName());
        this.setRequestedNumOfSimulationRuns("2");
        this.setRequestStatus(request.getRequestStatus().toString());
        this.setNumOfRunningSimulation(request.getNumOfRunningSimulation().toString());
        this.setNumOfTerminateSimulations(request.getNumOfTerminateSimulations().toString());
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber.set(requestNumber);
    }

    public void setSimulationName(String simulationName) {
        this.simulationName.set(simulationName);
    }

    public void setRequestedNumOfSimulationRuns(String requestedNumOfSimulationRuns) {
        this.requestedNumOfSimulationRuns.set(requestedNumOfSimulationRuns);
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus.set(requestStatus);
    }

    public void setNumOfRunningSimulation(String numOfRunningSimulation) {
        this.numOfRunningSimulation.set(numOfRunningSimulation);
    }

    public void setNumOfTerminateSimulations(String numOfTerminateSimulations) {
        this.numOfTerminateSimulations.set(numOfTerminateSimulations);
    }
}
