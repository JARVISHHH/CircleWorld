package engine.game.components;

import Nin2.XMLProcessor;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PhysicsComponent extends Component{
    double mass;
    Vec2d acc;
    Vec2d vel;
    Vec2d impulse, force;
    double restitution;

    double maxVel = 10;

    public PhysicsComponent() {
        tag = "Physics";
        setTickable(true);
    }

    public PhysicsComponent(double mass, double restitution) {
        tag = "Physics";
        XMLProcessor.tag2component.put("PhysicsComponent", PhysicsComponent.class);
        this.mass = mass;
        this.restitution = restitution;
        acc = new Vec2d(0, 0);
        vel = new Vec2d(0, 0);
        impulse = new Vec2d(0, 0);
        force = new Vec2d(0, 0);
        setTickable(true);
    }

    public double getMass() {
        return mass;
    }

    public void applyForce(Vec2d f) {
        force = force.plus(f);
    }

    public void applyImpulse(Vec2d p) {
        impulse = impulse.plus(p);
    }

    public void applyAcceleration(Vec2d a) {
        acc = acc.plus(a);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        double t = nanosSincePreviousTick / 1000000000.0;
        vel = vel.plus(force.smult(t).smult(1 / mass).plus(impulse.smult(1 / mass))).plus(acc.smult(t));
        if(vel.mag() > maxVel) {
            vel = vel.normalize().smult(maxVel);
        }
        Vec2d oldPos = getGameObject().getTransformComponent().getPosition();
        getGameObject().getTransformComponent().setPosition(oldPos.plus(vel.smult(t * 10)));
        acc = new Vec2d(0, 0);
        force = new Vec2d(0, 0);
        impulse = new Vec2d(0, 0);
        super.onTick(nanosSincePreviousTick);
    }

    @Override
    public Element writeXML(Document doc) {
        Element physicsComponent = doc.createElement("PhysicsComponent");
        physicsComponent.setAttribute("mass", String.valueOf(mass));
        physicsComponent.setAttribute("acc", String.valueOf(acc));
        physicsComponent.setAttribute("vel", String.valueOf(vel));
        physicsComponent.setAttribute("impulse", String.valueOf(impulse));
        physicsComponent.setAttribute("force", String.valueOf(force));
        physicsComponent.setAttribute("restitution", String.valueOf(restitution));
        physicsComponent.setAttribute("maxVel", String.valueOf(maxVel));

        return physicsComponent;
    }

    @Override
    public void readXML(Element e) {
        mass = Double.parseDouble(e.getAttribute("mass"));
        acc = Vec2d.toVec2d(e.getAttribute("acc"));
        vel = Vec2d.toVec2d(e.getAttribute("vel"));
        impulse = Vec2d.toVec2d(e.getAttribute("impulse"));
        force = Vec2d.toVec2d(e.getAttribute("force"));
        restitution = Double.parseDouble(e.getAttribute("restitution"));
        maxVel = Double.parseDouble(e.getAttribute("maxVel"));
    }
}
