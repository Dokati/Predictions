package Body.ThirdScreen;
import Dto.SimulationExecutionDto;
import Paths.ButtonsImagePath;
import PrimaryContreoller.PrimaryController;
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
import java.util.ResourceBundle;

public class ThirdScreenBodyController implements Initializable {

    @FXML private TableView<SimulationExecutionDto> executionListTable;
    @FXML private HBox detailsHbox;
    @FXML private VBox buttonVbox;
    @FXML private TableView<EntityPopulation> entityPopulationTable;
    @FXML private Text ticksText;
    @FXML private Text secondText;
    ObservableList<SimulationExecutionDto> simulationsData;
    ObservableList<EntityPopulation> entityPopulationList;

    private PrimaryController primaryController;
    String chosenSimulationId;
    Button playButton;
    Button pauseButton;
    Button stopButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ///////---------SimulationsTable------------///////////////////
        executionListTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        executionListTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("status"));
        simulationsData = FXCollections.observableArrayList();
        executionListTable.setItems(simulationsData);
        executionListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                SimulationExecutionDto selectedItem = executionListTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    handleSimulationChosenClick(selectedItem);
                }
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


    }

    private void handleSimulationChosenClick(SimulationExecutionDto selectedSimulationDetails) {

        createTheDetailsArea(selectedSimulationDetails);

    }

    private void createTheDetailsArea(SimulationExecutionDto selectedSimulationDetails) {
        selectedSimulationDetails.getEntitiesPopulation().forEach((key, valueProperty) -> entityPopulationList.add(new EntityPopulation(key,valueProperty)));

        this.ticksText.textProperty().unbind();
        this.ticksText.textProperty().bind(Bindings.concat("Ticks: ", selectedSimulationDetails.getTickProperty().asString()));
        this.secondText.textProperty().unbind();
        this.secondText.textProperty().bind(Bindings.concat("Seconds: ", selectedSimulationDetails.getTimeProperty().asString()));

        if (selectedSimulationDetails.isRunning()){
            this.buttonVbox.getChildren().addAll(playButton,pauseButton,stopButton);
        }


    }

    private void setControlButtons() {
        playButton = new Button();
        pauseButton = new Button();
        stopButton = new Button();
        ImageView playimage = new ImageView(new Image(ButtonsImagePath.PLAY));
        playimage.setFitWidth(30);
        playimage.setFitHeight(30);
        ImageView stopimage = new ImageView(new Image(ButtonsImagePath.STOP));
        stopimage.setFitWidth(30);
        stopimage.setFitHeight(30);
        ImageView pauseimage = new ImageView(new Image(ButtonsImagePath.PAUSE));
        pauseimage.setFitWidth(30);
        pauseimage.setFitHeight(30);
        playButton.setGraphic(playimage);
        pauseButton.setGraphic(pauseimage);
        stopButton.setGraphic(stopimage);
    }


    public void addSimulationToTable(SimulationExecutionDto simulationExecutionDto) {
        simulationsData.add(simulationExecutionDto);
        executionListTable.refresh();
    }

    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

}
