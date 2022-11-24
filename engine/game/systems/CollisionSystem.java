package engine.game.systems;

import engine.game.GameObject;
import engine.game.components.Component;
import engine.game.components.RayComponent;
import javafx.scene.input.MouseEvent;
import org.ietf.jgss.GSSManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class CollisionSystem extends System{

    protected TreeSet<GameObject> collisionOrder = new TreeSet<GameObject>(new CollisionSystem.GameObjectComparator());  // Order of checking collisions

    public static class GameObjectComparator implements Comparator<GameObject> {
        @Override
        public int compare(GameObject gameObject1, GameObject gameObject2) {
            if(gameObject1.getZIndex() > gameObject2.getZIndex()) return 1;
            else if(gameObject1.getZIndex() == gameObject2.getZIndex()) return 0;
            else return -1;
        }
    }

    @Override
    public void addGameObject(GameObject gameObject) {
        super.addGameObject(gameObject);
        collisionOrder.add(gameObject);
    }

    @Override
    public void removeGameObject(GameObject gameObject) {
        super.removeGameObject(gameObject);
        collisionOrder.remove(gameObject);
    }

    private void doCollision() {
        Iterator<GameObject> iterate1 = collisionOrder.iterator();
        while(iterate1.hasNext()) {
            GameObject gameObject1 = iterate1.next();
            ArrayList<Component> rayComponents = gameObject1.getComponentList("Ray");
            for(Component rayComponent: rayComponents) {
                ((RayComponent)rayComponent).update();
            }
            Iterator<GameObject> iterate2 = collisionOrder.iterator();
            GameObject gameObject2;
            while((gameObject2 = iterate2.next()) != gameObject1) {
                gameObject1.collide(gameObject2);
                gameObject1.rayCast(gameObject2);
            }
            gameObject1.rayCast(gameObject2);
            while(iterate2.hasNext()) {
                gameObject2 = iterate2.next();
                gameObject1.rayCast(gameObject2);
            }
            for(Component rayComponent: rayComponents) {
                ((RayComponent)rayComponent).apply();
            }
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        doCollision();
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        doCollision();
    }
}
