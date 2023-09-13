package Body.SecondScreen;

import Dto.EnvPropDto;
import PrimaryContreoller.PrimaryController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private GridPane populationGridPane;
    @FXML
    private TableView<EnvPropTableItem> envPropTable;
    @FXML
    private HBox hbox;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox amountVbox;
    @FXML
    private Button runSimulationButton;

    private Map<String, String> envPropValues;
    Map<String, TextField> entityToPopTextFieldMap;
    int totalPopulation = 0;
    Text totalPopulationTextField;
    Text maxPopulationTexrField;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        envPropValues = new HashMap<>();
        //-----------------------------//
        numberTextField = new TextField();
        numberTextField.setPrefWidth(200); // Change this to your desired width
        numberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                envPropValues.put(activeEnvProp, newValue);
                updateCellValue(activeEnvProp,"Value", newValue);
            }

        });
        makeNumberButtonNumericOnly();
        //-----------------------------//
        currenSliderValue = new Text();
        this.slider = new Slider();
        this.slider.setOnMouseReleased(event -> {
            envPropValues.put(activeEnvProp, Double.toString(slider.getValue()));
            updateCellValue(activeEnvProp,"Value", Double.toString(slider.getValue()));});
        updateSliderCurrentValNPropVal();
        //-----------------------------//
        stringTextField = new TextField();
        //-----------------------------//
        this.trueOrFalseBox = new ComboBox<>();
        trueOrFalseBox.getItems().addAll("True", "False");
        trueOrFalseBox.setOnAction(event -> {
            if (trueOrFalseBox.getSelectionModel().getSelectedItem() != null) {
                envPropValues.put(activeEnvProp, trueOrFalseBox.getValue());
                updateCellValue(activeEnvProp, "Value", trueOrFalseBox.getValue());
            }
        });
        //-----------------------------//
        this.stringTextField = new TextField();
        stringTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                envPropValues.put(activeEnvProp, newValue);
                updateCellValue(activeEnvProp,"Value", newValue);
            }
        });
        //-----------------------------//
        entityToPopTextFieldMap = new HashMap<>();
        //-----------------------------//
        totalPopulationTextField = new Text("Total Population: ");
        maxPopulationTexrField = new Text("Max Population: ");
        this.amountVbox.getChildren().addAll(totalPopulationTextField,maxPopulationTexrField);
        //-----------------------------//
        TableColumn<EnvPropTableItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);
        TableColumn<EnvPropTableItem, Integer> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(65);
        TableColumn<EnvPropTableItem, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setPrefWidth(110);
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("Value"));
        this.envPropTable.getColumns().addAll(nameColumn, typeColumn, valueColumn);
        //-----------------------------//



    }

    private void updateCellValue(String name, String columnName, String newValue) {
        for (EnvPropTableItem item : this.envPropTable.getItems()) {
            if (item.getName().equalsIgnoreCase(name)) {
                if (columnName.equalsIgnoreCase("Value")) {
                    item.setValue(newValue);
                    break;
                }
            }
        }
        envPropTable.refresh();

    }
    private void makeNumberButtonNumericOnly() {
        numberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                // Regular expression matches digits followed by an optional decimal point and digits
                numberTextField.setText(oldValue);
            }
        });


    }

    public void setEnvPropTable() {
        List<EnvPropDto> envPropDtoList = this.primaryController.getPredictionManager().getAllEnvProps();
        ObservableList<EnvPropTableItem> data = FXCollections.observableArrayList();

        for (EnvPropDto envPropDto : envPropDtoList) {
            data.add(new EnvPropTableItem(envPropDto.getName(), envPropDto.getType(), null));
        }

        this.envPropTable.setItems(data);

        envPropTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                EnvPropTableItem selectedItem = envPropTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    handleEnvPropTableItemClick(selectedItem.getName());
                }
            }
        });

    }

    private void handleEnvPropTableItemClick(String selectedEnvProp) {
        this.activeEnvProp = selectedEnvProp;
        this.vbox.getChildren().clear();
        EnvPropDto envPropDto = this.primaryController.getPredictionManager().getEnvProp(selectedEnvProp);
        Text text = createAskFromUserString(envPropDto.getType(), envPropDto.getName());
        text.setWrappingWidth(260);

        String value = envPropValues.containsKey(activeEnvProp) ? envPropValues.get(activeEnvProp) : "";
        if (envPropDto.getType().equals("boolean")) {
            this.trueOrFalseBox.setValue(value);
            this.vbox.getChildren().addAll(text, this.trueOrFalseBox);
        }
        if (envPropDto.getType().equals("float")) {
            if (envPropDto.hasRange()) {
                this.slider.setMin(envPropDto.getRange().getFrom());
                this.slider.setMax(envPropDto.getRange().getTo());
                value = value.equals("") ? envPropDto.getRange().getFrom().toString() : envPropValues.get(activeEnvProp);
                double val = Double.parseDouble(value);
                this.slider.setValue(val);
                this.currenSliderValue.setText("Value:" + this.slider.getValue());

                this.vbox.getChildren().addAll(text, this.slider, currenSliderValue);
            } else {
                this.numberTextField.setText(value);
                this.vbox.getChildren().addAll(text, this.numberTextField);
            }
        }
        if (envPropDto.getType().equals("string")) {
            this.stringTextField.setText(value);
            this.vbox.getChildren().addAll(text, this.stringTextField);
        }


    }

    private Text createAskFromUserString(String type, String name) {
        Text text = new Text();
        if (type.equals("float")) {
            text.setText(("Please insert your desire float number for " + name));
        }
        if (type.equals("string")) {
            text.setText(("Please insert your desire string for " + name));
        }
        if (type.equals("boolean")) {
            text.setText(("Please choose your desire boolean value for " + name));
        }
        return text;
    }

    private void updateSliderCurrentValNPropVal() {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currenSliderValue.setText("Current: " + newValue);
            }
        });
    }

    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }

    ///////////------------------------------------------------------////////////////////
    ///////////----------Entities Section:---------------------------////////////////////



    public void setEntitiesPopulationList(List<String> entitiesNames, Integer populationSpace) {
        ObservableList<String> Entities = FXCollections.observableArrayList(entitiesNames);

        this.maxPopulationTexrField.setText("Max Population: " + populationSpace);

        int rowIndex = 0;
        for (String entity : Entities) {
            Text EntityText = new Text(entity);
            TextField populationTextField = new TextField();
            populationTextField.setPromptText("Population of " + entity);

            entityToPopTextFieldMap.put(entity, populationTextField);

            // Add a listener to each TextField to allow only integer input
            populationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    populationTextField.setText(oldValue);
                }
                else {//if its numeric number
                    updateTotalPopulationLabel();
                    if (this.totalPopulation>populationSpace) {
                        populationTextField.setText(oldValue);
                    }
                    updateTotalPopulationLabel();
                }


            });
            this.populationGridPane.add(EntityText, 0, rowIndex);
            this.populationGridPane.add(populationTextField, 1, rowIndex);

            rowIndex++;
        }

    }

    private void updateTotalPopulationLabel() {
        int totalPopulation = 0;
        for (TextField populationTextField : this.entityToPopTextFieldMap.values()) {
            if (!populationTextField.getText().isEmpty()) {
                totalPopulation += Integer.parseInt(populationTextField.getText());
            }
        }
        this.totalPopulationTextField.setText("Total Population: " + totalPopulation);

        this.totalPopulation = totalPopulation;
    }

    @FXML
    private void runSimulationOnClick(ActionEvent event) {
        // convert the Map<String, TextField> to Map<String, Integer>. if value is empty string, put 0 population.
        Map<String, Integer> entitiesPopulationMap = entityToPopTextFieldMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {return entry.getValue().getText().isEmpty() ? 0: Integer.parseInt(entry.getValue().getText());}));

        primaryController.jumpToResultTab();
        primaryController.runSimulation(entitiesPopulationMap, envPropValues);
    }

    @FXML
    void clearSecondsScreenOnClick(ActionEvent event) {
        this.envPropValues.keySet().forEach(key -> updateCellValue(key,"Value", null));
        clearSecondsScreenEnvPropPart();
        this.entityToPopTextFieldMap.values().forEach(TextInputControl::clear);
    }

    private void clearSecondsScreenEnvPropPart() {
        this.numberTextField.clear();
        this.trueOrFalseBox.getSelectionModel().clearSelection();
        this.slider.setValue(this.slider.getMin());
        this.stringTextField.clear();
        this.envPropValues.clear();
    }

    public void clearSecondScreen() {
        clearSecondsScreenEnvPropPart();
        this.vbox.getChildren().clear();/// clear the vbox in case loading new file.
        this.entityToPopTextFieldMap.clear();
        this.populationGridPane.getChildren().clear();

    }
}