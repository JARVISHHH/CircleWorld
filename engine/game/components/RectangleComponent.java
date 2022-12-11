package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RectangleComponent extends Component{
    protected Color color;
    protected String text;
    protected Font textFont;
    protected Vec2d textPosition;
    protected Color textColor;

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

    public void setText(String text, Font textFont, Vec2d textPosition) {
        this.text = text;
        this.textFont = textFont;
        this.textPosition = textPosition;
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
        g.setFont(textFont);
        g.setFill(textColor);
        g.fillText(text, textPosition.x, textPosition.y);
        super.onDraw(g);
    }
}
