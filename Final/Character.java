package Final;

import engine.game.GameObject;
import engine.game.collision.AABShape;
import engine.game.collision.CircleShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Character {
    static KeyCode direction[] = {KeyCode.LEFT, KeyCode.RIGHT};  // Directions that the character can move

    protected GameObject character = null;
    protected Vec2d characterSize;

    public Character(Vec2d position,  Vec2d spriteSize) {
        this.characterSize = spriteSize.smult(3.0 / 4.0);
        character = createCharacter(position, characterSize);
    }

    public GameObject getCharacter() {
        return character;
    }

    public Vec2d getCharacterSize() {
        return characterSize;
    }

    /**
     * Component that control how the character moves.
     */
    static class CharacterMoveComponent extends MoveComponent{

        public CharacterMoveComponent() {
            super();
            tag = "Move";
            this.maxVel = 15;
        }

        double horizontalAcceleration = 150;
        double verticalAcceleration = 0;

        @Override
        public void onTick(long nanosSincePreviousTick) {
            ClimbComponent climbComponent = (ClimbComponent) gameObject.getComponent("Climb");
            if(climbComponent != null && climbComponent.isClimbing()) return;
            double t = nanosSincePreviousTick / 1000000000.0;
            for(int k = 0; k < 2; k++) {
                if(gameObject.keyPressing.containsKey(direction[k]) && gameObject.keyPressing.get(direction[k])) {
                    PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
                    moveDirection = new Vec2d(dx[k], dy[k]);
                    Vec2d force = moveDirection;
                    Vec2d impulse = new Vec2d(force.x * horizontalAcceleration, force.y * verticalAcceleration).smult(physicsComponent.getMass() * t);
                    if(physicsComponent.getVel().x * impulse.x > 0) {
                        if(physicsComponent.getVel().x + impulse.x / physicsComponent.getMass() > maxVel) {
                            impulse = new Vec2d((maxVel - physicsComponent.getVel().x) * physicsComponent.getMass(), 0);
                        }
                        else if(physicsComponent.getVel().x + impulse.x / physicsComponent.getMass() < -maxVel) {
                            impulse = new Vec2d((-maxVel - physicsComponent.getVel().x) * physicsComponent.getMass(), 0);
                        }
                        if(physicsComponent.getVel().x * impulse.x < 0) impulse = new Vec2d(0, 0);
                    }
                    physicsComponent.applyImpulse(impulse);
                }
            }
        }

        @Override
        public Element writeXML(Document doc) {
            Element characterMoveComponent = doc.createElement("CharacterMoveComponent");
            characterMoveComponent.setAttribute("moveDirection", String.valueOf(moveDirection));

            return characterMoveComponent;
        }
    }

    static class CharacterDashComponent extends DashComponent {

        public String beforeSprite = "circle";
        public String afterSprite = "tiredCircle";

        @Override
        public void doDash(double t) {
            super.doDash(t);
            SpriteComponent spriteComponent = (SpriteComponent) gameObject.getComponent("Sprite");
            if(spriteComponent != null) {
                spriteComponent.setSpriteTag(afterSprite);
            }
        }

        @Override
        public void refresh() {
            super.refresh();
            SpriteComponent spriteComponent = (SpriteComponent) gameObject.getComponent("Sprite");
            if(spriteComponent != null) {
                spriteComponent.setSpriteTag(beforeSprite);
            }
        }
    };

    /**
     * Create a new character object.
     * @param position the original position of the game.
     * @param characterSize the size of the character in the game.
     * @return return the character game object.
     */
    public GameObject createCharacter(Vec2d position, Vec2d characterSize) {

        GameObject characterObject = new GameObject();

        characterObject.setTransformComponent(new TransformComponent(position, characterSize));

        SpriteComponent spriteComponent = new SpriteComponent("circle", new Vec2d(0, 0), characterSize, new Vec2i(0, 0));
        characterObject.addComponent(spriteComponent);

        CollisionComponent jumpGroundDetect = new CollisionComponent(new AABShape(new Vec2d(characterSize.x / 4, characterSize.y - 1), new Vec2d(characterSize.x / 2, 1)), false, false, false, false, false, true);
        characterObject.addComponent(jumpGroundDetect);
        JumpComponent jumpComponent = new JumpComponent();
        jumpComponent.setJumpKey(KeyCode.C);
        jumpComponent.addDetect(jumpGroundDetect);
        characterObject.addComponent(jumpComponent);

        CollisionComponent dashGroundDetect = new CollisionComponent(new AABShape(new Vec2d(characterSize.x / 4, characterSize.y - 1), new Vec2d(characterSize.x / 2, 1)), false, false, false, false, false, true);
        characterObject.addComponent(dashGroundDetect);
        CharacterDashComponent dashComponent = new CharacterDashComponent();
        dashComponent.setDashKey(KeyCode.X);
        dashComponent.setGroundDetect(dashGroundDetect);
        characterObject.addComponent(dashComponent);

        CollisionComponent gravityGroundDetect = new CollisionComponent(new AABShape(new Vec2d(characterSize.x / 4, characterSize.y - 1), new Vec2d(characterSize.x / 2, 1)), false, false, false, false, false, true);
        characterObject.addComponent(gravityGroundDetect);
        GravityComponent gravityComponent = new GravityComponent();
        gravityComponent.setGroundDetect(gravityGroundDetect);
        characterObject.addComponent(gravityComponent);

        CharacterMoveComponent characterMoveComponent = new CharacterMoveComponent();
        characterObject.addComponent(characterMoveComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new CircleShape(characterSize.smult(1.0 / 2.0), characterSize.x / 2 - 2));
        collisionComponent.setGroup(0);
        characterObject.addComponent(collisionComponent);
        KeyEventsComponent keyEventsComponent = new KeyEventsComponent();
        characterObject.addComponent(keyEventsComponent);
        HealthComponent healthComponent = new HealthComponent(60);
        characterObject.addComponent(healthComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(70, 0);
        characterObject.addComponent(physicsComponent);

        return characterObject;
    }
}
