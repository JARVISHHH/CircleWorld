package engine.game.components;

import engine.game.GameObject;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Component {

    protected GameObject gameObject;
    protected String tag;
    protected boolean tickable = false;
    protected boolean drawable = false;
    protected boolean responseMouseEvents = false;
    protected boolean responseKeyEvents = false;
    protected boolean collide = false;

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public String getTag() {
        return tag;
    }

    protected void setTickable(boolean tickable) {
        this.tickable = tickable;
    }

    protected void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }
    protected void setResponseMouseEvents(boolean responseMouseEvents) {this.responseMouseEvents = responseMouseEvents;}
    protected void setResponseKeyEvents(boolean responseKeyEvents) {
        this.responseKeyEvents = responseKeyEvents;
    }
    protected void setCollide(boolean collide) {this.collide = collide;};

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

    public void setTransformComponent(Vec2d position, Vec2d size){
        gameObject.getTransformComponent().setTransform(position, size);
    }

    public void setTransformComponentPosition(Vec2d position){
        gameObject.getTransformComponent().setPosition(position);
    }

    public Affine getAffine(){
        return getGameObject().getGameWorld().getAffine();
    }

    // Return a copy of the component
    public Component copy() {
        Component component = new Component();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        return component;
    }

    public Element writeXML(Document doc) {
        return null;
    }

    public void readXML(Element e) {

    }

    public void onTick(long nanosSincePreviousTick) {

    }

    public void onLateTick() {

    }

    public void onDraw(GraphicsContext g) {

    }

    public void onKeyPressed(KeyEvent e) {

    }

    public void onKeyReleased(KeyEvent e) {

    }

    public void onMouseClicked(MouseEvent e) {

    }

    public void onMouseDragged(MouseEvent e) {

    }

    public void onMousePressed(MouseEvent e) {

    }

    public void onMouseReleased(MouseEvent e) {

    }
}
