package Body.SecondScreen.ListViewItems;

public class FloatEnvPropListItemWithRange implements MyListItem{
    String name;
    String Type;

    public FloatEnvPropListItemWithRange(String name, String type) {
        this.name = name;
        Type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return Type;
    }
}
