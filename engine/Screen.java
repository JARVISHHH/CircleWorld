package engine;

import engine.support.Vec2d;
import engine.uikit.UIElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Screen {

    protected ArrayList<UIElement> children = new ArrayList<UIElement>();
    protected Vec2d position;
    protected Vec2d size;
    protected Color color;

    protected boolean active = false;

    public String name;

    public Screen(Vec2d size) {
        this.size = size;
        this.color = Color.rgb(255, 255, 255);
    }
    public Screen(Vec2d size, Color color) {
        this.size = size;
        this.color = color;
    }

    public Screen(String name, Vec2d size) {
        this.name = name;
        this.size = size;
        color = Color.rgb(255, 255, 255);
    }

    public Screen(String name, Vec2d size, Color color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addUIElement(UIElement element) {
        children.add(element);
    }
    public void clearChildren() {
        children.clear();
    }
    public Vec2d getSize() {
        return size;
    }

    public void onShutdown() {
        setActive(false);
        for(UIElement child: children)
            child.onShutdown();
    }

    public void onStartup() {
        setActive(true);
        for(UIElement child: children)
            child.onStartup();
    }

    public void onDraw(GraphicsContext g) {
        if(!active) return;
        g.setFill(color);
        g.fillRect(0, 0, size.x, size.y);
        for(UIElement child: children)
            child.onDraw(g);
    }

    public void onResize(Vec2d newSize) {
        double ratio = Math.min(newSize.x / size.x, newSize.y / size.y);
        for(UIElement child: children)
            child.onResize(child.getSize().smult(ratio));  // Pass correct aspect ratio size here
        size = size.smult(ratio);
    }

    public void onTick(long nanosSincePreviousTick) {
        if(!active) return;
        for(UIElement child:children) {
            child.onTick(nanosSincePreviousTick);
        }
    }

    public void onKeyTyped(KeyEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onKeyTyped(e);
        }
    }

    public void onKeyPressed(KeyEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onKeyPressed(e);
        }
    }

    public void onKeyReleased(KeyEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onKeyReleased(e);
        }
    }

    public void onMouseClicked(MouseEvent e) {
        if(!active) return;
        for(UIElement child: children) {
            child.onMouseClicked(e);
        }
    }

    public void onMousePressed(MouseEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onMousePressed(e);
        }
    }

    public void onMouseReleased(MouseEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onMouseReleased(e);
        }
    }

    public void onMouseDragged(MouseEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onMouseDragged(e);
        }
    }

    public void onMouseMoved(MouseEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onMouseMoved(e);
        }
    }

    public void onMouseWheelMoved(ScrollEvent e) {
        if(!active) return;
        for(UIElement child:children) {
            child.onMouseWheelMoved(e);
        }
    }

    public void onFocusChanged(boolean newVal) {
        if(!active) return;
        for(UIElement child:children) {
            child.onFocusChanged(newVal);
        }
    }
}
