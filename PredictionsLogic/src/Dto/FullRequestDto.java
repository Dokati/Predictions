package Dto;

import Terminition.Termination;
import UserRequest.UserRequest;
import UserRequest.UserRequestStatusType;
import java.util.List;

public class FullRequestDto {
    private String requestNumber;
    private String simulationName;
    private String requestedNumOfSimulationRuns;
    private String requestStatus;
    private String numOfRunningSimulation;
    private String numOfTerminateSimulations;

    public FullRequestDto(UserRequest request){
        this.requestNumber = request.getRequestNumber().toString();
        this.simulationName = request.getSimulationName();
        this.requestedNumOfSimulationRuns = request.getRequestedNumOfSimulationRuns().toString();
        this.requestStatus = request.getRequestStatus().toString().toLowerCase();
        this.numOfRunningSimulation = request.getNumOfRunningSimulation().toString();
        this.numOfTerminateSimulations = request.getNumOfTerminateSimulations().toString();
    }
}
