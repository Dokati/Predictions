package Body.ThirdScreen;
import Dto.SimulationExecutionDto;
import EndSimulationDetails.EntitySimulationEndDetails;
import EndSimulationDetails.PropertySimulationEndDetails;
import Paths.ButtonsImagePath;
import PrimaryContreoller.PrimaryController;
import World.instance.SimulationStatusType;
import World.instance.WorldInstance;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;



import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ThirdScreenBodyController implements Initializable {

    @FXML private TableView<SimulationExecutionDto> executionListTable;
    @FXML private HBox detailsHbox;
    @FXML private VBox buttonVbox;
    @FXML private TableView<EntityPopulation> entityPopulationTable;
    @FXML private Text ticksText;
    @FXML private Text secondText;
    @FXML private HBox progressHbox;
    @FXML private TabPane resultTabPane;
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private ComboBox<String> entitiesComboBox;
    @FXML private ListView<String> propertiesListView;
    @FXML private TableView<PropertyHistPair> propertyHistogramTable;
    @FXML private Label oconsistencyLabel;
    @FXML private Label avgValOfPropLabel;
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
    ProgressBar progressBar;
    Text progressText;
    Text progressPrecent;


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
        ///////-----------------------------------///////////////////
        progressBar = new ProgressBar();
        progressBar.setPrefSize(200,25);
        progressText = new Text("Simulation Progress:");
        progressText.setFont(new Font(19));
        progressPrecent = new Text();
        progressPrecent.setFont(new Font(19));
        ///////-----------------------------------///////////////////
        resultTabPane.setVisible(false);
        ///////-----------------------------------///////////////////
        propertyHistogramTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("property"));
        propertyHistogramTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));
        propertyHistogramTable.setPlaceholder(new Label("No instance with\n this property"));

        ///////-----------------------------------///////////////////



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
        if(selectedSimulationDetails.isProgressable()){
            progressBar.setDisable(false);
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(selectedSimulationDetails.getProgress());
            progressPrecent.textProperty().unbind();
            IntegerProperty percent = new SimpleIntegerProperty();
            percent.bind(selectedSimulationDetails.getProgress().multiply(100));
            progressPrecent.textProperty().bind(Bindings.concat(percent.asString(),"%"));
            if(progressHbox.getChildren().isEmpty()) progressHbox.getChildren().addAll(progressText,progressBar,progressPrecent);
        }
        else {
            progressHbox.getChildren().clear();
        }

        if (!selectedSimulationDetails.isRunning()){
            loadSimulationResult();
            resultTabPane.setVisible(true);
        }
        else {
            resultTabPane.setVisible(false);

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
                simulatiosnMap.get(chosenSimulationId).StopSimulation();
                progressBar.setDisable(true);
                simulationsDataList.get(chosenSimulationId-1).FinishRunning();
            }
        });
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
    public void SimulationFinished(Integer id) {
        if(chosenSimulationId != null && chosenSimulationId == id){
            disableControlButtons();
            this.resultTabPane.setVisible(true);
            loadSimulationResult();
        }
        updateSimulationStatusInTable(id);

    }

    private void loadSimulationResult() {
        //loadEntityAmountGraph();
        loadEntitiesComboBoxAndPropList();

    }



    private void loadEntitiesComboBoxAndPropList() {
        entitiesComboBox.getItems().clear();
        entitiesComboBox.getItems().addAll(chosenSimulation.getEndSimulationDetails().keySet());
        entitiesComboBox.getSelectionModel().selectFirst();
        entitiesComboBox.setOnAction(event -> {
            String selectedEntity = entitiesComboBox.getSelectionModel().getSelectedItem();
            loadPropertiesListView(selectedEntity);
        });
        loadPropertiesListView(entitiesComboBox.getSelectionModel().getSelectedItem());
    }

    private void loadPropertiesListView(String selectedEntity) {
        propertiesListView.getItems().clear();
        if(selectedEntity == null) return;
        propertiesListView.getItems().addAll(chosenSimulation.getEndSimulationDetails().get(selectedEntity).getEndSimulationPropertiesDetails().keySet());
        propertiesListView.getSelectionModel().selectFirst();
        propertiesListView.setOnMouseClicked(event -> {
            String selectedProperty = propertiesListView.getSelectionModel().getSelectedItem();
            loadPropertyHistogramTable(selectedProperty,selectedEntity);
            loadStatisticsLabels(selectedProperty,selectedEntity);
        });
        loadPropertyHistogramTable(propertiesListView.getSelectionModel().getSelectedItem(),selectedEntity);
    }

    private void loadStatisticsLabels(String selectedProperty, String selectedEntity) {
        PropertySimulationEndDetails propertySimulationEndDetails = chosenSimulation.getEndSimulationDetails()
                .get(selectedEntity).getEndSimulationPropertiesDetails().get(selectedProperty);
        String oconsistencyLabeltext = "Consistency: ";
        oconsistencyLabel.setText(oconsistencyLabeltext+propertySimulationEndDetails.getConsistency().toString());
        String avgValOfPropLabelText = propertySimulationEndDetails.getAverage() != null ? propertySimulationEndDetails.getAverage().toString() : null;
        if (avgValOfPropLabelText != null) {
            avgValOfPropLabelText = "Average value of property: " + avgValOfPropLabelText;
            avgValOfPropLabel.setText(avgValOfPropLabelText);
        }
    }

    private void loadPropertyHistogramTable(String selectedProperty,String selectedEntity) {
        propertyHistogramTable.getItems().clear();
        if(selectedProperty == null || selectedEntity == null) return;
        Map<Object,Integer> propertyHistogram = chosenSimulation.getEndSimulationDetails().get(selectedEntity).getEndSimulationPropertiesDetails().get(selectedProperty).getPropertyHistogram();
        propertyHistogram.forEach((key, value) -> propertyHistogramTable.getItems().add(new PropertyHistPair(key.toString(),value)));
    }

    private void loadEntityAmountGraph() {
        lineChart.getData().clear();
        int iterationLimit = 2000; // Set the desired iteration limit
        int iterationCount = 0; // Initialize the counter
        // Iterate through the map and create a series for each key
        for (Map.Entry<String, EntitySimulationEndDetails> entry : chosenSimulation.getEndSimulationDetails().entrySet()) {
            String key = entry.getKey();
            EntitySimulationEndDetails details = entry.getValue();

            // Create a new series with the key as the name
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(key);
            iterationCount = 0;
            // Iterate through populationByTick and add data points to the series
            for (Map.Entry<Integer, Integer> populationEntry : details.getPopulationByTick().entrySet()) {
                if (iterationCount >= iterationLimit) {
                    break; // Exit the loop when the limit is reached
                }
                Integer tick = populationEntry.getKey();
                Integer population = populationEntry.getValue();

                // Create an XYChart.Data point and add it to the series
                series.getData().add(new XYChart.Data<>(tick, population));
                iterationCount++; // Increment the counter
            }

            // Add the series to the chart
            lineChart.getData().add(series);
        }
    }

    private void updateSimulationStatusInTable(Integer id) {
        Optional<SimulationExecutionDto> optionalDto = simulationsDataList.stream()
                .filter(dto -> dto.getNumberId().equals(id))
                .findFirst();
        optionalDto.get().setStatus("Finished");
        this.executionListTable.refresh();
    }
    public void RefreshEntityPopTable() {
        this.entityPopulationTable.refresh();
    }
}
