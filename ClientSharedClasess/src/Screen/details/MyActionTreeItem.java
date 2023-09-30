package Screen.details;

public class MyActionTreeItem extends MyTreeItem{

    int actionIndex;

    public MyActionTreeItem(String value, String name, int numberOfActionInActionList) {
        super(value, name);
        this.actionIndex = numberOfActionInActionList;
    }

    public int getactionIndex() {
        return actionIndex;
    }
}
