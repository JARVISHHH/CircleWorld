package engine.game.components;

import engine.game.GameObject;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;

public class InfiniteComponent extends Component{
    public InfiniteComponent() {
        tag = "Infinite";
        setResponseMouseEvents(true);
    }

    @Override
    public Component copy() {
        InfiniteComponent component = new InfiniteComponent();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        return component;
    }

    protected boolean inBound(Vec2d position){
        Vec2d gameObjectPosition = gameObject.getTransformComponent().position;
        Vec2d gameObjectSize = gameObject.getTransformComponent().size;
        if(position.x > gameObjectPosition.x && position.x < gameObjectPosition.x + gameObjectSize.y
                && position.y > gameObjectPosition.y && position.y < gameObjectPosition.y + gameObjectSize.y)
            return true;
        return false;
    }

    // When the mouse is pressed, make a copy of the game object and get rid of the infinite component
    @Override
    public void onMousePressed(MouseEvent e) {
        Affine affine = getAffine();
        Vec2d mousePosition = new Vec2d(affine.transform(e.getX(), e.getY()).getX(), affine.transform(e.getX(), e.getY()).getY());
        if(!inBound(new Vec2d(mousePosition))) return;
        GameObject newGameObject = new GameObject(gameObject);
        newGameObject.removeComponent(newGameObject.getComponentList("Infinite"));
        MouseDraggedComponent mouseDraggedComponent = new MouseDraggedComponent();
        MouseClickedComponent mouseClickedComponent = new MouseClickedComponent();
        newGameObject.addComponent(mouseDraggedComponent);
        newGameObject.addComponent(mouseClickedComponent);
        gameObject.getGameWorld().addGameObject(newGameObject);
    }
}
