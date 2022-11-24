package engine.uikit;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends UIElement {
    public Rectangle(Vec2d position, Vec2d size, Color color) {
        super();
        this.position = position;
        this.size = size;
        this.color = color;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        g.setFill(color);
        g.fillRect(position.x, position.y, size.x, size.y);
        super.onDraw(g);
    }
}
