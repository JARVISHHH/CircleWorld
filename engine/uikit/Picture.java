package engine.uikit;

import engine.game.Resource;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Picture extends UIElement{

    protected Image image = null;

    public Picture(Vec2d position, Vec2d size, Image image) {
        super();
        this.position = position;
        this.size = size;
        this.image = image;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        if(image != null) {
            g.drawImage(image,
                        0, 0,
                        image.getWidth(), image.getHeight(),
                        position.x, position.y,
                        size.x, size.y);
        }
        super.onDraw(g);
    }
}
