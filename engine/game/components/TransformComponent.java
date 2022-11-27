package engine.game.components;

import Final.XMLProcessor;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TransformComponent extends Component{
    protected Vec2d position;
    protected Vec2d size;

    public TransformComponent(){
        tag = "Transform";
        position = new Vec2d(0, 0);
        size = new Vec2d(100, 100);
    }

    public TransformComponent(TransformComponent transformComponent) {
        tag = "Transform";
        position = new Vec2d(transformComponent.position.x, transformComponent.position.y);
        size = new Vec2d(transformComponent.size.x, transformComponent.size.y);
    }

    public TransformComponent(Vec2d position, Vec2d size){
        tag = "Transform";
        XMLProcessor.tag2component.put("TransformComponent", TransformComponent.class);
        this.position = position;
        this.size = size;
    }

    @Override
    public Component copy() {
        TransformComponent component = new TransformComponent();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        component.position = new Vec2d(this.position.x, this.position.y);
        component.size = new Vec2d(this.size.x, this.size.y);
        return component;
    }

    public Vec2d getPosition(){
        return position;
    }

    public Vec2d getSize() {
        return size;
    }

    public void setTransform(Vec2d position, Vec2d size){
        this.position = position;
        this.size = size;
    }

    public void setPosition(Vec2d position){
        this.position = position;
    }

    public void setSize(Vec2d size) {
        this.size = size;
    }

    @Override
    public Element writeXML(Document doc) {
        Element transformComponent = doc.createElement("TransformComponent");
        transformComponent.setAttribute("position", position.toString());
        transformComponent.setAttribute("size", size.toString());

        return transformComponent;
    }

    @Override
    public void readXML(Element e) {
        position = Vec2d.toVec2d(e.getAttribute("position"));
        size = Vec2d.toVec2d(e.getAttribute("size"));
    }
}
