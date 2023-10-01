package Screens.Allocations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

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
        data.add(new RequestItem("1"));
        data.add(new RequestItem("2"));
        RequestTable.setEditable(true);
        RequestTable.setItems(data);
        RequestTable.refresh();


    }
}
