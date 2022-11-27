package engine.game.components;

import engine.support.Vec2d;
import engine.support.Vec2i;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.LinkedList;

public class AnimationComponent extends SpriteComponent{
    protected LinkedList<Vec2i> sprites = new LinkedList<>();

    protected double changeTime = 0.15;
    protected double sumTime = 0;

    protected int startIndex = 0;
    protected int animationSize = 0;
    protected int totalSize = 0;

    public AnimationComponent() {
        tag = "Animation";
        setDrawable(true);
        setTickable(true);
        this.spritePosition = new Vec2d(0, 0);
    }

    public AnimationComponent(String spriteTag, Vec2d position, Vec2d size) {
        tag = "Animation";
        this.spriteTag = spriteTag;
        this.position = position;
        this.size = size;
        setDrawable(true);
        setTickable(true);
    }

    @Override
    public Component copy() {
        AnimationComponent component = new AnimationComponent(spriteTag, position, size);
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        component.sprites = sprites;
        return component;
    }

    public void setChangeTime(double changeTime) {
        this.changeTime = changeTime;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setAnimationSize(int animationSize) {
        this.animationSize = animationSize;
    }

    public void addSprite(Vec2i index) {
        sprites.add(index);
        if(this.index == null) this.index = index;
        totalSize++;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        int currentSize;
        if(animationSize == 0) currentSize = totalSize;
        else currentSize = animationSize;
        sumTime += nanosSincePreviousTick / 1000000000.0;
        if(sumTime / changeTime > currentSize) sumTime -= (int)((sumTime / changeTime) / currentSize) * currentSize * changeTime;
        int currentIndex = startIndex + (int)(sumTime / changeTime) % currentSize;
        index = sprites.get(currentIndex);
    }

    protected void setXMLAttribute(Element animationComponent) {
        animationComponent.setAttribute("spriteTag", spriteTag);
        animationComponent.setAttribute("position", String.valueOf(position));
        animationComponent.setAttribute("size", String.valueOf(size));
        animationComponent.setAttribute("index", String.valueOf(index));

        animationComponent.setAttribute("sprites", String.valueOf(sprites));
        animationComponent.setAttribute("changeTime", String.valueOf(changeTime));
        animationComponent.setAttribute("sumTime", String.valueOf(sumTime));
        animationComponent.setAttribute("startIndex", String.valueOf(startIndex));
        animationComponent.setAttribute("animationSize", String.valueOf(animationSize));
    }

    @Override
    public Element writeXML(Document doc) {
        Element animationComponent = doc.createElement("AnimationComponent");

        setXMLAttribute(animationComponent);

        return animationComponent;
    }

    @Override
    public void readXML(Element e) {
        spriteTag = e.getAttribute("spriteTag");
        position = Vec2d.toVec2d(e.getAttribute("position"));
        size = Vec2d.toVec2d(e.getAttribute("size"));
        index = Vec2i.toVec2i(e.getAttribute("index"));

        changeTime = Double.parseDouble(e.getAttribute("changeTime"));
        sumTime = Double.parseDouble(e.getAttribute("sumTime"));
        startIndex = Integer.parseInt(e.getAttribute("startIndex"));
        animationSize = Integer.parseInt(e.getAttribute("animationSize"));

        String spritesStr = e.getAttribute("sprites").replace("[", "").replace("]", "");
        String[] indices = spritesStr.split("\\),");
        for(String index: indices) {
            addSprite(Vec2i.toVec2i(index));
        }
    }
}
