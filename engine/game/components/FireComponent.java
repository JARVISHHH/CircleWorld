package engine.game.components;

import engine.game.GameObject;
import engine.game.collision.CircleShape;
import engine.game.collision.Collision;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.LinkedList;

public class FireComponent extends Component{

    protected boolean hasProjected = false;

    protected int damage = 30;

    public LinkedList<Vec2i> spriteIndex = new LinkedList<>();

    protected KeyCode fireKey = null;

    public FireComponent() {
        tag = "Fire";
        setTickable(true);
    }

    public FireComponent(int damage) {
        tag = "Fire";
        this.damage = damage;
        setTickable(true);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void clearSpriteIndex() {
        spriteIndex.clear();
    }

    public void addSpriteIndex(Vec2i index) {
        spriteIndex.add(index);
    }

    public void setFireKey(KeyCode keyCode) {
        this.fireKey = keyCode;
    }

    private void projectMoving(GameObject projectile, double middleX, double middleY) {
        Vec2d currentPosition = this.gameObject.getTransformComponent().getPosition();
        Vec2d characterSize = gameObject.getTransformComponent().size;
        Vec2d projectileSize = new Vec2d(characterSize.x / 2, characterSize.y / 2);

        MoveComponent moveComponent = (MoveComponent)gameObject.getComponent("Move");
        if(moveComponent != null) {
            Vec2d direction = moveComponent.moveDirection.normalize();

            projectile.setTransformComponent(new TransformComponent(new Vec2d(middleX - projectileSize.x / 2 + (characterSize.x / 2 + projectileSize.x / 2 + 1) * direction.x, middleY - projectileSize.y / 2), projectileSize));

            MovingComponent movingComponent = new MovingComponent(direction);
            projectile.addComponent(movingComponent);
        } else {
            projectile.setTransformComponent(new TransformComponent(new Vec2d(currentPosition.x + characterSize.x + 1, middleY  - projectileSize.y / 2), projectileSize));

            MovingComponent movingComponent = new MovingComponent(new Vec2d(1, 0));
            projectile.addComponent(movingComponent);
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        HashMap<KeyCode, Boolean> keys = gameObject.keyPressing;
        Vec2d characterSize = gameObject.getTransformComponent().size;
        Vec2d projectileSize = new Vec2d(characterSize.x / 2, characterSize.y / 2);
        GameObject projectile = new GameObject();
        Vec2d currentPosition = this.gameObject.getTransformComponent().getPosition();
        double middleX = currentPosition.x + characterSize.x / 2;
        double middleY = currentPosition.y + characterSize.y / 2;
        boolean keyPressed = false;
        if(keys.containsKey(fireKey) && keys.get(fireKey)) {
            if(hasProjected) return;
            keyPressed = true;
            projectMoving(projectile, middleX, middleY);
        }
        if(!keyPressed) {
            hasProjected = false;
            return;
        }
        hasProjected = true;
        CollisionComponent collisionComponent = new CollisionComponent(new CircleShape(new Vec2d(projectileSize.x / 2, projectileSize.y / 2), Math.min(projectileSize.x / 2, projectileSize.y / 2)));
        collisionComponent.destructAfterFirstCollision = true;

        PhysicsComponent physicsComponent = new PhysicsComponent(200, 0);
        if(spriteIndex.size() != 0) {
            AnimationComponent animationComponent = new AnimationComponent("Projectiles", new Vec2d(0, 0), projectileSize);
            for (Vec2i index : spriteIndex)
                animationComponent.addSprite(index);
            animationComponent.setChangeTime(0.1);
            projectile.addComponent(animationComponent);
        }
        AttackComponent attackComponent = new AttackComponent(damage);

        projectile.addComponent(collisionComponent);
        projectile.addComponent(physicsComponent);
        projectile.addComponent(attackComponent);
        gameObject.getGameWorld().addGameObject(projectile);
    }

    @Override
    public Element writeXML(Document doc) {
        Element fireComponent = doc.createElement("FireComponent");
        fireComponent.setAttribute("hasProjected", Boolean.toString(hasProjected));
        fireComponent.setAttribute("damage", Integer.toString(damage));
        fireComponent.setAttribute("fireKey", String.valueOf(fireKey));
        fireComponent.setAttribute("spriteIndex", this.spriteIndex.toString());

        return fireComponent;
    }

    @Override
    public void readXML(Element e) {
        hasProjected = Boolean.parseBoolean(e.getAttribute("hasProjected"));
        damage = Integer.parseInt(e.getAttribute("damage"));
        fireKey = KeyCode.valueOf(e.getAttribute("fireKey"));

        String spritesStr = e.getAttribute("spriteIndex").replace("[", "").replace("]", "");
        String[] indices = spritesStr.split("\\),");
        for(String index: indices) {
            addSpriteIndex(Vec2i.toVec2i(index));
        }
    }
}