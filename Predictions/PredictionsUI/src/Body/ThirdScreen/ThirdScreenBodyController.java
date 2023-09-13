package Body.ThirdScreen;
import Dto.SimulationExecutionDto;
import Paths.ButtonsImagePath;
import PrimaryContreoller.PrimaryController;
import World.instance.SimulationStatusType;
import World.instance.WorldInstance;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;



import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ThirdScreenBodyController implements Initializable {

    @FXML private TableView<SimulationExecutionDto> executionListTable;
    @FXML private HBox detailsHbox;
    @FXML private VBox buttonVbox;
    @FXML private TableView<EntityPopulation> entityPopulationTable;
    @FXML private Text ticksText;
    @FXML private Text secondText;
    ObservableList<SimulationExecutionDto> simulationsDataList;
    ObservableList<EntityPopulation> entityPopulationList;
    private PrimaryController primaryController;
    Integer chosenSimulationId;
    SimulationExecutionDto chosenSimulation;
    Button playButton;
    Button pauseButton;
    Button stopButton;

    ImageView playimage;
    ImageView stopimage;
    ImageView pauseimage;
    ImageView disableplayimage;
    ImageView disableStopimage;
    ImageView disablePauseimage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ///////---------SimulationsTable------------///////////////////
        executionListTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        executionListTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("status"));
        simulationsDataList = FXCollections.observableArrayList();
        executionListTable.setItems(simulationsDataList);
        executionListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                simulationGotSelected();
            }
        });
        ///////-----------------------------------///////////////////
        entityPopulationTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("entityName"));
        entityPopulationTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("population"));
        entityPopulationList = FXCollections.observableArrayList();
        entityPopulationTable.setItems(entityPopulationList);

        //setting the table items will be on run time.
        ///////-----------------------------------///////////////////

        setControlButtons();
        //this.buttonVbox.getChildren().addAll(playButton,pauseButton,stopButton);

        chosenSimulationId = null;

    }

    private void simulationGotSelected() {
        SimulationExecutionDto selectedItem = executionListTable.getSelectionModel().getSelectedItem();
        chosenSimulation = selectedItem;
        chosenSimulationId = selectedItem.getNumberId();
        if (selectedItem != null) {
            handleSimulationChosenClick(selectedItem);
        }
    }

    private void handleSimulationChosenClick(SimulationExecutionDto selectedSimulationDetails) {
        createTheDetailsArea(selectedSimulationDetails);
    }
    private void createTheDetailsArea(SimulationExecutionDto selectedSimulationDetails) {
        entityPopulationList.clear();
        this.buttonVbox.getChildren().clear();
        selectedSimulationDetails.getEntitiesPopulation().forEach((key, valueProperty) -> entityPopulationList.add(new EntityPopulation(key,valueProperty)));

        this.ticksText.textProperty().unbind();
        this.ticksText.textProperty().bind(Bindings.concat("Ticks: ", selectedSimulationDetails.getTickProperty().asString()));
        this.secondText.textProperty().unbind();
        this.secondText.textProperty().bind(Bindings.concat("Seconds: ", selectedSimulationDetails.getTimeProperty().asString()));

        this.buttonVbox.getChildren().addAll(playButton,pauseButton,stopButton);

        if (selectedSimulationDetails.isRunning()){
            enableControlButtons();
        }
        else {
            disableControlButtons();
        }


    }

    private void setControlButtons() {
        playButton = new Button();
        pauseButton = new Button();
        stopButton = new Button();
        playimage = new ImageView(new Image(ButtonsImagePath.PLAY));
        playimage.setFitWidth(30);
        playimage.setFitHeight(30);
        stopimage = new ImageView(new Image(ButtonsImagePath.STOP));
        stopimage.setFitWidth(30);
        stopimage.setFitHeight(30);
        pauseimage = new ImageView(new Image(ButtonsImagePath.PAUSE));
        pauseimage.setFitWidth(30);
        pauseimage.setFitHeight(30);
        disableplayimage = new ImageView(new Image(ButtonsImagePath.DISABLE_PLAY));
        disableplayimage.setFitWidth(30);
        disableplayimage.setFitHeight(30);
        disableStopimage = new ImageView(new Image(ButtonsImagePath.DISABLE_STOP));
        disableStopimage.setFitWidth(30);
        disableStopimage.setFitHeight(30);
        disablePauseimage = new ImageView(new Image(ButtonsImagePath.DISABLE_PAUSE));
        disablePauseimage.setFitWidth(30);
        disablePauseimage.setFitHeight(30);
        enableControlButtons();

    }

    private void setControlButtonsListeners() {
        HashMap<Integer, WorldInstance> simulatiosnMap =
                primaryController.getPredictionManager().getSimulationList();

        playButton.setOnAction(event -> {
            SimulationStatusType simulationStatus = simulatiosnMap.get(chosenSimulationId).getStatus();
            if(simulationStatus.equals(SimulationStatusType.Pause)){
                simulatiosnMap.get(chosenSimulationId).ChangeSimulationStatusToRunning();}
        });

        pauseButton.setOnAction(event -> {
            SimulationStatusType simulationStatus = simulatiosnMap.get(chosenSimulationId).getStatus();
            if(simulationStatus.equals(SimulationStatusType.Running)){
                simulatiosnMap.get(chosenSimulationId).PauseSimulation();}
        });

        stopButton.setOnAction(event -> {
            SimulationStatusType simulationStatus = simulatiosnMap.get(chosenSimulationId).getStatus();
            if(simulationStatus.equals(SimulationStatusType.Running) ||
                    simulationStatus.equals(SimulationStatusType.Pause)){
                simulatiosnMap.get(chosenSimulationId).StopSimulation();}
        });
    }

    private void stopSimulation(SimulationStatusType simulationStatus) {
        simulationStatus = SimulationStatusType.Stop;

    }

    private void PauseSimulation(SimulationStatusType simulationStatus) {
        simulationStatus = SimulationStatusType.Pause;
    }

    private void changeSimulationStatusToRunning(SimulationStatusType simulationStatus) {
        simulationStatus = SimulationStatusType.Running;
    }

    private void enableControlButtons(){
        playButton.setGraphic(playimage);
        playButton.setDisable(false);
        pauseButton.setGraphic(pauseimage);
        pauseButton.setDisable(false);
        stopButton.setGraphic(stopimage);
        stopButton.setDisable(false);
    }
    private void disableControlButtons(){
        playButton.setGraphic(disableplayimage);
        playButton.setDisable(true);
        pauseButton.setGraphic(disablePauseimage);
        pauseButton.setDisable(true);
        stopButton.setGraphic(disableStopimage);
        stopButton.setDisable(true);
    }


    public void addSimulationToTable(SimulationExecutionDto simulationExecutionDto) {
        simulationsDataList.add(simulationExecutionDto);
        this.executionListTable.getSelectionModel().select(simulationsDataList.size()-1);
        simulationGotSelected();
        executionListTable.refresh();
    }

    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
        initializeThatDependsOnPrimaryInit();
    }

    private void initializeThatDependsOnPrimaryInit() {
        setControlButtonsListeners();
    }

    public void chosenSimulationFinished(Integer id) {
        if(chosenSimulationId != null && chosenSimulationId == id){
            disableControlButtons();
        }

    }

    public void RefreshEntityPopTable() {
        this.entityPopulationTable.refresh();
    }
}
