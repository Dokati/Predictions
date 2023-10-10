package Request;

import Dto.BaseDto;
import Dto.SimulationTitlesDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static Config.Configuration.BASE_URL;
import static Config.Configuration.HTTP_CLIENT;

public class RequestCreator {

    public static Request createPostSimulationFileRequest(String filePath) throws IOException {
        String RESOURCE = "/addSimulationToListServlet";
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
    public static Request CreatePostActionsRequest(String ruleName, String index, String worldDefName) {
        String url = BASE_URL + "/getActionDetails";
        RequestBody requestBody = new FormBody.Builder()
                .add("ruleName", ruleName)
                .add("index", index)
                .add("worldDefName", worldDefName)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }


    public static Request CreateThreadPoolSizePostRequest(String size) {
        return  CreateSingleParameterPostRequest(size, "threadPoolSize", "/setThreadPoolSize");
    }
    public static Request CreateUserNamePostRequest(String userName) {
        return  CreateSingleParameterPostRequest(userName, "user name", "/UserList");
    }
    public static Request CreateEntityActionsRequest(String entityName, String worldName) {
         return   CreateSTwoParameterPostRequest(entityName, "entityName",
                 worldName,"worldDefName"  ,"/getEntityProperties");
    }
    public static Request CreateRuleActivaionRequest(String ruleName, String worldName) {
        return   CreateSTwoParameterPostRequest(ruleName, "ruleName",
                worldName,"worldDefName"  ,"/getRuleActivation");
    }
    public static Request CreateSingleParameterPostRequest(String value, String parameterName , String resource) {
        String url = BASE_URL + resource;
        RequestBody requestBody = new FormBody.Builder()
                .add(parameterName, value)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }
    public static Request CreateSTwoParameterPostRequest(String value1, String parameterName1 ,
                                                         String value2, String parameterName2, String resource) {

        String url = BASE_URL + resource;
        RequestBody requestBody = new FormBody.Builder()
                .add(parameterName1, value1)
                .add(parameterName2, value2)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    public  static  Request createGetListRequestsFromServer(String name){
        return CreateSingleParameterGetRequest(name, "user name","/userRequestsList");

    }
    public static Request CreateSingleParameterGetRequest(String value, String parameterName , String resource) {
        // Create a URL with query parameters
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + resource).newBuilder();
        urlBuilder.addQueryParameter(parameterName, value);
        String url = urlBuilder.build().toString();

        // Create a GET request
        Request request = new Request.Builder()
                .url(url)
                .get() // Specify it's a GET request
                .build();

        return request;
    }
    public static Request createPostRequestWithDto(String resource, BaseDto dto) {
        String url = BASE_URL + resource;
        // Serialize the DTO to JSON using Gson
        Gson gson = new Gson();
        String json = gson.toJson(dto);
        // Create a request body with the JSON data
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        // Create the POST request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return request;
    }

    public static Response ExecuteRequest(Request request){
        try {
            Call call = HTTP_CLIENT.newCall(request);
            return call.execute();
        } catch (IOException e) {
            // Handle IOException here (e.g., log the error, throw a custom exception, or take appropriate action)
            Platform.runLater(()->showAlertToUser(e.getMessage())); // This prints the stack trace for debugging purposes
            return null; // Return an appropriate response or value in case of failure
        }
    }
    public static void ExecuteRequestAndHandleResponse(Request request) {
        Response response = ExecuteRequest(request);
        if(response == null || response.code() != 200 || !response.isSuccessful()){
            showAlertToUser("An error occurred while trying to connect to the server, please try again later");
        }
        if(response != null) {
            response.body().close();
        }

    }

    public static List<SimulationTitlesDetails> getFromServerAllWorldsDetails() {
        Request request = CreateGetRequest("/getAllSimulationsDetails");
        Response response = ExecuteRequest(request);
        if (response != null) {
            try {
                Type simulationListType = new TypeToken<List<SimulationTitlesDetails>>() {}.getType();
                List<SimulationTitlesDetails> SimulationTitlesDetailsList = new Gson().fromJson(response.body().string(), simulationListType);
                return SimulationTitlesDetailsList;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }

    public static Integer askServerForNumberOfWorlds() {
        Integer res = null;
        Request request = CreateGetRequest("/getNumOfSimulations");
        Response response = ExecuteRequest(request);
        if (response != null) {
            try {
                res= Integer.parseInt(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
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
