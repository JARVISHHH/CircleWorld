package engine.game.components;

import engine.game.GameObject;
import Nin2.XMLProcessor;
import engine.game.collision.Ray;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;

public class FireRayComponent extends Component{

    protected KeyCode fireKey = null;

    protected boolean hasProjected = false;

    protected int damage = 15;

    public FireRayComponent() {
        tag = "FireRay";
        setTickable(true);
    }

    public FireRayComponent(int damage) {
        tag = "FireRay";
        this.damage = damage;
        setTickable(true);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setFireKey(KeyCode keyCode) {
        this.fireKey = keyCode;
    }

    private void project(GameObject projectile, double middleX, double middleY) {
        Vec2d currentPosition = this.gameObject.getTransformComponent().getPosition();
        Vec2d characterSize = gameObject.getTransformComponent().size;

        MoveComponent moveComponent = (MoveComponent)gameObject.getComponent("Move");
        if(moveComponent != null) {
            Vec2d direction = moveComponent.moveDirection.normalize();

            projectile.setTransformComponent(new TransformComponent(new Vec2d(middleX + (characterSize.x / 2 + 1) * direction.x, middleY), new Vec2d(0, 0)));

            RayComponent rayComponent = new RayComponent(new Ray(new Vec2d(0, 0), direction));
            projectile.addComponent(rayComponent);
        } else {
            projectile.setTransformComponent(new TransformComponent(new Vec2d(currentPosition.x + characterSize.x + 1, middleY), new Vec2d(0, 0)));

            RayComponent rayComponent = new RayComponent(new Ray(new Vec2d(0, 0), new Vec2d(1, 0)));
            projectile.addComponent(rayComponent);
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        HashMap<KeyCode, Boolean> keys = gameObject.keyPressing;
        Vec2d characterSize = gameObject.getTransformComponent().size;
        GameObject projectile = new GameObject();
        Vec2d currentPosition = this.gameObject.getTransformComponent().getPosition();
        double middleX = currentPosition.x + characterSize.x / 2;
        double middleY = currentPosition.y + characterSize.y / 2;
        boolean keyPressed = false;
        if(keys.containsKey(fireKey) && keys.get(fireKey)) {
            if(hasProjected) return;
            keyPressed = true;
            project(projectile, middleX, middleY);
        }
        if(!keyPressed) {
            hasProjected = false;
            return;
        }
        hasProjected = true;
        gameObject.getGameWorld().addGameObject(projectile);
    }

    @Override
    public Element writeXML(Document doc) {
        Element fireRayComponent = doc.createElement("FireRayComponent");
        fireRayComponent.setAttribute("hasProjected", Boolean.toString(hasProjected));
        fireRayComponent.setAttribute("damage", Integer.toString(damage));
        fireRayComponent.setAttribute("fireKey", String.valueOf(fireKey));

        return fireRayComponent;
    }

    @Override
    public void readXML(Element e) {
        hasProjected = Boolean.parseBoolean(e.getAttribute("hasProjected"));
        damage = Integer.parseInt(e.getAttribute("damage"));
        fireKey = KeyCode.valueOf(e.getAttribute("fireKey"));
    }
}
