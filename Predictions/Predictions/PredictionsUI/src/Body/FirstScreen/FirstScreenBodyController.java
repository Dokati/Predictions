package Body.FirstScreen;

import Dto.*;
import PrimaryContreoller.PrimaryController;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static Request.RequestCreator.*;

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
        this.TreeView.setRoot(rootItem);
    }

    
    public void setTitleDetail(SimulationTitlesDetails simulationTitleDto){
        MyTreeItem newWorld  = new MyTreeItem("World Name: " + simulationTitleDto.getWorldName(), "World Name");
        this.TreeView.getRoot().getChildren().add(newWorld);
        addToNewWorldBasicChildren(newWorld);
        // insert Entities Names:
        newWorld.getChildren().get(0).getChildren().addAll(simulationTitleDto.getEntitiesNames().stream()
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
            newWorld.getChildren().get(1).getChildren().add(ruleItem);
        });

        //insert enviorment variable name:
        newWorld.getChildren().get(3).getChildren().addAll(simulationTitleDto.getEnvVariableNames().stream()
                .map(envName -> new MyTreeItem(envName, "Env name")).collect(Collectors.toList()));


        this.TreeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && newValue.isLeaf()){
                        if(((MyTreeItem)newValue).getName().equals("Entity name")) {try {setEntityPropertiesDetails(newValue.getValue());} catch (IOException e)
                        {throw new RuntimeException(e);}}
                        if(((MyTreeItem)newValue).getName().equals("Activation")) {try {setRuleActivaionDetails(newValue.getParent().getValue());} catch (IOException e) {
                            throw new RuntimeException(e);}}
                        if(((MyTreeItem)newValue).getName().equals("Termination")) {
                            try {setTerminationDetails();} catch (IOException e) {throw new RuntimeException(e);}}
                        if(((MyTreeItem)newValue).getName().equals("Env name")) {
                            try {setEnvVariablesDetails(newValue.getValue());} catch (IOException e) {throw new RuntimeException(e);}}
                        if(((MyTreeItem)newValue).getName().equals("Action")) {
                            try {setActionDetails(newValue, newValue.getParent().getParent().getValue());} catch (IOException e) {throw new RuntimeException(e);}}
                        if(((MyTreeItem)newValue).getName().equals("Grid")) {try {setGridDetails();} catch (IOException e) {throw new RuntimeException(e);}
                        }
                    }
                });



    }

    private void addToNewWorldBasicChildren(MyTreeItem newWorld) {

        MyTreeItem entitiesItem = new MyTreeItem("Entities","Entities");
        MyTreeItem rulesItem = new MyTreeItem("Rules","Rules");
        MyTreeItem terminationItem = new MyTreeItem("Termination","Termination");
        MyTreeItem envItem = new MyTreeItem("Enviorment Variables","Enviorment Variables");
        MyTreeItem gridItem = new MyTreeItem("Grid","Grid");

        newWorld.getChildren().addAll(entitiesItem,rulesItem,terminationItem,envItem,gridItem);
    }

    private void setGridDetails() throws IOException {
        this.tilePane.getChildren().clear();
        Request request = CreateGetRequest("/getGridDetails");
        Response response = ExecuteRequest(request);
        if(response!=null) {
            GridDto Grid = new Gson().fromJson(response.body().string(), GridDto.class);
            DetailLabel label = new DetailLabel("Grid details:\n\nRows: " + Grid.getRows() + "\nCulomns: " + Grid.getCulomns());
            this.tilePane.getChildren().add(label);
        }
    }

    private void setActionDetails(TreeItem<String> action, String ruleName) throws IOException {
        this.tilePane.getChildren().clear();
        int Index = ((MyActionTreeItem)action).getactionIndex();
        String stringIndex = Integer.toString(Index);
        Request request = CreatePostActionsRequest(ruleName, stringIndex);
        Response response = ExecuteRequest(request);
        if(response!=null) {
            ActionDetailsDto actionDetailsDto = new Gson().fromJson(response.body().string(), ActionDetailsDto.class);
            DetailLabel label = new DetailLabel(actionDetailsDto.getActionDetails());
            this.tilePane.getChildren().add(label);
        }

    }

    private void setEnvVariablesDetails(String envName) throws IOException {
        this.tilePane.getChildren().clear();
        Request request = CreateGetRequest("/getEnvVariableDetails");
        Response response = ExecuteRequest(request);
        if (response == null) return;
        EnviormentVariablesDto enviormentVariablesDto = new Gson().fromJson(response.body().string(), EnviormentVariablesDto.class);
        EnvVariableDto envVariableDto = enviormentVariablesDto.getEnvVariableDtoList().stream().filter(env -> env.getName().equals(envName)).findFirst().get();
        DetailLabel label = new DetailLabel("Env-variable deatils:\n\nName: " + envVariableDto.getName() + "\n"+"Type: " +envVariableDto.getType()+ "\n");
        if(envVariableDto.hasRange()) label.setText(label.getText()+ "Range: " + envVariableDto.getRange());
        this.tilePane.getChildren().add(label);
    }

    private void setTerminationDetails() throws IOException {
        this.tilePane.getChildren().clear();
        Request request = CreateGetRequest("/getTerminationDetails");
        Response response = ExecuteRequest(request);
        if (response == null) return;
        TerminationDto terminationDto = new Gson().fromJson(response.body().string(), TerminationDto.class);
        DetailLabel label = new DetailLabel("Termination by:\n");
        for(Map.Entry<String ,Integer> entry : terminationDto.getTerminations().entrySet()){
            if(entry.getKey()!= "BYUSER"){
                label.setText(label.getText()+"\n"+ entry.getKey()+": " + entry.getValue().toString()+".\n");
            }
            else label.setText(label.getText() + "\nUser Choice.");
        }
        this.tilePane.getChildren().add(label);
    }

    private void setRuleActivaionDetails(String ruleName) throws IOException {
        this.tilePane.getChildren().clear();
        Request request =  CreateRuleActivaionRequest(ruleName);
        Response response = ExecuteRequest(request);
        if (response == null) return;
        RuleActivationDto activationDto = new Gson().fromJson(response.body().string(), RuleActivationDto.class);
        DetailLabel label = new DetailLabel("Activation terms:\nTick: "+activationDto.getTick()+"\nProbability: "+activationDto.getProbability());
        this.tilePane.getChildren().add(label);
    }

    private void setEntityPropertiesDetails(String entityName) throws IOException {
        this.tilePane.getChildren().clear();
        Request request =  CreateEntityActionsRequest(entityName);
        Response response = ExecuteRequest(request);
        if (response == null) return;
        EntityPropertyDetailDto entityPropertiesDetail =  new Gson().fromJson(response.body().string(), EntityPropertyDetailDto.class);
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
