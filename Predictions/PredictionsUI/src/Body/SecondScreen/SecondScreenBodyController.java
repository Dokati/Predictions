package Body.SecondScreen;

import Dto.EnvPropDto;
import PrimaryContreoller.PrimaryController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SecondScreenBodyController implements Initializable {

    PrimaryController primaryController;
    TextField numberTextField;
    TextField stringTextField;
    String activeEnvProp;
    Slider slider;
    Text currenSliderValue;
    ComboBox<String> trueOrFalseBox;
    @FXML
    private GridPane grid;
    @FXML
    private HBox hbox;
    @FXML
    private VBox vbox;
    private ListView<String> myListView;
    private Map<String,String> envPropValues;
    @FXML
    private Button enviormentVariablesButton;

    @FXML
    private Button entitypopulationButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        envPropValues = new HashMap<>();
        //-----------------------------//
        numberTextField = new TextField();
        numberTextField.setPrefWidth(200); // Change this to your desired width
        numberTextField.textProperty().addListener((observable, oldValue, newValue) -> envPropValues.put(activeEnvProp,newValue));
        makeNumberButtonNumericOnly();
        //-----------------------------//
        currenSliderValue = new Text();
        this.slider = new Slider();
        updateSliderCurrentValNPropVal();
        //-----------------------------//
        stringTextField = new TextField();
        //-----------------------------//
        this.trueOrFalseBox = new ComboBox<>();
        trueOrFalseBox.getItems().addAll("True", "False");
        trueOrFalseBox.setOnAction(event -> envPropValues.put(activeEnvProp, trueOrFalseBox.getValue()));
        //-----------------------------//
        this.stringTextField = new TextField();
        stringTextField.textProperty().addListener((observable, oldValue, newValue) -> envPropValues.put(activeEnvProp,newValue));
    }

    private void makeNumberButtonNumericOnly() {
        numberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                // Regular expression matches digits followed by an optional decimal point and digits
                numberTextField.setText(oldValue);
            }
        });


    }

    private void handleEnvPropListItemClick(String selectedEnvProp) {
        this.activeEnvProp = selectedEnvProp;
        this.vbox.getChildren().clear();
        EnvPropDto envPropDto = this.primaryController.getPredictionManager().getEnvProp(selectedEnvProp);
        Text text = createAskFromUserString(envPropDto.getType(), envPropDto.getName());

        String value = envPropValues.containsKey(activeEnvProp)? envPropValues.get(activeEnvProp): "";
        if(envPropDto.getType().equals("boolean")){
            this.trueOrFalseBox.setValue(value);
            this.vbox.getChildren().addAll(text,this.trueOrFalseBox);
        }
        if(envPropDto.getType().equals("float")){
            if(envPropDto.hasRange()){
                value = value.equals("")? envPropDto.getRange().getFrom().toString():envPropValues.get(activeEnvProp);
                this.slider.setValue(Double.parseDouble(value));
                this.currenSliderValue.setText("Value:" + this.slider.getValue());
                this.slider.setMin(envPropDto.getRange().getFrom());
                this.slider.setMax(envPropDto.getRange().getTo());

                this.vbox.getChildren().addAll(text,this.slider, currenSliderValue);
            }
            else {
                this.numberTextField.setText(value);
                this.vbox.getChildren().addAll(text,this.numberTextField);
            }
        }
        if(envPropDto.getType().equals("string")){
            this.stringTextField.setText(value);
            this.vbox.getChildren().addAll(text,this.stringTextField);
        }



    }

    private Text createAskFromUserString(String type, String name) {
        Text text = new Text();
        if(type.equals("float")){
            text.setText(("Please insert your desire float number for " + name));
        }
        if (type.equals("string")){
            text.setText(("Please insert your desire string for " + name));
        }
        if (type.equals("boolean")){
            text.setText(("Please choose your desire boolean value for " + name));
        }
        return text;
    }

    private void updateSliderCurrentValNPropVal() {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currenSliderValue.setText("Current: " + newValue);
                envPropValues.put(activeEnvProp,newValue.toString());
            }
        });
    }

    public void setEnvPropList(List<String> envPropDtoList) {

        this.myListView = new ListView<>();
        myListView.getItems().addAll(envPropDtoList);


        myListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Double-click
                String selectedItem = myListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Handle double-click event here
                    handleEnvPropListItemClick(selectedItem);
                }
            }
        });
    }

    @FXML
    void ShowEnvPropListOnClick(ActionEvent event) {
        this.hbox.getChildren().add(this.myListView);
    }
    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }
}
