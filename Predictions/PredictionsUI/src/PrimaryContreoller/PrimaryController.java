package PrimaryContreoller;

import Body.FirstScreen.FirstScreenBodyController;
import Dto.SimulationTitlesDetails;
import Header.HeaderController;
import Manager.PredictionManager;
import StylePaths.StylePaths;
import World.instance.WorldInstance;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {
    private PredictionManager predictionManager;
    private HashMap<String, WorldInstance> previousActivations;
    @FXML private BorderPane borderPane;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane firstScreenBody;
    @FXML private FirstScreenBodyController firstScreenBodyController;
    @FXML
    public void initialize() {
        if (headerComponentController != null && firstScreenBodyController != null) {
            headerComponentController.setMainController(this);
            firstScreenBodyController.setMainController(this);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        predictionManager = new PredictionManager();
        previousActivations = new HashMap<>();

        if (headerComponentController != null && firstScreenBodyController != null) {
            headerComponentController.setMainController(this);
            firstScreenBodyController.setMainController(this);
        }
    }
    public PredictionManager getPredictionManager() {
        return predictionManager;
    }

    public HashMap<String, WorldInstance> getPreviousActivations() {
        return previousActivations;
    }


    public void SetTitleDetailsOnFirstScreen(SimulationTitlesDetails simulationTitleDto) {
        this.firstScreenBodyController.setTitleDetail(simulationTitleDto);

    }

    public void setBackgroundskinColor(String color) {

        if(color.equals("dark")){
            this.borderPane.getStylesheets().clear();
            this.borderPane.getStylesheets().add(StylePaths.DARK_THEME);
        }
        if(color.equals("rose-gold")){
            this.borderPane.getStylesheets().clear();
            this.borderPane.getStylesheets().add(StylePaths.ROSE_GOLD_THEME);
        }
        if(color.equals("default")){
            this.borderPane.getStylesheets().clear();
            this.borderPane.getStylesheets().add(StylePaths.DEFAULT);
        }


    }
}
