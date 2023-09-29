package Body.ThirdScreen;

public class PropertyHistPair {
    String property;
    Integer amount;

    public PropertyHistPair(String property, Integer amount) {
        this.property = property;
        this.amount = amount;
    }

    public String getProperty() {
        return property;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
