package Body.FirstScreen;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

public class MyTreeItem extends TreeItem<String> {

    private String name;

    public MyTreeItem(String name) {
        this.name = name;
    }

    public MyTreeItem(String value, String name) {
        super(value);
        this.name = name;
    }

    public MyTreeItem(String value, Node graphic, String name) {
        super(value, graphic);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
