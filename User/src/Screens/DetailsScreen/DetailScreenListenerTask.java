package Screens.DetailsScreen;

import Dto.GridDto;
import Dto.SimulationTitlesDetails;
import PrimaryScreen.UserPrimaryController;
import Screen.details.FirstScreenBodyController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static Request.RequestCreator.CreateGetRequest;
import static Request.RequestCreator.ExecuteRequest;

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
                this.firstScreenBodyController.SetSeveralWorldsDetails(simulationTitlesDetailsList);
                List<String> simulationNames = simulationTitlesDetailsList.stream()
                        .map(SimulationTitlesDetails::getSimulationName)
                        .collect(Collectors.toList());
                this.userPrimaryController.setSimulationNames(simulationNames);
            }
        }

        System.out.println("DetailsScreenListenerTask");

    }

    private List<SimulationTitlesDetails> getFromServerAllWorldsDetails() {
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


    private Integer askServerForNumberOfWorlds() {
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
}
