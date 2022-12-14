package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RectangleTextComponent extends Component{
    protected Color color;
    protected Color backgroundColor = Color.rgb(255, 255, 255);

    protected Vec2d position;
    protected Vec2d size;
    protected String text;
    protected Font textFont;
    protected Vec2d textPosition;
    protected Color textColor;

    protected boolean show = false;

    public RectangleTextComponent() {
        tag = "RectangleText";
        color = Color.rgb(0, 0, 0);
        setDrawable(true);
    }

    public RectangleTextComponent(Vec2d position, Vec2d size, Color color) {
        tag = "RectangleText";
        this.position = position;
        this.size = size;
        this.color = color;
        setDrawable(true);
    }

    public void setText(String text, Font textFont, Vec2d textPosition, Color textColor) {
        this.text = text;
        this.textFont = textFont;
        this.textPosition = textPosition;
        this.textColor = textColor;
    }

    @Override
    public Component copy() {
        RectangleTextComponent component = new RectangleTextComponent();
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
        if(!show) return;
        Vec2d screenPosition = gameObject.getTransformComponent().getPosition().plus(position);
        g.setFill(backgroundColor);
        g.fillRect(screenPosition.x, screenPosition.y, size.x, size.y);
        g.setLineWidth(2);
        g.setStroke(color);
        g.strokeRect(screenPosition.x, screenPosition.y, size.x, size.y);
        g.setLineWidth(1);
        g.setFont(textFont);
        g.setFill(textColor);
        g.fillText(text, screenPosition.x + textPosition.x, screenPosition.y + textPosition.y);
        super.onDraw(g);
    }
}
