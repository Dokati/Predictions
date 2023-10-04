import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;

import java.util.logging.Level;
import java.util.logging.Logger;


public class AdminMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
        primaryStage.setTitle("Predictions");
        Parent load = FXMLLoader.load(getClass().getResource("PrimaryScreen/PrimaryScreen.fxml"));
        Scene scene = new Scene(load, 1300 , 702);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("Hello world!");
    }
    public static void main(String[] args) {
        launch(args);
    }

}
