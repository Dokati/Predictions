package Header;

import Dto.SimulationTitlesDetails;
import PrimaryContreoller.PrimaryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {

    PrimaryController primaryController;

    @FXML private TextField filePathTextField;
    @FXML private Button loadFileButton;
    @FXML private HBox hBox;
    @FXML private MenuBar menuBar;
    @FXML private Label runningSimulationsLabel;
    @FXML private Label waitingSimulationsLabel;
    @FXML private Label finishedSimulations;
    @FXML private TextArea queueTextArea;
    @Override
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
    @FXML
    void ChangeBackgroundskinColor(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String color = menuItem.getText();
        primaryController.setBackgroundskinColor(color);
    }

    @FXML
    void ChangeButtonsAppearance(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String color = menuItem.getText();
        primaryController.setButtonsAppearance(color);

    }

    @FXML
    void ChangeLabelAppearance(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String SizeNFont = menuItem.getText();
        primaryController.setLabelsAppearance(SizeNFont);
    }
    public void setQueueManageTextarea(){

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

    public void clearHeader() {

    }
}