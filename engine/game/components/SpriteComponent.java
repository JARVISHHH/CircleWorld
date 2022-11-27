package engine.game.components;

import engine.game.Resource;
import engine.game.Wrapper;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SpriteComponent extends Component{
    protected String spriteTag;
    protected Vec2d position;
    protected Vec2d size;

    Wrapper wrapper;

    protected Vec2d spritePosition;
    protected Vec2d spriteSize;
    protected Vec2i index;
    protected boolean getInfo = false;

    public SpriteComponent() {
        tag = "Sprite";
        setDrawable(true);
        this.spritePosition = new Vec2d(0, 0);
    }

    public SpriteComponent(String spriteTag, Vec2d position, Vec2d size, Vec2i index) {
        tag = "Sprite";
        this.spriteTag = spriteTag;
        this.position = position;
        this.size = size;
        this.index = index;
        setDrawable(true);
    }

    public void setSpriteTag(String spriteTag) {
        this.spriteTag = spriteTag;
    }

    @Override
    public Component copy() {
        SpriteComponent component = new SpriteComponent(spriteTag, position, size, index);
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        return component;
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!getInfo) {
            wrapper = Resource.getWrapper(spriteTag);
            if (wrapper == null) return;
            getInfo = true;
        }
        this.spritePosition = wrapper.getSprite(index);
        this.spriteSize = wrapper.getSpriteSize();
        Vec2d gameObjectPosition = gameObject.getTransformComponent().getPosition();
        Image image = Resource.getImage(spriteTag);
        g.drawImage(image,
                spritePosition.x, spritePosition.y,
                spriteSize.x, spriteSize.y,
                gameObjectPosition.x + position.x, gameObjectPosition.y + position.y,
                size.x, size.y);
    }

    @Override
    public Element writeXML(Document doc) {
        Element spriteComponent = doc.createElement("SpriteComponent");
        spriteComponent.setAttribute("spriteTag", spriteTag);
        spriteComponent.setAttribute("position", String.valueOf(position));
        spriteComponent.setAttribute("size", String.valueOf(size));
        spriteComponent.setAttribute("index", String.valueOf(index));

        return spriteComponent;
    }

    @Override
    public void readXML(Element e) {
        spriteTag = e.getAttribute("spriteTag");
        position = Vec2d.toVec2d(e.getAttribute("position"));
        size = Vec2d.toVec2d(e.getAttribute("size"));
        index = Vec2i.toVec2i(e.getAttribute("index"));
    }
}
