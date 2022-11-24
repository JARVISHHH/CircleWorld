package engine.uikit;

import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text extends UIElement{

    protected String content;
    protected Font font;

    public Text(String content, Font font, Vec2d position, Color color) {
        super();
        this.content = content;
        this.color = color;
        this.font = font;
        this.position = position;
        FontMetrics FM = new FontMetrics(content, font);
        this.size = new Vec2d(FM.width, FM.height);
    }

    public Text(String content, Font font, Vec2d position) {
        super();
        this.content = content;
        this.font = font;
        this.position = position;
        FontMetrics FM = new FontMetrics(content, font);
        this.size = new Vec2d(FM.width, FM.height);
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        g.setFont(font);
        g.setFill(color);
        g.fillText(content, position.x, position.y);
        super.onDraw(g);
    }

    @Override
    public void onResize(Vec2d newSize) {
        double ratio = newSize.x / size.x;
        position = position.smult(ratio);
        font = Font.font(font.getFamily(), font.getSize() * ratio);
        changeSize();
    }

    protected void changeSize() {
        FontMetrics FM = new FontMetrics(content, font);
        this.size = new Vec2d(FM.width, FM.height);
    }
}
