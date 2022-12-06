package engine.game.components;

import Final.XMLProcessor;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GravityComponent extends Component{

    protected double gravity = 60;

    protected double noGravityTime = 0;
    protected CollisionComponent groundDetect = null;

    public GravityComponent() {
        tag = "Gravity";
        setTickable(true);
    }

    public void setNoGravityTime(double noGravityTime) {
        this.noGravityTime = noGravityTime;
    }

    public void setGroundDetect(CollisionComponent groundDetect) {
        this.groundDetect = groundDetect;

    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if((groundDetect == null || groundDetect.movePosition.y >= 0) && noGravityTime <= 0) {
            PhysicsComponent physicsComponent = (PhysicsComponent) getGameObject().getComponent("Physics");
            if (physicsComponent != null) {
                Vec2d g = new Vec2d(0, gravity);
                physicsComponent.applyAcceleration(g);
            }
        } else if(noGravityTime > 0) noGravityTime -= nanosSincePreviousTick / 1000000000.0;
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

        NodeList nodeList = e.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(e.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element element = (Element) e.getChildNodes().item(i);
            if(element.getTagName().equals("CollisionComponent")) {
                groundDetect = new CollisionComponent();
                groundDetect.readXML(element);
            }
        }

        if(groundDetect != null) gameObject.addComponent(groundDetect);
    }
}
