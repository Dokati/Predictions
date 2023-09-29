package Rule;

import Context.*;
import Entity.definition.EntityDefinition;
import Entity.instance.EntityInstance;
import PRD.PRDAction;

import PRD.PRDRule;
import Property.definition.EnvPropertyDefinition;
import Rule.Action.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Rule {
    private String name;
    private Integer ticks = 1;
    private Double probability = 1.0;
    private ArrayList<Action> actions;

    public Rule(String name, Integer ticks, Double probability, ArrayList<Action> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public Rule(PRDRule ruleDef, HashMap<String, EntityDefinition> entities, HashMap<String, EnvPropertyDefinition> environmentProperties) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.name = ruleDef.getName();

        if(ruleDef.getPRDActivation()!= null){
            if(ruleDef.getPRDActivation().getTicks() != null){
                CheckTicks(ruleDef.getPRDActivation().getTicks());
                this.ticks = ruleDef.getPRDActivation().getTicks();
            }
            if(ruleDef.getPRDActivation().getProbability()!= null){
                CheckProbability(ruleDef.getPRDActivation().getProbability());
                this.probability = ruleDef.getPRDActivation().getProbability();
            }
        }

        this.actions = new ArrayList<>();
        for(PRDAction actionDef : ruleDef.getPRDActions().getPRDAction())
        {
            this.actions.add(ActionFactory.ActionCreator(actionDef,entities, environmentProperties));
        }
    }

    public void CheckTicks(Integer ticks)
    {
        if(ticks < 0)
        {
            throw new IllegalArgumentException("Invalid ticks. ticks for a rule cannot be " + ticks.toString());
        }
    }
    public void CheckProbability(Double probability)
    {
        if(probability < 0 || probability > 1)
        {
            throw new IllegalArgumentException("Invalid probability. Probability for a rule cannot be " + probability.toString());
        }
    }

    public boolean RuleIsRunnable(Integer simulationTick){
        if (this.ticks!=null && simulationTick % this.ticks != 0){
            return false;
        }

        return this.probability == null || !(new Random(1).nextDouble() > this.probability);
    }

    public void RunRule(Context context){
        for (Action action: this.actions) {
            if (action.getMainEntity().equals(context.getActiveEntityInstance().getEntityDef())) {

                if(action.IsSecondaryEntityExist()) {
                    for(EntityInstance secondaryEntity : findSuitableSecondaryEntities(context,action)){
                        if(action.getMainEntity().equals(context.getActiveEntityInstance().getEntityDef()) ||
                                action.getMainEntity().equals(secondaryEntity.getEntityDef())){
                            action.Activate(new ContextSecondaryEntity(context,secondaryEntity));
                        }
                    }
                }
                else {
                    action.Activate(context);
                }
            }
        }
    }

    public List<EntityInstance> findSuitableSecondaryEntities(Context context, Action action){

        List<EntityInstance> suitableSecondaryEntities = new ArrayList<>();

        for(EntityInstance entityInstance : context.getWorldInstance().getEntities()){
            if (entityInstance.getEntityDef().equals(action.getSecondaryEntity().getEntityDefinition())
            && action.getSecondaryEntity().getCondition().conditionIsTrue(new Context(entityInstance,context.getWorldInstance(),context.getCurrentTick()))
            && suitableSecondaryEntities.size() < action.getSecondaryEntity().getCount()) {
                suitableSecondaryEntities.add(entityInstance);
            }
        }

        return suitableSecondaryEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTicks() {
        return ticks;
    }

    public Double getProbability() {
        return probability;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        StringBuilder actionsStringBuilder = new StringBuilder();
        for(Action action : this.actions) {
            actionsStringBuilder.append(action);
        }

        return "Rule: " +
                "name: '" + name + '\'' +
                ", ticks: " + ticks +
                ", probability: " + probability +
                ", number of actions: " + actions.size() +
                ", actions list: " + actionsStringBuilder +
                "\n";
    }
}
