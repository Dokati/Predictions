package Body.ThirdScreen;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;

import java.util.Map;
import java.util.function.Consumer;

public class UiAdapter {

    private Runnable incrementTicks;
    private Consumer<Map<String, Integer>> updateEntityPopulation;


    public UiAdapter(Runnable incrementTicks, Consumer<Map<String, Integer>> updateEntityPopulation) {
        this.incrementTicks = incrementTicks;
        this.updateEntityPopulation = updateEntityPopulation;
    }

public void IncrementTick(){
    Platform.runLater(()->this.incrementTicks.run());
}

public void UpdateEntitiesPopulation(Map<String, Integer> entitiesPopulation){
        Platform.runLater(()->{updateEntityPopulation.accept(entitiesPopulation);});
}
}
