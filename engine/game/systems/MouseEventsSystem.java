package engine.game.systems;

import engine.game.GameObject;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class MouseEventsSystem extends System{

    @Override
    public void onMouseDragged(MouseEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onMouseDragged(e);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onMouseClicked(e);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onMousePressed(e);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onMouseReleased(e);
    }
}
