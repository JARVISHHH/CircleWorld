package engine.game;

import engine.game.collision.Collision;
import engine.game.components.*;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class GameObject {

    protected GameWorld gameWorld;  // Game world it belongs to
    protected boolean active = false;
    protected ArrayList<Component> components = new ArrayList<Component>();
    protected TransformComponent transformComponent = new TransformComponent();
    protected int zIndex;
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean responseMouseEvents = false;
    protected boolean responseKeyEvents = false;
    protected boolean collide = false;

    public HashMap<KeyCode, Boolean> keyPressing = new HashMap<KeyCode, Boolean>();

    public GameObject() {

    }

    // Copy from an existing game object
    public GameObject(GameObject gameObject) {
        for(Component component: gameObject.components) {
            Component newComponent = component.copy();
            newComponent.setGameObject(this);
            components.add(newComponent);
        }
        transformComponent = new TransformComponent(gameObject.getTransformComponent());
        drawable = gameObject.drawable;
        responseMouseEvents = gameObject.responseMouseEvents;
        responseKeyEvents = gameObject.responseKeyEvents;
        collide = gameObject.collide;
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getZIndex() {
        return zIndex;
    }

    public boolean isTickable() {
        return tickable;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public boolean doResponseMouseEvents() {
        return responseMouseEvents;
    }

    public boolean doResponseKeyEvents() {
        return responseKeyEvents;
    }

    public boolean checkCollide() {return collide;}

    public void setTickable(boolean tickable) {
        this.tickable = tickable;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public void setResponseMouseEvents(boolean responseMouseEvents) {
        this.responseMouseEvents = responseMouseEvents;
    }
    public void setResponseKeyEvents(boolean responseKeyEvents) {
        this.responseKeyEvents = responseKeyEvents;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public void addComponent(Component component) {
        components.add(component);
        component.setGameObject(this);
        if(component.isTickable())
            setTickable(true);
        if(component.isDrawable())
            setDrawable(true);
        if(component.doResponseMouseEvents())
            setResponseMouseEvents(true);
        if(component.checkCollide())
            setCollide(true);
        if(component.doResponseKeyEvents())
            setResponseKeyEvents(true);
    }

    public void removeComponent(Component c) {
        if(c == null) return;
        components.remove(c);
    }

    public void removeComponent(ArrayList<Component> c) {
        for(Component component: c) components.remove(component);
    }

    public ArrayList<Component> getComponentList(String tag) {
        ArrayList<Component> result = new ArrayList<Component>();
        for(Component component: components)
            if(component.getTag().equals(tag))
                result.add(component);
        return result;
    }

    public Component getComponent(String tag) {
        for(Component component: components)
            if(component.getTag().equals(tag))
                return component;
        return null;
    }

    public ArrayList<Component> getAllComponents() {
        ArrayList<Component> allComponents = new ArrayList<>();
        allComponents.add(this.transformComponent);
        allComponents.addAll(components);
        return allComponents;
    }

    public TransformComponent getTransformComponent() {
        return transformComponent;
    }

    public void setTransformComponent(TransformComponent transformComponent) {
        this.transformComponent = transformComponent;
    }

    public void collide(GameObject gameObject) {
        if(!active || !gameObject.active) return;
        ArrayList<Component> collisionComponents1 = getComponentList("Collision");
        ArrayList<Component> collisionComponents2 = gameObject.getComponentList("Collision");
        for(int i = 0; i < collisionComponents1.size(); i++) {
            CollisionComponent component1 = (CollisionComponent)collisionComponents1.get(i);
            for(int j = 0; j < collisionComponents2.size(); j++) {
                CollisionComponent component2 = (CollisionComponent)collisionComponents2.get(j);
                Vec2d mtv = component1.getCollide(component2);
                component1.collide(new Collision(component2, mtv == null ? null : mtv.smult(-1), null, null));
                component2.collide(new Collision(component1, mtv, null, null));
            }
        }
    }

    public void rayCast(GameObject gameObject) {
        if(!active || !gameObject.active) return;
        ArrayList<Component> rayComponents = getComponentList("Ray");
        ArrayList<Component> collisionComponents2 = gameObject.getComponentList("Collision");
        for(int i = 0; i < rayComponents.size(); i++) {
            RayComponent component1 = (RayComponent)rayComponents.get(i);
            if(component1.destructed) continue;
            for(int j = 0; j < collisionComponents2.size(); j++) {
                CollisionComponent component2 = (CollisionComponent)collisionComponents2.get(i);
                component1.rayCast(component2);
            }
        }
    }

    public boolean getAttack(int value) {
        if(!active) return false;
        HealthComponent component = (HealthComponent)getComponent("Health");
        if(component == null) return false;
        component.minus(value);
        if(component.getHealth() > 0) return false;
        if(this == gameWorld.centerGameObject) {
            gameWorld.win = false;
            gameWorld.hasResult = true;
        } else {
            gameWorld.removeGameObject(this);
        }
        return true;
    }

    public boolean getGoal() {
        if(!active) return false;
        if(this == gameWorld.centerGameObject) {
            gameWorld.win = true;
            gameWorld.hasResult = true;
            return true;
        }
        return false;
    }

    public Element writeXML(Document doc, String name) {
        Element gameObjectElement = doc.createElement(name);

        gameObjectElement.setAttribute("active", String.valueOf(active));
        gameObjectElement.setAttribute("zIndex", String.valueOf(zIndex));
        gameObjectElement.setAttribute("tickable", String.valueOf(tickable));
        gameObjectElement.setAttribute("drawable", String.valueOf(drawable));
        gameObjectElement.setAttribute("responseMouseEvents", String.valueOf(responseMouseEvents));
        gameObjectElement.setAttribute("responseKeyEvents", String.valueOf(responseKeyEvents));
        gameObjectElement.setAttribute("collide", String.valueOf(collide));
        gameObjectElement.setAttribute("keyPressing", String.valueOf(keyPressing));

        gameObjectElement.appendChild(transformComponent.writeXML(doc));

        for(Component component:components) {
            Element e = component.writeXML(doc);
            if(e != null)
                gameObjectElement.appendChild(component.writeXML(doc));
        }

        return gameObjectElement;
    }

    public void onTick(long nanosSincePreviousTick) {
        if(!active) return;
        for(Component component: components)
            if(component.isTickable())
                component.onTick(nanosSincePreviousTick);
    }

    public void onLateTick() {

    }

    public void onDraw(GraphicsContext g) {
        if(!active) return;
        for(Component component: components)
            if(component.isDrawable())
                component.onDraw(g);
    }

    public void onKeyPressed(KeyEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseKeyEvents())
                component.onKeyPressed(e);
    }

    public void onKeyReleased(KeyEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseKeyEvents())
                component.onKeyReleased(e);
    }

    public void onMouseDragged(MouseEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseMouseEvents())
                component.onMouseDragged(e);
    }

    public void onMouseClicked(MouseEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseMouseEvents())
                component.onMouseClicked(e);
    }

    public void onMousePressed(MouseEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseMouseEvents())
                component.onMousePressed(e);
    }

    public void onMouseReleased(MouseEvent e) {
        if(!active) return;
        for(Component component: components)
            if(component.doResponseMouseEvents())
                component.onMouseReleased(e);
    }

    public void onShutdown() {
        for(Component component: components)
            component.onShutdown();
    }
}
