package Screens.Management;
import Body.FirstScreen.FirstScreenBodyController;
import Dto.SimulationTitlesDetails;
import PrimaryScreen.AdminPrimaryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementController implements Initializable {
    private AdminPrimaryController primaryController;
    @FXML private ScrollPane detailsComponent;
    @FXML private FirstScreenBodyController detailsComponentController;
    @FXML private Button loadFileButton;
    @FXML private TextField filePathTextField;
    @FXML private TextField setThreadsCountTextField;
    @FXML private Button setThreadsCountButton;

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

    public void setMainController(AdminPrimaryController primaryController) {
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
