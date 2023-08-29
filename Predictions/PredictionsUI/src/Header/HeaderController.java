package Header;

import Dto.SimulationTitlesDetails;
import PrimaryContreoller.PrimaryController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    PrimaryController primaryController;

    @FXML
    private TextField filePathTextField;
    @FXML
    private Button loadFileButton;
    @FXML
    private HBox hBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Button queueMmanagementButton;
    @FXML
    public void HandleLoadFileButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
        SimulationTitlesDetails simulationTitleDto = primaryController.getPredictionManager().loadSimulation(filePathTextField.getText());
        primaryController.SetTitleDetailsOnFirstScreen(simulationTitleDto);

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

    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

}