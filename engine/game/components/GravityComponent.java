package engine.game.components;

import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GravityComponent extends Component{

    protected double gravity = 60;
    protected CollisionComponent groundDetect;

    public GravityComponent() {
        tag = "Gravity";
        setTickable(true);
    }

    public void setGroundDetect(CollisionComponent groundDetect) {
        this.groundDetect = groundDetect;

    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(groundDetect == null || groundDetect.movePosition.y == 0) {
            PhysicsComponent physicsComponent = (PhysicsComponent) getGameObject().getComponent("Physics");
            if (physicsComponent != null) {
                Vec2d g = new Vec2d(0, gravity);
                physicsComponent.applyAcceleration(g);
            }
        }
    }

    @Override
    public Element writeXML(Document doc) {
        Element gravityComponent = doc.createElement("GravityComponent");
        gravityComponent.setAttribute("gravity", String.valueOf(gravity));
        gravityComponent.appendChild(groundDetect.writeXML(doc));

        return gravityComponent;
    }

    @Override
    public void readXML(Element e) {
        gravity = Double.parseDouble(e.getAttribute("gravity"));
//        groundDetect = new CollisionComponent();
//        groundDetect.readXML();
    }
}
