package Screens.Requests;
import Dto.RequestDto;
import PrimaryScreen.UserPrimaryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static Request.RequestCreator.ExecuteRequest;
import static Request.RequestCreator.createPostRequestWithDto;

public class RequestController implements Initializable {
    UserPrimaryController userPrimaryController;
    @FXML
    private TableView<SimulationRequest> requestsTable;
    TextField SecondsTextField;
    @FXML private Button submitButton;
    @FXML private Label SubmitNoteLabel;

    private void sendRequestToServer(RequestDto requestDto) {
        Request request = createPostRequestWithDto("/userRequestsList", requestDto);
        Response response = ExecuteRequest(request);
        if (response!=null && response.isSuccessful()) {
            SubmitNoteLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            SubmitNoteLabel.setText("Request sent successfully");
        } else {
            SubmitNoteLabel.setTextFill(javafx.scene.paint.Color.RED);
            SubmitNoteLabel.setText("Request failed");
        }



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<SimulationRequest> data = FXCollections.observableArrayList(
                new SimulationRequest()
);

        requestsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        requestsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("requestedRuns"));
        requestsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("terminationCondition"));
        requestsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("tick"));
        requestsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("seconds"));

        requestsTable.setEditable(true);
        requestsTable.setItems(data);
    }

    @FXML void submitButtonOnClick(ActionEvent event) {
        SimulationRequest simulationRequest = requestsTable.getItems().get(0);

        if (requestFieldsHaveBeenFilledPropely()) {
            System.out.println("Please fill all the fields");
        } else {
            HashMap<String, String> terminationConditionMap = getTerminationHashMap(simulationRequest);
            RequestDto requestDto = new RequestDto(simulationRequest.getSimulationName().getValue().toString(),
                    Integer.parseInt(simulationRequest.getRequestedRuns().getText()),
                    terminationConditionMap,
                    this.userPrimaryController.getUserName());

            sendRequestToServer(requestDto);
        }

    }

    private boolean requestFieldsHaveBeenFilledPropely() {
        SimulationRequest simulationRequest = requestsTable.getItems().get(0);
        boolean SimulationNameComboBoxIsEmpty = simulationRequest.getSimulationName().getValue() == null;
        boolean TerminationComboBoxIsEmpty = simulationRequest.getTerminationCondition().getValue() == null;
        boolean amountOfRunsTextFieldIsEmpty = simulationRequest.getRequestedRuns().getText().isEmpty();
        boolean TickTextFieldIsEmpty = simulationRequest.getTick().getText().isEmpty() && !simulationRequest.getTick().isDisabled();
        boolean SecondsTextFieldIsEmpty = simulationRequest.getSeconds().getText().isEmpty() && !simulationRequest.getSeconds().isDisabled();
        return SimulationNameComboBoxIsEmpty || TerminationComboBoxIsEmpty || amountOfRunsTextFieldIsEmpty || TickTextFieldIsEmpty || SecondsTextFieldIsEmpty;
    }

    private static HashMap<String, String> getTerminationHashMap(SimulationRequest simulationRequest) {
        HashMap<String,String> terminationConditionMap = new HashMap<>();
        if (simulationRequest.getTerminationCondition().getValue().equals("Tick")) {
            terminationConditionMap.put("Tick", simulationRequest.getTick().getText());
        } else if (simulationRequest.getTerminationCondition().getValue().equals("Seconds")) {
            terminationConditionMap.put("Seconds", simulationRequest.getSeconds().getText());
        } else if (simulationRequest.getTerminationCondition().getValue().equals("Tick/Seconds")) {
            terminationConditionMap.put("Tick", simulationRequest.getTick().getText());
            terminationConditionMap.put("Seconds", simulationRequest.getSeconds().getText());
        } else {
            terminationConditionMap.put("UserChoise", "UserChoise");
        }
        return terminationConditionMap;
    }

    public void setPrimaryController(UserPrimaryController userPrimaryController) {
        this.userPrimaryController = userPrimaryController;
    }

    public void setSimulationNames(List<String> simulationNames) {
        SimulationRequest simulationRequest = requestsTable.getItems().get(0);
        simulationRequest.getSimulationName().getItems().clear();
        simulationRequest.getSimulationName().getItems().addAll(simulationNames);
    }
}
