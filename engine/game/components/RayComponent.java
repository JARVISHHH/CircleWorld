package engine.game.components;

import engine.game.collision.Ray;
import engine.game.collision.Shape;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RayComponent extends Component{
    protected Color color;

    protected final Ray ray;

    protected float travelRate = 2000;  // Travel rate per second

    protected float maxLength = 1000;
    protected float maxTime = maxLength / travelRate;
    protected float curTime = 0;
    protected float curLength = 0;

    protected float t = (float)1E20;  // Collision position

    public boolean destructed = false;

    public CollisionComponent destination = null;

    public RayComponent(Ray ray) {
        tag = "Ray";
        this.ray = ray;
        color = Color.rgb(0, 255, 0);
        setTickable(true);
        setCollide(true);
        setDrawable(true);
    }

    public void update() {
        if(destructed) return;
        t = (float)1E10;
    }

    public void apply() {
        if(destructed) return;
        if(destination != null && t <= curLength) {
            if(!destination.isPassable && !destination.isStatic) {
                PhysicsComponent physicsComponent = (PhysicsComponent) destination.gameObject.getComponent("Physics");
                if (physicsComponent != null) {
                    physicsComponent.applyImpulse(ray.dir.smult(60));
                }
            }
            destructed = true;
        }
    }

    public void rayCast(CollisionComponent collisionComponent) {
        if(destructed) return;
        Shape newShape = collisionComponent.shape.getScreenPosition(collisionComponent.getGameObject().getTransformComponent().getPosition());
        Ray screenRay = ray.getScreenPosition(getGameObject().getTransformComponent().getPosition());
        float tmp = newShape.rayCast(screenRay);
        if(tmp < 0) return;
        if(t < 0 || t > tmp) {
            t = tmp;
            destination = collisionComponent;
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(destructed) return;
        if(curTime > maxTime) {
            gameObject.getGameWorld().removeGameObject(gameObject);
        } else {
            float t = (float)(nanosSincePreviousTick / 1000000000.0);
            curLength += t * travelRate;
            curTime += t;
        }
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setLineWidth(2);
        g.setStroke(color);
        double res = Math.min(curLength, t);
        Vec2d src = ray.src.plus(gameObject.getTransformComponent().getPosition());
        Vec2d dest = src.plus(ray.dir.smult(res));
        g.strokeLine(src.x, src.y, dest.x, dest.y);
        g.setLineWidth(1);
        g.setStroke(Color.rgb(0, 0, 0));

        if(destructed) gameObject.getGameWorld().removeGameObject(gameObject);
    }

//    @Override
//    public Element writeXML(Document doc) {
//        Element rayComponent = doc.createElement("RayComponent");
//        rayComponent.setAttribute("position", position.toString());
//        rayComponent.setAttribute("size", size.toString());
//
//        return rayComponent;
//    }
//
//    @Override
//    public void readXML(Element e) {
//        position = Vec2d.toVec2d(e.getAttribute("position"));
//        size = Vec2d.toVec2d(e.getAttribute("size"));
//    }

}
