package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleComponent extends Component{
    protected Color color;

    public RectangleComponent() {
        tag = "Rectangle";
        color = Color.rgb(0, 0, 0);
        setDrawable(true);
    }

    public RectangleComponent(Color color) {
        tag = "Rectangle";
        this.color = color;
        setDrawable(true);
    }

    @Override
    public Component copy() {
        RectangleComponent component = new RectangleComponent();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        component.color = this.color;
        return component;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setLineWidth(3);
        g.setStroke(color);
        Vec2d position = gameObject.getTransformComponent().getPosition();
        Vec2d size = gameObject.getTransformComponent().getSize();
        g.strokeRect(position.x, position.y, size.x, size.y);
        g.setLineWidth(1);
        super.onDraw(g);
    }
}
