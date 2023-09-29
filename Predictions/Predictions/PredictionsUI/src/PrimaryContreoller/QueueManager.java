package PrimaryContreoller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class QueueManager {
    IntegerProperty runningSimulations;
    IntegerProperty waitingSimulations;
    IntegerProperty finishedSimulations;
    IntegerProperty threadPoolSize;
    public QueueManager(){
        runningSimulations = new SimpleIntegerProperty(0);
        waitingSimulations = new SimpleIntegerProperty(0);
        finishedSimulations = new SimpleIntegerProperty(0);
        threadPoolSize = new SimpleIntegerProperty(0);
    }

    public void setThreadPoolSize(Integer threadPoolSize) {
        this.threadPoolSize.set(threadPoolSize);
    }

    public synchronized void incrementRunningSimulations(){
        if(runningSimulations.get()<threadPoolSize.get()){
            this.runningSimulations.set(runningSimulations.get()+1);
        }
        else {
            waitingSimulations.set(waitingSimulations.get()+1);
        }
    }
    public synchronized void decrementRunningSimulations() {
        if (waitingSimulations.get() > 0) {
            waitingSimulations.set(waitingSimulations.get() - 1);
        }
        else{
            this.runningSimulations.set(runningSimulations.get() - 1);
        }
    }

    public synchronized void incrementFinishedSimulations(){
        finishedSimulations.set(finishedSimulations.get()+1);
    }

    public IntegerProperty getRunningSimulations() {
        return runningSimulations;
    }

    public IntegerProperty getWaitingSimulations() {
        return waitingSimulations;
    }

    public IntegerProperty getFinishedSimulations() {
        return finishedSimulations;
    }

    public IntegerProperty getThreadPoolSize() {
        return threadPoolSize;
    }

}
