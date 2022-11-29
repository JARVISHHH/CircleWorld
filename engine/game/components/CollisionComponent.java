package engine.game.components;


import Final.XMLProcessor;
import engine.game.collision.Collision;
import engine.game.collision.Shape;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CollisionComponent extends Component{

    protected Shape shape;

    protected boolean isStatic = false;

    protected boolean isPassable = false;

    protected boolean isDestructible = false;

    protected boolean isProjectile = false;

    protected boolean isGoal = false;

    protected boolean isDetect = false;

    protected int group = 0;

    protected Vec2d movePosition = new Vec2d(0, 0);

    public CollisionComponent() {
        tag = "Collision";
        isStatic = false;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape) {
        tag = "Collision";
        this.shape = shape;
        isStatic = false;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic, boolean isPassable) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        this.isPassable = isPassable;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic, boolean isPassable, boolean isDestructible) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        this.isPassable = isPassable;
        this.isDestructible = isDestructible;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic, boolean isPassable, boolean isDestructible, boolean isProjectile) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        this.isPassable = isPassable;
        this.isDestructible = isDestructible;
        this.isProjectile = isProjectile;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic, boolean isPassable, boolean isDestructible, boolean isProjectile, boolean isGoal) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        this.isPassable = isPassable;
        this.isDestructible = isDestructible;
        this.isProjectile = isProjectile;
        this.isGoal = isGoal;
        setCollide(true);
        setDrawable(true);
    }

    public CollisionComponent(Shape shape, boolean isStatic, boolean isPassable, boolean isDestructible, boolean isProjectile, boolean isGoal, boolean isDetect) {
        tag = "Collision";
        this.shape = shape;
        this.isStatic = isStatic;
        this.isPassable = isPassable;
        this.isDestructible = isDestructible;
        this.isProjectile = isProjectile;
        this.isGoal = isGoal;
        this.isDetect = isDetect;
        setCollide(true);
        setDrawable(true);
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Vec2d getMovePosition() {
        return movePosition;
    }

    public void setMovePosition(Vec2d movePosition) {
        this.movePosition = movePosition;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public Component copy() {
        CollisionComponent component = new CollisionComponent(this.shape);
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        return component;
    }

    // Check if components collided
    public Vec2d getCollide(CollisionComponent component) {
        Shape newShape1 = shape.getScreenPosition(getGameObject().getTransformComponent().position);
        Shape newShape2 = component.shape.getScreenPosition(component.getGameObject().getTransformComponent().getPosition());
        return newShape1.getCollision(newShape2);
    }

    public void collide(Collision newCollision) {
        if(newCollision.mtv == null) return;
        if((isProjectile || newCollision.other.isProjectile) && group == newCollision.other.group) return;
        Vec2d oldPosition = gameObject.getTransformComponent().position;
        if(isStatic) {
            // Damage
            AttackComponent attackComponent = (AttackComponent)this.gameObject.getComponent("Attack");
            if(attackComponent != null)
                newCollision.other.gameObject.getAttack(attackComponent.damage);

            if(isGoal)
                newCollision.other.gameObject.getGoal();

            if(isProjectile)
                this.gameObject.getGameWorld().removeGameObject(this.gameObject);

            return;
        }
        if(newCollision.other.isPassable || isPassable || newCollision.other.isDetect) return;
        if(isDetect) {
            if(newCollision.other.isStatic) {
                movePosition = movePosition.plus(newCollision.mtv);
            } else {
                movePosition = movePosition.plus(newCollision.mtv.smult(1 / 2.0));
            }
            return;
        }

        Vec2d outImpulse = new Vec2d(0, 0);
        PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
        PhysicsComponent otherPhysicsComponent = (PhysicsComponent)newCollision.other.getGameObject().getComponent("Physics");
        if(newCollision.other.isStatic) {
//            movePosition = movePosition.plus(newCollision.mtv);
            setTransformComponentPosition(oldPosition.plus(newCollision.mtv));
            // Apply impulse
            if(physicsComponent != null && otherPhysicsComponent != null) {
                double mass1 = physicsComponent.mass;
                Vec2d normalizedMtv = newCollision.mtv.normalize();
                Vec2d vel1 = normalizedMtv.smult(normalizedMtv.dot(physicsComponent.vel)), vel2 = normalizedMtv.smult(normalizedMtv.dot(otherPhysicsComponent.vel));
                double cor = Math.sqrt(physicsComponent.restitution * otherPhysicsComponent.restitution);
                outImpulse = vel2.minus(vel1).smult(mass1).smult(1 + cor);
                physicsComponent.applyImpulse(outImpulse);
            }
        } else {
//            movePosition = movePosition.plus(newCollision.mtv.smult(1 / 2.0));
            setTransformComponentPosition(oldPosition.plus(newCollision.mtv.smult(1 / 2.0)));
            // Apply impulse
            if(physicsComponent != null && otherPhysicsComponent != null) {
                double mass1 = physicsComponent.mass, mass2 = otherPhysicsComponent.mass;
                Vec2d normalizedMtv = newCollision.mtv.normalize();
                Vec2d vel1 = normalizedMtv.smult(normalizedMtv.dot(physicsComponent.vel)), vel2 = normalizedMtv.smult(normalizedMtv.dot(otherPhysicsComponent.vel));
                double cor = Math.sqrt(physicsComponent.restitution * otherPhysicsComponent.restitution);
                outImpulse = vel2.minus(vel1).smult(mass1).smult(mass2).smult(1 + cor).smult(1 / (mass1 + mass2));
                physicsComponent.applyImpulse(outImpulse);
            }
        }
//        // Update jump component
//        if(newCollision.mtv.y < 0) {
//            JumpComponent jumpComponent = (JumpComponent)getGameObject().getComponent("Jump");
//            if(jumpComponent != null) jumpComponent.resetLeftJumpTime();
//        }

        // Damage
        AttackComponent attackComponent = (AttackComponent)this.gameObject.getComponent("Attack");
        if(attackComponent != null)
            newCollision.other.gameObject.getAttack(attackComponent.damage);

        // Check if the game is over
        if(isGoal)
            newCollision.other.gameObject.getGoal();

        if(isProjectile)
            this.gameObject.getGameWorld().removeGameObject(this.gameObject);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        Shape screenShape = shape.getScreenPosition(getGameObject().getTransformComponent().position);
        screenShape.onDraw(g);
    }

    @Override
    public Element writeXML(Document doc) {
        Element collisionComponent = doc.createElement("CollisionComponent");
        collisionComponent.setAttribute("isStatic", String.valueOf(isStatic));
        collisionComponent.setAttribute("isPassable", String.valueOf(isPassable));
        collisionComponent.setAttribute("isDestructible", String.valueOf(isDestructible));
        collisionComponent.setAttribute("destructAfterFirstCollision", String.valueOf(isProjectile));
        collisionComponent.setAttribute("isGoal", String.valueOf(isGoal));

        Element shape = this.shape.writeXML(doc);
        collisionComponent.appendChild(shape);

        return collisionComponent;
    }

    @Override
    public void readXML(Element e) {
        isStatic = Boolean.parseBoolean(e.getAttribute("isStatic"));
        isPassable = Boolean.parseBoolean(e.getAttribute("isPassable"));
        isDestructible = Boolean.parseBoolean(e.getAttribute("isDestructible"));
        isProjectile = Boolean.parseBoolean(e.getAttribute("destructAfterFirstCollision"));
        isGoal = Boolean.parseBoolean(e.getAttribute("isGoal"));

        NodeList nodeList = e.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++) {
            if(e.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element shapeElement = (Element) e.getChildNodes().item(i);
            try {
                this.shape = XMLProcessor.tag2shape.get(shapeElement.getTagName()).newInstance();
                this.shape.readXML(shapeElement);
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
