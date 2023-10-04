package Screens.Management;

import Dto.SimulationTitlesDetails;
import javafx.application.Platform;

import java.util.List;
import java.util.TimerTask;

import static Request.RequestCreator.askServerForNumberOfWorlds;
import static Request.RequestCreator.getFromServerAllWorldsDetails;

public class ManagementListenerTask extends TimerTask {
    ManagementController managementController;

    public ManagementListenerTask(ManagementController managementController) {
        this.managementController = managementController;
    }

    @Override
    public void run() {
        Integer numberOfWorlds = askServerForNumberOfWorlds();
        if (numberOfWorlds != null) {
            if (numberOfWorlds > managementController.getDetailsComponentController().getNumberOfWorlds()) {
                List<SimulationTitlesDetails> simulationTitlesDetailsList = getFromServerAllWorldsDetails();
                Platform.runLater(() -> {
                    this.managementController.getDetailsComponentController().SetSeveralWorldsDetails(simulationTitlesDetailsList);
                });

            }
        }
    }
}
