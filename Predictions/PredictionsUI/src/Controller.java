import World.instance.WorldInstance;
import Manager.PredictionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable {



    PredictionManager predictionManager;

    HashMap<String,WorldInstance> previousActivations;

    @FXML
    private Button LoadFileButton;

    @FXML
    private TextField filePathTextField;

    @FXML
    void HandleLoadFileButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
        predictionManager.loadSimulation(filePathTextField.getText());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        predictionManager = new PredictionManager();
        previousActivations = new HashMap<>();
    }
}