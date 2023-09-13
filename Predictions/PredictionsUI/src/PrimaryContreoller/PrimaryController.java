package PrimaryContreoller;

import Body.FirstScreen.FirstScreenBodyController;
import Body.SecondScreen.SecondScreenBodyController;
import Body.ThirdScreen.RunSimulationTask;
import Body.ThirdScreen.ThirdScreenBodyController;
import Dto.SimulationEndDetailsDto;
import Dto.SimulationExecutionDto;
import Dto.SimulationTitlesDetails;
import Header.HeaderController;
import Manager.PredictionManager;
import Paths.StylePaths;
import World.instance.WorldInstance;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PrimaryController implements Initializable {
    private PredictionManager predictionManager;

    @FXML private BorderPane borderPane;
    @FXML private TabPane tabPane;
    @FXML private ScrollPane headerComponent;
    @FXML private HeaderController headerComponentController;
    @FXML private ScrollPane firstScreenBody;
    @FXML private FirstScreenBodyController firstScreenBodyController;
    @FXML private ScrollPane secondScreenBody;
    @FXML private SecondScreenBodyController secondScreenBodyController;
    @FXML private ScrollPane thirdScreenBody;
    @FXML private ThirdScreenBodyController thirdScreenBodyController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        predictionManager = new PredictionManager();

        if (headerComponentController != null && firstScreenBodyController != null && secondScreenBodyController !=null
        && thirdScreenBodyController != null) {
            headerComponentController.setMainController(this);
            firstScreenBodyController.setMainController(this);
            secondScreenBodyController.setMainController(this);
            thirdScreenBodyController.setMainController(this);
        }
    }
    public PredictionManager getPredictionManager() {
        return predictionManager;
    }



    public void SetTitleDetailsOnFirstScreen(SimulationTitlesDetails simulationTitleDto) {
        this.firstScreenBodyController.setTitleDetail(simulationTitleDto);

    }

    public void setBackgroundskinColor(String color) {
        removeBackgroundStylesheets(this.borderPane);
        if(color.equals("dark")){
            this.borderPane.getStylesheets().add(StylePaths.DARK_THEME);
        }
        if(color.equals("rose-gold")){
            this.borderPane.getStylesheets().add(StylePaths.ROSE_GOLD_THEME);
        }
        if(color.equals("default")){
            this.borderPane.getStylesheets().add(StylePaths.DEFAULT);
        }


    }

    public void setButtonsAppearance(String color) {
        removeButtonStylesheets(this.borderPane);
        if(color.equals("dark")){
            this.borderPane.getStylesheets().add(StylePaths.DARK_BUTTONS);
        }
        if(color.equals("rose-gold")){
            this.borderPane.getStylesheets().add(StylePaths.ROSE_BUTTONS);
        }
        if(color.equals("default")){
            this.borderPane.getStylesheets().add(StylePaths.DEFAULT_BUTTONS);
        }
    }

    private void removeButtonStylesheets(BorderPane borderPane) {
        List<String> stylesheetsToRemove = new ArrayList<>();

        for (String stylesheet : borderPane.getStylesheets()) {
            if (stylesheet.endsWith("Button.css")) {
                stylesheetsToRemove.add(stylesheet);
            }
        }

        borderPane.getStylesheets().removeAll(stylesheetsToRemove);
    }

    private void removeBackgroundStylesheets(BorderPane borderPane) {
        List<String> stylesheetsToRemove = new ArrayList<>();

        for (String stylesheet : borderPane.getStylesheets()) {
            if (stylesheet.endsWith("Background.css")) {
                stylesheetsToRemove.add(stylesheet);
            }
        }

        borderPane.getStylesheets().removeAll(stylesheetsToRemove);
    }

    public void setLabelsAppearance(String sizeNFont) {
        removeLabelStylesheets(this.tabPane);
        if(sizeNFont.equals("Medium montserrat")){
            this.tabPane.getStylesheets().add(StylePaths.MEDIUM_LABEL);
        }
        if(sizeNFont.equals("Large raleway")){
            this.tabPane.getStylesheets().add(StylePaths.LARGE_LABEL);
        }
        if(sizeNFont.equals("default")){
            this.tabPane.getStylesheets().add(StylePaths.DEFAULT_LABEL);
        }
    }

    private void removeLabelStylesheets(TabPane tabPane) {
        List<String> stylesheetsToRemove = new ArrayList<>();

        for (String stylesheet : tabPane.getStylesheets()) {
            if (stylesheet.endsWith("Label.css")) {
                stylesheetsToRemove.add(stylesheet);
            }
        }

        tabPane.getStylesheets().removeAll(stylesheetsToRemove);
    }

    public void initFirstNSecondScrean(String FilePath) {
        //first screen
        SimulationTitlesDetails simulationTitleDto = null;
        try {
            simulationTitleDto = getPredictionManager().loadSimulation(FilePath);
            SetTitleDetailsOnFirstScreen(simulationTitleDto);
        }
        catch (IllegalArgumentException exception){
            showAlertToUser(exception.getMessage());
        }
        catch (RuntimeException e) {
            showAlertToUser("Loading the XML file failed");
        }
//      second screen - the envprop list and entity list

        this.secondScreenBodyController.setEnvPropTable();
        this.secondScreenBodyController.setEntitiesPopulationList(simulationTitleDto.getEntitiesNames(),simulationTitleDto.getPopulationSpace());
    }

    private void showAlertToUser(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ho no, an error occurred!");
        alert.setContentText("Error message: " + message);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(closeButton);

        // Show the dialog and wait for user interaction
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == closeButton) {
            // User clicked "Close" or closed the dialog
        }
    }

    public void runSimulation(Map<String, Integer> entitiesPopulationMap, Map<String, String> envPropValues) {
        HashMap<Integer, WorldInstance> simulationList = this.predictionManager.getSimulationList();
        int simulationIdNumber = this.predictionManager.getSimulationIdNumber();
        String simulationId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss"));

        predictionManager.InitializePopulation(entitiesPopulationMap);

        simulationList.put(simulationIdNumber,new WorldInstance(predictionManager.getWorldDefinition(),envPropValues));

        Future<SimulationEndDetailsDto> futureResult = predictionManager.threadPool.submit(simulationList.get(simulationIdNumber));

        SimulationExecutionDto simulationExecutionDto = new SimulationExecutionDto(simulationId,"Running", simulationIdNumber, entitiesPopulationMap);
        this.thirdScreenBodyController.addSimulationToTable(simulationExecutionDto);
        RunSimulationTask runSimulationTask = new RunSimulationTask(simulationList.get(simulationIdNumber), simulationExecutionDto, this.thirdScreenBodyController);
        Thread thread = new Thread(runSimulationTask);
        thread.start();

        this.predictionManager.setSimulationIdNumber(simulationIdNumber+1);
    }

    public void jumpToResultTab() {
        Tab selectedTab = tabPane.getTabs().get(2);
        tabPane.getSelectionModel().select(selectedTab);
    }

    public void clearAllScreens() {
         firstScreenBodyController.clearFirstScren();
         secondScreenBodyController.clearSecondScreen();
    }
}
