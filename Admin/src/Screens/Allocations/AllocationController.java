package Screens.Allocations;

import Dto.AdminRequestDto;
import Screens.DetailsScreen.DetailScreenListenerTask;
import Screens.Requests.RequestListenerTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.stream.Collectors;

import static Utils.Timer.TIMER_DELAY;
import static Utils.Timer.TIMER_SCEDULE_PERIOD;

public class AllocationController implements Initializable {

    @FXML
    private TableView<RequestItem> RequestTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RequestTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        RequestTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        RequestTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("userName"));
        RequestTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("reqNumOfRuns"));
        RequestTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("terminationConditiom"));
        RequestTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("status"));
        RequestTable.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("runningSimulations"));
        RequestTable.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("finishedSimulations"));
        RequestTable.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("approveButton"));
        RequestTable.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("rejectButton"));

        ObservableList<RequestItem> data = FXCollections.observableArrayList();
//        data.add(new RequestItem("1"));
//        data.add(new RequestItem("2"));
        RequestTable.setEditable(true);
        RequestTable.setItems(data);
        RequestTable.refresh();

        startListeners();

    }

    private void startListeners() {
        AllocationListenerTask allocationListenerTask = new AllocationListenerTask(this);
        Timer timer = new Timer();
        timer.schedule(allocationListenerTask, TIMER_DELAY, TIMER_SCEDULE_PERIOD);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    public void setCurrentAllocationRequests(List<AdminRequestDto> requests) {
        List<String> IdList = RequestTable.getItems().stream().map(RequestItem::getId).collect(Collectors.toList());
        ObservableList<RequestItem> data = FXCollections.observableArrayList();
        for (AdminRequestDto request : requests) {
            if (!IdList.contains(request.getId())) {
                 data.add(new RequestItem(request));
            }
        }
        RequestTable.setItems(data);
        RequestTable.refresh();

    }
}
