package Body.SecondScreen.ListViewItems;

import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class StringEnvPropItem {

    String name;
    String Type;
    TextArea text;

    public StringEnvPropItem(String name, String type, TextArea text) {
        this.name = name;
        Type = type;
        this.text = new TextArea();
    }
}
