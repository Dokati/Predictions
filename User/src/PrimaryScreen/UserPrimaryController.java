package PrimaryScreen;

import Screen.details.FirstScreenBodyController;
import Screens.DetailsScreen.DetailScreenListenerTask;
import Screens.Requests.RequestController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import static Request.RequestCreator.CreateUserNamePostRequest;
import static Request.RequestCreator.ExecuteRequest;
import static Utils.Timer.TIMER_DELAY;
import static Utils.Timer.TIMER_SCEDULE_PERIOD;

public class UserPrimaryController implements Initializable {

    private String userName;
    @FXML private Label nameLabel;
    @FXML private ScrollPane detailsComponent;
    @FXML private FirstScreenBodyController detailsComponentController;
    @FXML private ScrollPane requestsComponent;
    @FXML private RequestController requestComponentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logInPage();
        nameLabel.setText("Name: " + userName);
        startListeners();
        if(requestComponentController != null) {
            requestComponentController.setPrimaryController(this);
        }    }
    private void startListeners() {
        DetailScreenListenerTask detailScreenListenerTask = new DetailScreenListenerTask(this.detailsComponentController);
        Timer timer = new Timer();
        timer.schedule(detailScreenListenerTask, TIMER_DELAY, TIMER_SCEDULE_PERIOD);}

    private void logInPage() {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setTitle("Predictions Login");

        Label nameLabel = new Label("Hello, welcome to Predictions!\nPlease enter your name:");
        nameLabel.setFont(new javafx.scene.text.Font("System", 20));
        TextField nameField = new TextField();
        Button loginButton = new Button("Login");
        // Create a label to display validation messages
        Label validationLabel = new Label();

        loginButton.setOnAction(e -> {
            String inputName = nameField.getText();
            // Call a method to validate the name (you can implement this method)
            if(inputName.isEmpty()) {
                validationLabel.setText("Please enter your name.");
                return;
            }
            else{
                try {
                    boolean isValid = validateName(inputName);


                    if (isValid) {
                        userName = inputName;
                        loginStage.close();
                    } else {
                        validationLabel.setText("Invalid name. Please try again.");
                    }
                }catch (Exception ex){
                    validationLabel.setText("Server is unreachable.");
                }
            }
        });
        loginStage.setOnCloseRequest(event -> {
            // Terminate the JavaFX application when the user closes the login window
            System.exit(0);
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(nameLabel, nameField, loginButton, validationLabel);
        vbox.setSpacing(20);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(vbox, 450, 250);
        loginStage.setScene(scene);

        loginStage.showAndWait();

        if (userName != null) {
            System.out.println("User Name: " + userName);
        }
    }
    private boolean validateName(String name) {

        Boolean isValid = false;
        Request request = CreateUserNamePostRequest(name);
        Response response = ExecuteRequest(request);
        if (response == null) {
            throw new RuntimeException("server is unreachable");}
        if(response.code() == 200) {
            isValid = true;
        }

        // Return true if the name is valid, false otherwise
        return isValid; // This is a simple example; you can replace it with your logic
    }

    public String getUserName() {
        return userName;
    }
}


