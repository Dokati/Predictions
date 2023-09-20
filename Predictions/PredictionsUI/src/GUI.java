import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Predictions");
        Parent load = FXMLLoader.load(getClass().getResource("PrimaryContreoller/primaryController.fxml"));
        Scene scene = new Scene(load, 1050 , 702);
        primaryStage.setScene(scene);
//       primaryStage.setMinWidth(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
