package engine.game.systems;

import engine.game.GameObject;

import java.util.ArrayList;

public class TickSystem extends System{
    protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

    @Override
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        for(GameObject gameObject: gameObjects)
            gameObject.onTick(nanosSincePreviousTick);
    }
}
