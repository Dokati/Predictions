package Screens.Management;

import Dto.SimulationTitlesDetails;
import PrimaryScreen.PrimaryController;
import Screen.details.FirstScreenBodyController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static Request.RequestCreator.CreateThreadPoolSizePostRequest;
import static Request.RequestCreator.ExecuteRequest;

public class ManagementController implements Initializable {
    private PrimaryController primaryController;
    @FXML private ScrollPane detailsComponent;
    @FXML private FirstScreenBodyController detailsComponentController;
    @FXML private Button loadFileButton;
    @FXML private TextField filePathTextField;
    @FXML private TextField setThreadsCountTextField;
    @FXML private Button setThreadsCountButton;
    @FXML private Label threadCountLabel;

    @FXML private Label runningSimulationsLabel;
    @FXML private Label waitingSimulationsLabel;
    @FXML private Label finishedSimulations;
    @FXML private TextArea queueTextArea;

    public void initialize(URL location, ResourceBundle resources) {
    }
    @FXML public void HandleLoadFileButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            primaryController.loadSimulation(selectedFile.getAbsolutePath());
        }
    }
    @FXML void ThreadsCountButtonOnClick(ActionEvent event) {
        if (setThreadsCountTextField.getText().isEmpty()) {
            threadCountLabel.setTextFill(Color.RED);
            threadCountLabel.setText("Please enter a number");
        }
        else {
            threadCountLabel.setText("");
            Request request = CreateThreadPoolSizePostRequest(setThreadsCountTextField.getText());
            Response response = ExecuteRequest(request);
            if (response.isSuccessful()) {
                this.primaryController.setThreadPoolSize(Integer.parseInt(setThreadsCountTextField.getText()));
                threadCountLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                threadCountLabel.setText("Thread pool size set to " + setThreadsCountTextField.getText());
            } else {
                threadCountLabel.setTextFill(Color.RED);
                threadCountLabel.setText("Failed to set thread pool size");
            }
        }

    }

    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
        initializeThatDependsOnPrimaryInit();
    }

    private void initializeThatDependsOnPrimaryInit() {
        bindlabelsToQueueManager();
    }
    private void bindlabelsToQueueManager() {
        runningSimulationsLabel.textProperty().bind(primaryController.queueManager.getRunningSimulations().asString());
        waitingSimulationsLabel.textProperty().bind(primaryController.queueManager.getWaitingSimulations().asString());
        finishedSimulations.textProperty().bind(primaryController.queueManager.getFinishedSimulations().asString());
    }

    public TextField getFilePathTextField() {
        return filePathTextField;
    }

    public void SetTitleDetailsOnFirstScreen(SimulationTitlesDetails simulationTitleDto) {
        this.detailsComponentController.setTitleDetail(simulationTitleDto);

    }


}
