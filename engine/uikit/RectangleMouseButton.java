package engine.uikit;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RectangleMouseButton extends UIElement {

    protected Color normalColor;  // Normal color
    protected Color hoverColor;  // The color when mouse is hovering

    public RectangleMouseButton(Vec2d position, Vec2d size) {
        super();
        this.position = position;
        this.size = size;
        this.color = Color.rgb(0, 0, 0, 0);
        this.normalColor = Color.rgb(0, 0, 0, 0);
        this.hoverColor = Color.rgb(0, 0, 0, 0);
    }

    public RectangleMouseButton(Vec2d position, Vec2d size, Color normalColor) {
        super();
        this.position = position;
        this.size = size;
        this.color = normalColor;
        this.normalColor = normalColor;
        this.hoverColor = Color.rgb(0, 0, 0, 0);
    }

    public RectangleMouseButton(Vec2d position, Vec2d size, Color normalColor, Color hoverColor) {
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
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        g.setFill(color);
        g.fillRect(position.x, position.y, size.x, size.y);
        super.onDraw(g);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        action();
        super.onMouseClicked(e);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(!active) return;
        if(!inBound(new Vec2d(e.getX(), e.getY()))) {
            this.color = normalColor;
            return;
        }
        this.color = hoverColor;
        super.onMouseDragged(e);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(!active) return;
        if(!inBound(new Vec2d(e.getX(), e.getY()))) {
            this.color = normalColor;
            return;
        }
        this.color = hoverColor;
        super.onMousePressed(e);
    }

    @Override
    public void onMouseMoved(MouseEvent e) {
        if(!active) return;
        if(!inBound(new Vec2d(e.getX(), e.getY()))) {
            this.color = normalColor;
            return;
        }
        this.color = hoverColor;
        super.onMouseMoved(e);
    }

    protected boolean inBound(Vec2d pos) {
        return pos.x > position.x && pos.x < position.x + size.x && pos.y > position.y && pos.y < position.y + size.y;
    }
}
