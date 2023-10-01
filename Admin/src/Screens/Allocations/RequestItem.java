package Screens.Allocations;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import static Paths.Paths.ACCEPT_BUTTON;
import static Paths.Paths.REJECT_BUTTON;


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
        this.runningSimulations = "0";
        this.finishedSimulations = "0";
        this.approveButton = new Button();
        ImageView approveImage = new ImageView(ACCEPT_BUTTON);
        approveImage.setFitHeight(20);
        approveImage.setFitWidth(20);
        this.approveButton.setGraphic(approveImage);
        this.rejectButton = new Button();
        ImageView rejectImage = new ImageView(REJECT_BUTTON);
        rejectImage.setFitHeight(20);
        rejectImage.setFitWidth(20);
        this.rejectButton.setGraphic(rejectImage);
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
