package Body.ThirdScreen;
import Dto.SimulationExecutionDto;
import Paths.ButtonsImagePath;
import PrimaryContreoller.PrimaryController;
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


import java.net.URL;
import java.util.ResourceBundle;

public class ThirdScreenBodyController implements Initializable {

    @FXML
    private TableView<SimulationExecutionDto> executionListTable;
    @FXML
    private HBox detailsHbox;

    @FXML
    private VBox buttonVbox;
    ObservableList<SimulationExecutionDto> data;
    private PrimaryController primaryController;

    Button playButton;
    Button pauseButton;
    Button stopButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executionListTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        executionListTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("status"));
        data = FXCollections.observableArrayList();
        executionListTable.setItems(data);
        setControlButtons();
        this.buttonVbox.getChildren().addAll(playButton,pauseButton,stopButton);


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

    public void addSimulationToTable(String id, String status){
        data.add(new SimulationExecutionDto(id, status));
        executionListTable.refresh();
    }
    public void addSimulationToTable(SimulationExecutionDto simulationExecutionDto) {
        data.add(simulationExecutionDto);
        executionListTable.refresh();
    }


    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

}
