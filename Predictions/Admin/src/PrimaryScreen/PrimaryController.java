package PrimaryScreen;

import Body.FirstScreen.FirstScreenBodyController;
import Dto.SimulationTitlesDetails;
import PrimaryContreoller.QueueManager;
import Screens.Management.ManagementController;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static Config.Configuration.BASE_URL;
import static Config.Configuration.HTTP_CLIENT;

public class PrimaryController implements Initializable {

    @FXML private GridPane managementScreen;
    @FXML private ManagementController managementScreenController;


    public QueueManager queueManager;
    public ExecutorService taskThreadPool;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        queueManager = new QueueManager();

        if (managementScreenController != null ) {
            managementScreenController.setMainController(this);
        }
    }

    public void loadSimulation(String FilePath) {
        SimulationTitlesDetails simulationTitleDto = null;
        try {
            Request request = createPostSimulationFileRequest(FilePath);
            Call call = HTTP_CLIENT.newCall(request);
            Response response = call.execute();// sending the file to the engine.
            simulationTitleDto = new Gson().fromJson(response.body().string(), SimulationTitlesDetails.class);
//            predictionManager.resetSimulationList();
            managementScreenController.getFilePathTextField().setText(FilePath);
            showSuccessDialog();
//            clearAllScreens();
            initFirstScrean(simulationTitleDto);
            taskThreadPool = Executors.newFixedThreadPool(simulationTitleDto.getThreadsCount());
            queueManager.setThreadPoolSize(simulationTitleDto.getThreadsCount());
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

    private Request createPostSimulationFileRequest(String filePath) throws IOException {
        String RESOURCE = "/loadSimulationServlet";
        String xmlContent = new String(Files.readAllBytes(new File(filePath).toPath()));

        RequestBody body = RequestBody.create(MediaType.parse("application/xml"), xmlContent);
        String url = BASE_URL + RESOURCE;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;

    }

    private Request simpleGet(String filePath) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        return request;

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
}
