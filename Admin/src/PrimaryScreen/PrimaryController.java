package PrimaryScreen;

import Dto.SimulationTitlesDetails;
import Screens.Allocations.AllocationController;
import Screens.Allocations.AllocationListenerTask;
import Screens.Management.ManagementController;
import Screens.Management.ManagementListenerTask;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import okhttp3.*;
import okio.Buffer;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.ExecutorService;

import static Request.RequestCreator.*;
import static Utils.Timer.TIMER_DELAY;
import static Utils.Timer.TIMER_SCEDULE_PERIOD;

public class PrimaryController implements Initializable {

    @FXML private GridPane managementScreen;
    @FXML private ManagementController managementScreenController;
    @FXML private ScrollPane AllocationScreen;
    @FXML private AllocationController AllocationScreenController;


    public QueueManager queueManager;
    public ExecutorService taskThreadPool;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkIfImTheOnlyAdmin();
        queueManager = new QueueManager();

        if (managementScreenController != null ) {
            managementScreenController.setMainController(this);
        }

        startListeners();
    }

    private void startListeners() {
        ManagementListenerTask managementListenerTask = new ManagementListenerTask(managementScreenController);
        Timer timer = new Timer();
        timer.schedule(managementListenerTask, TIMER_DELAY, TIMER_SCEDULE_PERIOD);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        AllocationListenerTask allocationListenerTask = new AllocationListenerTask(AllocationScreenController);
        Timer timer2 = new Timer();
        timer2.schedule(allocationListenerTask, TIMER_DELAY, TIMER_SCEDULE_PERIOD);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    }
    private void checkIfImTheOnlyAdmin()  {
        Request request = CreateGetRequest("/adminStatus");
        Response response = ExecuteRequest(request);
        if(response == null){
            showAlertToUser("Server is not responding, please try again later");
        }
        else{
            String responseBody = null;
            try {
                responseBody = response.body().string();
            } catch (IOException e) {
                showAlertToUser(e.getMessage());
            }
            if(responseBody.equals("{\"adminUp\": true}")){
                showAlertToUser("There is already an admin connected to the server, please try again later");
                System.exit(0);
            }
            else if (responseBody.equals("{\"adminUp\": false}")){
                updateServerAdminStatus();
            }
            else {
                showAlertToUser("An error occurred while trying to connect to the server, please try again later");
                System.exit(0);
            }
            response.body().close();
        }

    }

    private void updateServerAdminStatus() {
        Request request = CreateSingleParameterPostRequest("true", "adminStatus", "/adminStatus");
        ExecuteRequestAndHandleResponse(request);
    }


    public void loadSimulation(String FilePath) {
        SimulationTitlesDetails simulationTitleDto = null;
        try {
            Request request = createPostSimulationFileRequest(FilePath);
            Response response = ExecuteRequest(request); //sending the file to the engine.
            if(response.code() == 200) {
                simulationTitleDto = new Gson().fromJson(response.body().string(), SimulationTitlesDetails.class);
//            predictionManager.resetSimulationList();
                managementScreenController.getFilePathTextField().setText(FilePath);
                showSuccessDialog();
//            clearAllScreens();
               // initFirstScrean(simulationTitleDto);
//            taskThreadPool = Executors.newFixedThreadPool(simulationTitleDto.getThreadsCount());
//            queueManager.setThreadPoolSize(simulationTitleDto.getThreadsCount());
            }
            else{
                showAlertToUser(response.body().string());
            }
        }
        catch (IllegalArgumentException exception){
            showAlertToUser(exception.getMessage());
        }
        catch (RuntimeException e) {
            showAlertToUser("Loading the XML file failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initFirstScrean(SimulationTitlesDetails simulationTitleDto) {
        this.managementScreenController.SetTitleDetailsOnFirstScreen(simulationTitleDto);
    }

    private static String extractRequestBody(Request request) throws IOException {
        Request copy = request.newBuilder().build();
        Buffer buffer = new Buffer();
        copy.body().writeTo(buffer);
        return buffer.readUtf8();
    }




    private void showSuccessDialog() {
        String message = "File Loaded Successfully"+ " \uD83D\uDE04";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void setThreadPoolSize(int size) {
        queueManager.setThreadPoolSize(size);
    }
}
