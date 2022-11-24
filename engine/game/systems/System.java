package engine.game.systems;

import engine.game.GameObject;
import engine.uikit.UIElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class System {
    ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void onTick(long nanosSincePreviousTick) {

    }

    public void onDraw(GraphicsContext g) {

    }

    public void onKeyPressed(KeyEvent e) {

    }

    public void onKeyReleased(KeyEvent e) {

    }

    public void onMouseDragged(MouseEvent e) {

    }

    public void onMouseClicked(MouseEvent e) {

    }

    public void onMousePressed(MouseEvent e) {

    }

    public void onMouseReleased(MouseEvent e) {

    }
}
