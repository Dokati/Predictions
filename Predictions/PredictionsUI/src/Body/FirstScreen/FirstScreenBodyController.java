package Body.FirstScreen;

import Dto.*;
import PrimaryContreoller.PrimaryController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;


import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FirstScreenBodyController implements Initializable {

    PrimaryController primaryController;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
    @FXML
    private TreeView<String> TreeView;
    @FXML
    private TilePane tilePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MyTreeItem rootItem = new MyTreeItem("World Details","World Details");
        MyTreeItem entitiesItem = new MyTreeItem("Entities","Entities");
        MyTreeItem rulesItem = new MyTreeItem("Rules","Rules");
        MyTreeItem terminationItem = new MyTreeItem("Termination","Termination");
        MyTreeItem envItem = new MyTreeItem("Enviorment Variables","Enviorment Variables");
        MyTreeItem gridItem = new MyTreeItem("Grid","Grid");

        this.TreeView.setRoot(rootItem);
        rootItem.getChildren().addAll(entitiesItem, rulesItem, terminationItem, envItem, gridItem);

    }

    
    public void setTitleDetail(SimulationTitlesDetails simulationTitleDto){
        // insert Entities Names:
        this.TreeView.getRoot().getChildren().get(0).getChildren().addAll(simulationTitleDto.getEntitiesNames().stream()
                .map(entityName -> new MyTreeItem(entityName, "Entity name")).collect(Collectors.toList()));

        // insert Rules name, activation and actions tree
        simulationTitleDto.getRulesTitleDto().stream().forEach(ruleDto -> {
            MyTreeItem ruleItem = new MyTreeItem(ruleDto.getName(), "Rule Name");
            MyTreeItem activationItem = new MyTreeItem("Activation" ,"Activation");
            MyTreeItem actionsItem = new MyTreeItem("Actions" ,"Actions");
            // insert actions names
            int[] actionIndex = {0};
            ruleDto.getActionNames().forEach(action -> {
                MyTreeItem actionItem = new MyActionTreeItem(action, "Action",actionIndex[0]);
                actionIndex[0]++;
                actionsItem.getChildren().add(actionItem);
            });

            ruleItem.getChildren().addAll(activationItem,actionsItem);
            this.TreeView.getRoot().getChildren().get(1).getChildren().add(ruleItem);
        });

        //insert enviorment variable name:
        this.TreeView.getRoot().getChildren().get(3).getChildren().addAll(simulationTitleDto.getEnvVariableNames().stream()
                .map(envName -> new MyTreeItem(envName, "Env name")).collect(Collectors.toList()));


        this.TreeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && newValue.isLeaf()){
                        if(((MyTreeItem)newValue).getName().equals("Entity name")) setEntityPropertiesDetails(newValue.getValue());
                        if(((MyTreeItem)newValue).getName().equals("Activation")) setRuleActivaionDetails(newValue.getParent().getValue());
                        if(((MyTreeItem)newValue).getName().equals("Termination")) setTerminationDetails();
                        if(((MyTreeItem)newValue).getName().equals("Env name")) setEnvVariablesDetails(newValue.getValue());
                        if(((MyTreeItem)newValue).getName().equals("Action")) setActionDetails(newValue, newValue.getParent().getParent().getValue());
                        if(((MyTreeItem)newValue).getName().equals("Grid")) setGridDetails();
                    }
                });



    }

    private void setGridDetails() {
        this.tilePane.getChildren().clear();
        GridDto Grid = primaryController.getPredictionManager().getGridDetails();
        DetailLabel label = new DetailLabel("Grid details:\n\nRows: "+Grid.getRows() + "\nCulomns: " + Grid.getCulomns());
        this.tilePane.getChildren().add(label);
    }

    private void setActionDetails(TreeItem<String> action, String ruleName) {
        this.tilePane.getChildren().clear();
        ActionDetailsDto actionDetailsDto = primaryController.getPredictionManager().getActionDetails(((MyActionTreeItem)action).getactionIndex(), ruleName);
        DetailLabel label = new DetailLabel(actionDetailsDto.getActionDetails());
        this.tilePane.getChildren().add(label);

    }

    private void setEnvVariablesDetails(String envName) {
        this.tilePane.getChildren().clear();
        EnviormentVariablesDto enviormentVariablesDto = primaryController.getPredictionManager().getEnvVariableDetails();
        EnvVariableDto envVariableDto = enviormentVariablesDto.getEnvVariableDtoList().stream().filter(env -> env.getName().equals(envName)).findFirst().get();
        DetailLabel label = new DetailLabel("Env-variable deatils:\n\nName: " + envVariableDto.getName() + "\n"+"Type: " +envVariableDto.getType()+ "\n");
        if(envVariableDto.hasRange()) label.setText(label.getText()+ "Range: " + envVariableDto.getRange());
        this.tilePane.getChildren().add(label);
    }

    private void setTerminationDetails() {
        this.tilePane.getChildren().clear();
        TerminationDto terminationDto = primaryController.getPredictionManager().getTerminationDetails();
        DetailLabel label = new DetailLabel("Termination by:\n");
        for(Map.Entry<String ,Integer> entry : terminationDto.getTerminations().entrySet()){
            if(entry.getKey()!= "BYUSER"){
                label.setText(label.getText()+"\n"+ entry.getKey()+": " + entry.getValue().toString()+".\n");
            }
            else label.setText(label.getText() + "\nUser Choice.");
        }
        this.tilePane.getChildren().add(label);
    }

    private void setRuleActivaionDetails(String ruleName) {
        this.tilePane.getChildren().clear();
        RuleActivationDto activationDto = primaryController.getPredictionManager().getActivationDetails(ruleName);
        DetailLabel label = new DetailLabel("Activation terms:\nTick: "+activationDto.getTick()+"\nProbability: "+activationDto.getProbability());
        this.tilePane.getChildren().add(label);
    }

    private void setEntityPropertiesDetails(String value) {
        this.tilePane.getChildren().clear();
        EntityPropertyDetailDto entityPropertiesDetail =  primaryController.getPredictionManager().getEntityPropertiesDetail(value);

        for(String propDetail : entityPropertiesDetail.getPropertiesDetails()){
            DetailLabel label = new DetailLabel(propDetail);
            this.tilePane.getChildren().add(label);
        }
    }


    public void setMainController(PrimaryController primaryController) {
        this.primaryController = primaryController;
    }


    public void clearFirstScren() {
        this.TreeView.getRoot().getChildren().get(0).getChildren().clear();
        this.TreeView.getRoot().getChildren().get(1).getChildren().clear();
        this.TreeView.getRoot().getChildren().get(3).getChildren().clear();
        tilePane.getChildren().clear();
    }
}
