package Screens.DetailsScreen;

import Dto.GridDto;
import Dto.SimulationTitlesDetails;
import PrimaryScreen.UserPrimaryController;
import Screen.details.FirstScreenBodyController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static Request.RequestCreator.*;

public class DetailScreenListenerTask extends TimerTask {
    FirstScreenBodyController firstScreenBodyController;
    UserPrimaryController userPrimaryController;

    public DetailScreenListenerTask(FirstScreenBodyController firstScreenBodyController, UserPrimaryController userPrimaryController) {
        this.firstScreenBodyController = firstScreenBodyController;
        this.userPrimaryController = userPrimaryController;
    }

    @Override
    public void run() {

        Integer numberOfWorlds = askServerForNumberOfWorlds();
        if (numberOfWorlds != null) {
            if (numberOfWorlds > firstScreenBodyController.getNumberOfWorlds()) {
                List<SimulationTitlesDetails> simulationTitlesDetailsList = getFromServerAllWorldsDetails();
                Platform.runLater(() -> {
                    this.firstScreenBodyController.SetSeveralWorldsDetails(simulationTitlesDetailsList);
                });
                List<String> simulationNames = simulationTitlesDetailsList.stream()
                        .map(SimulationTitlesDetails::getSimulationName)
                        .collect(Collectors.toList());
                Platform.runLater(() -> {
                    this.userPrimaryController.setSimulationNames(simulationNames);
                });
            }
        }

        System.out.println("DetailsScreenListenerTask");

    }




}
