package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MoveComponent extends Component{

    protected double maxVel = 20;
    protected double moveRate = 100;

    protected Vec2d moveDirection = new Vec2d(1, 0);

    protected double dx[] = {-1, 1, 0, 0}, dy[] = {0, 0, -1, 1};
    protected KeyCode direction[] = {KeyCode.LEFT, KeyCode.RIGHT,KeyCode.UP,KeyCode.DOWN};

    public MoveComponent() {
        tag = "Move";
        setTickable(true);
    }

    public void setMoveRate(double moveRate) {
        this.moveRate = moveRate;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        // Move
        double distance = moveRate * nanosSincePreviousTick / 1000000000;
        for(int k = 0; k < 4; k++) {
            if(gameObject.keyPressing.containsKey(direction[k]) && gameObject.keyPressing.get(direction[k])) {
                Vec2d oldPosition = getGameObject().getTransformComponent().position;
                moveDirection = new Vec2d(dx[k], dy[k]);
                double x = Math.max(0, Math.min(getGameObject().getGameWorld().getSize().x - getGameObject().getTransformComponent().size.x, oldPosition.x + dx[k] * distance));
                double y = Math.max(0, Math.min(getGameObject().getGameWorld().getSize().y - getGameObject().getTransformComponent().size.y, oldPosition.y + dy[k] * distance));
                setTransformComponentPosition(new Vec2d(x, y));
            }
        }
    }

    @Override
    public Element writeXML(Document doc) {
        Element moveComponent = doc.createElement("MoveComponent");
        moveComponent.setAttribute("moveDirection", String.valueOf(moveDirection));

        return moveComponent;
    }

    @Override
    public void readXML(Element e) {
        moveDirection = Vec2d.toVec2d(e.getAttribute("moveDirection"));
    }
}
