package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;

public class MouseClickedComponent extends Component{

    public MouseClickedComponent() {
        tag = "MouseClicked";
        setResponseMouseEvents(true);
    }

    @Override
    public Component copy() {
        MouseClickedComponent component = new MouseClickedComponent();
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

    @Override
    public void onMouseClicked(MouseEvent e) {
        if(!e.isStillSincePress()) return;
        Affine affine = getAffine();
        Vec2d mousePosition = new Vec2d(affine.transform(e.getX(), e.getY()).getX(), affine.transform(e.getX(), e.getY()).getY());
        if(!inBound(new Vec2d(mousePosition))) return;
        TransformComponent transformComponent = gameObject.getTransformComponent();
        gameObject.setTransformComponent(new TransformComponent(transformComponent.position, transformComponent.size.smult(1.1)));
    }
}
