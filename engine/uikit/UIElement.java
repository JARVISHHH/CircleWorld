package engine.uikit;

import java.util.ArrayList;

import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public abstract class UIElement {

    protected ArrayList<UIElement> children;
    protected UIElement parent;
    protected Vec2d position;
    protected Vec2d size;
    protected Color color;

    protected boolean active = true;

    public UIElement() {
        children = new ArrayList<UIElement>();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vec2d getPosition() {
        return position;
    }

    public Vec2d getSize() {
        return size;
    }

    public void addUIElement(UIElement element) {
        children.add(element);
    }

    public void clearChildren() {
        children.clear();
    }

    public void onTick(long nanosSincePreviousTick) {
        if(!active) return;
        for(UIElement child:children) {
            child.onTick(nanosSincePreviousTick);
        }
    }

    public void onDraw(GraphicsContext g) {
        if(!active) return;
        for(UIElement child: children) {
            child.onDraw(g);
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

    public void onResize(Vec2d newSize) {
        double ratio = newSize.x / size.x;
        for(int index = 0; index < children.size(); index++)
            children.get(index).onResize(children.get(index).size.smult(ratio));
        position = position.smult(ratio);
        size = size.smult(ratio);
    }
}
