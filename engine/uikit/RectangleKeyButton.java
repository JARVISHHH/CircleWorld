package engine.uikit;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RectangleKeyButton extends UIElement{
    protected Color normalColor;  // Normal color
    protected Color hoverColor;  // The color when mouse is hovering

    protected boolean chosen = false;

    protected double factor = 20;

    public RectangleKeyButton(Vec2d position, Vec2d size) {
        super();
        this.position = position;
        this.size = size;
        this.color = Color.rgb(0, 0, 0, 0);
        this.normalColor = Color.rgb(0, 0, 0, 0);
        this.hoverColor = Color.rgb(0, 0, 0, 0);
    }

    public RectangleKeyButton(Vec2d position, Vec2d size, Color normalColor) {
        super();
        this.position = position;
        this.size = size;
        this.color = normalColor;
        this.normalColor = normalColor;
        this.hoverColor = Color.rgb(0, 0, 0, 0);
    }

    public RectangleKeyButton(Vec2d position, Vec2d size, Color normalColor, Color hoverColor) {
        super();
        this.position = position;
        this.size = size;
        this.color = normalColor;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
    }

    protected void action() {

    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(chosen) this.color = hoverColor;
        else this.color = normalColor;
        super.onTick(nanosSincePreviousTick);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        g.setFill(color);
        if(!chosen)
            g.fillRoundRect(position.x, position.y, size.x, size.y, size.x / 10, size.y / 10);
        else
            g.fillRoundRect(position.x - size.x / factor, position.y - size.y / factor, size.x * (factor + 2) / factor, size.y * (factor + 2) / factor, size.x / 10 * (factor + 2) / factor, size.y / 10 * (factor + 2) / factor);
        super.onDraw(g);
    }
}
