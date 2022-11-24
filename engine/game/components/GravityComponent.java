package engine.game.components;

import Nin2.XMLProcessor;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GravityComponent extends Component{

    protected double gravity = 10;

    public GravityComponent() {
        tag = "Gravity";
        setTickable(true);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
        if(physicsComponent != null) {
            Vec2d g = new Vec2d(0, gravity);
            physicsComponent.applyAcceleration(g);
        }
        super.onTick(nanosSincePreviousTick);
    }

    @Override
    public Element writeXML(Document doc) {
        Element gravityComponent = doc.createElement("GravityComponent");
        gravityComponent.setAttribute("gravity", String.valueOf(gravity));

        return gravityComponent;
    }

    @Override
    public void readXML(Element e) {
        gravity = Double.parseDouble(e.getAttribute("gravity"));
    }
}
