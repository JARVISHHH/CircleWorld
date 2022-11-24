package engine.uikit;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Container extends UIElement{

    public Container(Vec2d position, Vec2d size) {
        super();
        this.position = position;
        this.size = size;
        this.color = Color.rgb(255, 255, 255, 0);
    }

    public Container(Vec2d position, Vec2d size, Color color) {
        super();
        this.position = position;
        this.size = size;
        this.color = color;
    }
}
