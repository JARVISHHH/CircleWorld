package engine.uikit;

import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class MenuList extends UIElement{

    protected ArrayList<RectangleKeyButton> buttons = new ArrayList<>();
    protected int index = 0;

    public MenuList(Vec2d position, Vec2d size) {
        super();
        this.position = position;
        this.size = size;
    }

    public void addButton(RectangleKeyButton rectangleKeyButton) {
        this.addUIElement(rectangleKeyButton);
        buttons.add(rectangleKeyButton);
        if(index == buttons.size() - 1) rectangleKeyButton.chosen = true;
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if(e.getCode() == KeyCode.UP) {
            buttons.get(index).chosen = false;
            index = index - 1 >= 0 ? index - 1 : index;
            buttons.get(index).chosen = true;
        } else if(e.getCode() == KeyCode.DOWN) {
            buttons.get(index).chosen = false;
            index = index + 1 < buttons.size() ? index + 1 : index;
            buttons.get(index).chosen = true;
        } else if(e.getCode() == KeyCode.C) {
            buttons.get(index).action();
        }
        super.onKeyPressed(e);
    }
}
