package Screens.Allocations;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


public class RequestItem {
    String id;
    String simulationName;
    String userName;
    String reqNumOfRuns;
    String terminationConditiom;
    Label status;
    String runningSimulations;
    String finishedSimulations;
    Button approveButton;
    Button rejectButton;

    public RequestItem(String id) {
        this.id = id;
        this.simulationName = "simulationName";
        this.userName = "userName";
        this.reqNumOfRuns = "reqNumOfRuns";
        this.terminationConditiom = "terminationConditiom";
        this.status = new Label("status");
        this.status.setTextFill(javafx.scene.paint.Color.web("#FF0000"));
        this.runningSimulations = "runningSimulations";
        this.finishedSimulations = "finishedSimulations";
        this.approveButton = new Button("approveButton");
        this.rejectButton = new Button("rejectButton");

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

    public Label getStatus() {
        return status;
    }

    public String getRunningSimulations() {
        return runningSimulations;
    }

    public String getFinishedSimulations() {
        return finishedSimulations;
    }

    public Button getApproveButton() {
        return approveButton;
    }

    public Button getRejectButton() {
        return rejectButton;
    }



}
