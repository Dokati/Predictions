package Property;


import PRD.PRDRange;

public class Range {

    private Float to;
    private Float from;

    public Range(PRDRange range) {
        if(range.getTo() < range.getFrom())
        {
            throw new IllegalArgumentException("Invalid range - It is not possible that to is smaller than from");
        }
        this.to = (float)range.getTo();
        this.from = (float)range.getFrom();
    }

    public Range(Float from, Float to) {
        this.from = from;
        this.to = to;
    }

    public Range(Double from, Double to) {
        this.from = from.floatValue();
        this.to = to.floatValue();
    }

    public Float getTo() {
        return to;
    }

    public void setTo(Float value) {
        this.to = value;
    }

    public Float getFrom() {
        return from;
    }

    public void setFrom(Float value) {
        this.from = value;
    }

    @Override
    public String toString() {
        return "\nRange: (to=" + to +
                ", from=" + from + ")";
    }
}
