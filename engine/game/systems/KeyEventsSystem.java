package engine.game.systems;

import engine.game.GameObject;
import javafx.scene.input.KeyEvent;

public class KeyEventsSystem extends System{
    @Override
    public void onKeyPressed(KeyEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onKeyPressed(e);
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        for(GameObject gameObject: gameObjects)
            gameObject.onKeyReleased(e);
    }
}
