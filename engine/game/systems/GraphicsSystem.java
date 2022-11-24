package engine.game.systems;

import engine.game.GameObject;
import engine.game.GameWorld;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class GraphicsSystem extends System{

    protected TreeSet<GameObject> drawOrder = new TreeSet<GameObject>(new GameObjectComparator());

    // Create a comparator
    public static class GameObjectComparator implements Comparator<GameObject> {
        @Override
        public int compare(GameObject gameObject1, GameObject gameObject2) {
            if(gameObject1.getZIndex() > gameObject2.getZIndex()) return 1;
            else if(gameObject1.getZIndex() == gameObject2.getZIndex()) return 0;
            else return -1;
        }
    }

    public void addGameObject(GameObject gameObject) {
        super.addGameObject(gameObject);
        drawOrder.add(gameObject);
    }

    @Override
    public void removeGameObject(GameObject gameObject) {
        super.removeGameObject(gameObject);
        drawOrder.remove(gameObject);
    }

    public void onDraw(GraphicsContext g) {
        Iterator<GameObject> iterate = drawOrder.iterator();
        while(iterate.hasNext()) {
            iterate.next().onDraw(g);
        }
    }

    public void onMouseDragged(MouseEvent e) {

    }
}
