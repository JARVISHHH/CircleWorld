package engine.game.components;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MovingComponent extends Component{

    protected double moveRate = 300;
    public Vec2d direction;

    public MovingComponent() {
        tag = "Moving";
        this.direction = new Vec2d(0, 0);
        setTickable(true);
    }

    public MovingComponent(Vec2d direction) {
        tag = "Moving";
        this.direction = direction;
        setTickable(true);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        double distance = moveRate * nanosSincePreviousTick / 1000000000;
        Vec2d oldPosition = getGameObject().getTransformComponent().position;
        double x = Math.max(0, Math.min(getGameObject().getGameWorld().getSize().x - getGameObject().getTransformComponent().size.x, oldPosition.x + direction.x * distance));
        double y = Math.max(0, Math.min(getGameObject().getGameWorld().getSize().y - getGameObject().getTransformComponent().size.y, oldPosition.y + direction.y * distance));
        setTransformComponentPosition(new Vec2d(x, y));
    }

    @Override
    public Element writeXML(Document doc) {
        Element movingComponent = doc.createElement("MovingComponent");
        movingComponent.setAttribute("moveRate", String.valueOf(moveRate));
        movingComponent.setAttribute("direction", String.valueOf(direction));

        return movingComponent;
    }

    @Override
    public void readXML(Element e) {
        moveRate = Double.parseDouble(e.getAttribute("moveRate"));
        direction = Vec2d.toVec2d(e.getAttribute("direction"));
    }
}
