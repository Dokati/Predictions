package Terminition;

import Exceptions.IllegalXmlDataInvalidActionEntityExceptions;
import PRD.PRDBySecond;
import PRD.PRDByTicks;

public class Termination {

    private TerminationType type;
    private Integer count;

    public Termination(int count,TerminationType type) {
        this.type = type;
        this.count = count;
    }

    public Termination(PRDBySecond terminationBySecond)
    {
        this.type = TerminationType.SECOND;

        if(terminationBySecond.getCount() < 0)
        {
            throw new IllegalXmlDataInvalidActionEntityExceptions("The termination by second cannot be less than 0");
        }
        this.count = terminationBySecond.getCount();
    }

    public Termination(Object terminationByUser)
    {
        this.type = TerminationType.BYUSER;
    }

    public Termination(PRDByTicks terminationByTick)
    {
        this.type = TerminationType.TICK;

        if(terminationByTick.getCount() < 0)
        {
            throw new IllegalXmlDataInvalidActionEntityExceptions("The termination by ticks cannot be less than 0");
        }

        this.count = terminationByTick.getCount();
    }

    public TerminationType getType() {
        return type;
    }

    public void setType(TerminationType type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Termination: " +
                "type: " + type +
                ", count: " + count + '\n';
    }
}
