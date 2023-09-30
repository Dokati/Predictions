package Screens.Requests;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SimulationRequest {
    private final ComboBox simulationName;
    private final TextField requestedRuns;
    private final ComboBox terminationCondition;
    private final TextField tick;
    private final TextField seconds;

    public SimulationRequest() {
        this.tick = new TextField();
        tick.setDisable(true);
        this.seconds = new TextField();
        seconds.setDisable(false);
        seconds.setDisable(true);

        this.simulationName = new ComboBox();
        simulationName.setPrefWidth(170);
        this.requestedRuns = new TextField();
        this.terminationCondition = new ComboBox();
        terminationCondition.getItems().addAll("UserChoise", "Tick", "Seconds", "Tick/Seconds");
        terminationCondition.setValue("UserChoise");

        terminationCondition.setOnAction(event -> {
            if (terminationCondition.getValue().equals("Tick")) {
                tick.setDisable(false);
                seconds.setDisable(true);
            } else if (terminationCondition.getValue().equals("Seconds")) {
                tick.setDisable(true);
                seconds.setDisable(false);
            } else if (terminationCondition.getValue().equals("Tick/Seconds")) {
                tick.setDisable(false);
                seconds.setDisable(false);
            } else {
                tick.setDisable(true);
                seconds.setDisable(true);
            }
        });

        makeTextFieldNumericOnly(requestedRuns);
        makeTextFieldNumericOnly(tick);
        makeTextFieldNumericOnly(seconds);

    }

    public ComboBox getSimulationName() {
        return simulationName;
    }

    public TextField getRequestedRuns() {
        return requestedRuns;
    }

    public ComboBox getTerminationCondition() {
        return terminationCondition;
    }

    public TextField getTick() {
        return tick;
    }

    public TextField getSeconds() {
        return seconds;
    }

    private void makeTextFieldNumericOnly(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // Regular expression matches digits followed by an optional decimal point and digits
                textField.setText(oldValue);
            }
        });
    }
}
