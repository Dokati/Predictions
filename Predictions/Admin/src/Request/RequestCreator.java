package Request;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static Config.Configuration.BASE_URL;
import static Config.Configuration.HTTP_CLIENT;

public class RequestCreator {

    public static Request createPostSimulationFileRequest(String filePath) throws IOException {
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

    public static Request CreateGetRequest(String resource) {
        String url = BASE_URL + resource;
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }
    public static Request CreateGetActionsRequest(String ruleName, String index) {
        String url = BASE_URL + "/getActionDetails";
        RequestBody requestBody = new FormBody.Builder()
                .add("ruleName", ruleName)
                .add("index", index)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    public static Response ExecuteRequest(Request request) throws IOException {
        try {
            Call call = HTTP_CLIENT.newCall(request);
            return call.execute();
        } catch (IOException e) {
            // Handle IOException here (e.g., log the error, throw a custom exception, or take appropriate action)
            showAlertToUser(e.getMessage()); // This prints the stack trace for debugging purposes
            return null; // Return an appropriate response or value in case of failure
        }
    }

    private static void showAlertToUser(String message) {
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
