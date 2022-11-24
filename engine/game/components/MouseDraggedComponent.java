package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;

public class MouseDraggedComponent extends Component{
    protected Vec2d draggingPosition;  // The position in component

    protected boolean dragging = false;

    public MouseDraggedComponent() {
        tag = "MouseDragged";
        setResponseMouseEvents(true);
    }

    @Override
    public Component copy() {
        MouseDraggedComponent component = new MouseDraggedComponent();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        component.dragging = this.dragging;
        component.draggingPosition = new Vec2d(this.draggingPosition.x, this.draggingPosition.y);
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

    @Override
    public void onMouseDragged(MouseEvent e) {
        Affine affine = getAffine();
        Vec2d mousePosition = new Vec2d(affine.transform(e.getX(), e.getY()).getX(), affine.transform(e.getX(), e.getY()).getY());
        if(!dragging && !inBound(mousePosition)) {
            dragging = false;
            return;
        }
        Vec2d gameObjectPosition = gameObject.getTransformComponent().position;
        if(dragging) {
            Vec2d newGameObjectPosition = new Vec2d(mousePosition.x - draggingPosition.x, mousePosition.y - draggingPosition.y);
            setTransformComponentPosition(newGameObjectPosition);
        }
        else {
            dragging = true;
            draggingPosition = new Vec2d(mousePosition.x - gameObjectPosition.x, mousePosition.y - gameObjectPosition.y);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        dragging = false;
    }
}
