package Nin2;

import engine.game.GameObject;
import engine.game.collision.AABShape;
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

    public Character(Vec2d position,  Vec2d characterSize) {
        this.characterSize = characterSize;
        character = createCharacter(position, characterSize);
    }

    public GameObject getCharacter() {
        return character;
    }

    public Vec2d getCharacterSize() {
        return characterSize;
    }

    /**
     * Component helps to show stand animation
     */
    static class StandAnimationComponent extends AnimationComponent {
        public boolean active = false;
        public StandAnimationComponent() {
            super();
            tag = "StandAnimation";
        }
        public StandAnimationComponent(String spriteTag, Vec2d position, Vec2d size) {
            super(spriteTag, position, size);
        }

        @Override
        public void onTick(long nanosSincePreviousTick) {
            boolean moving = false;
            int indices[] = {1, 3};
            // Check the direction and if the character is moving
            for(int i = 0; i < direction.length; i++) {
                if(gameObject.keyPressing.containsKey(direction[i]) && gameObject.keyPressing.get(direction[i])) {
                    if(!moving) index = sprites.get(indices[i]);
                    moving = true;
                }
            }
            if(moving) {
                active = false;
            } else {
                active = true;
            }
        }

        @Override
        public void onDraw(GraphicsContext g) {
            if(active) super.onDraw(g);
        }

        @Override
        public Element writeXML(Document doc) {
            Element animationComponent = doc.createElement("StandAnimationComponent");

            setXMLAttribute(animationComponent);
            animationComponent.setAttribute("active", String.valueOf(active));

            return animationComponent;
        }

        @Override
        public void readXML(Element e) {
            super.readXML(e);
            active = Boolean.parseBoolean(e.getAttribute("active"));
        }
    }

    /**
     * Component helps to show run left animation
     */
    static class RunLeftAnimationComponent extends AnimationComponent {
        public boolean active = false;

        public RunLeftAnimationComponent(){
            super();
            tag = "RunLeftAnimation";
        }

        public RunLeftAnimationComponent(String spriteTag, Vec2d position, Vec2d size) {
            super(spriteTag, position, size);
        }

        @Override
        public void onTick(long nanosSincePreviousTick) {
            if((gameObject.keyPressing.containsKey(KeyCode.LEFT) && gameObject.keyPressing.get(KeyCode.LEFT))) {
                active = true;
                super.onTick(nanosSincePreviousTick);
            } else {
                active = false;
                sumTime = 0;
            }
        }

        @Override
        public void onDraw(GraphicsContext g) {
            if(active) super.onDraw(g);
        }

        @Override
        public Element writeXML(Document doc) {
            Element animationComponent = doc.createElement("RunLeftAnimationComponent");

            setXMLAttribute(animationComponent);
            animationComponent.setAttribute("active", String.valueOf(active));

            return animationComponent;
        }

        @Override
        public void readXML(Element e) {
            super.readXML(e);
            active = Boolean.parseBoolean(e.getAttribute("active"));
        }
    }

    /**
     * Component helps to show run right animation
     */
    static class RunRightAnimationComponent extends AnimationComponent {
        public boolean active = false;

        public RunRightAnimationComponent() {
            super();
            tag = "RunRightAnimation";
        }

        public RunRightAnimationComponent(String spriteTag, Vec2d position, Vec2d size) {
            super(spriteTag, position, size);
        }

        @Override
        public void onTick(long nanosSincePreviousTick) {
            boolean moving = false;
            if (gameObject.keyPressing.containsKey(direction[0]) && gameObject.keyPressing.get(direction[0])) {
                moving = true;
            }
            if(!moving && (gameObject.keyPressing.containsKey(KeyCode.RIGHT) && gameObject.keyPressing.get(KeyCode.RIGHT))) {
                active = true;
                super.onTick(nanosSincePreviousTick);
            } else {
                active = false;
                sumTime = 0;
            }
        }

        @Override
        public void onDraw(GraphicsContext g) {
            if(active) super.onDraw(g);
        }

        @Override
        public Element writeXML(Document doc) {
            Element animationComponent = doc.createElement("RunRightAnimationComponent");

            setXMLAttribute(animationComponent);
            animationComponent.setAttribute("active", String.valueOf(active));

            return animationComponent;
        }

        @Override
        public void readXML(Element e) {
            super.readXML(e);
            active = Boolean.parseBoolean(e.getAttribute("active"));
        }
    }

    /**
     * Component that control how the character moves.
     */
    static class CharacterMoveComponent extends MoveComponent{

        public CharacterMoveComponent() {
            super();
            tag = "Move";
        }
        @Override
        public void onTick(long nanosSincePreviousTick) {
            double t = nanosSincePreviousTick / 1000000000.0;
            double acceleration = 75;
            double dx[] = {-1, 1}, dy[] = {0, 0};
            KeyCode direction[] = {KeyCode.LEFT, KeyCode.RIGHT};
            for(int k = 0; k < direction.length; k++) {
                if(gameObject.keyPressing.containsKey(direction[k]) && gameObject.keyPressing.get(direction[k])) {
                    PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
                    moveDirection = new Vec2d(dx[k], dy[k]);
                    Vec2d force = moveDirection;
                    Vec2d impulse = force.smult(physicsComponent.getMass() * acceleration * t);
                    if(physicsComponent.getVel().x * impulse.x > 0) {
                        if(physicsComponent.getVel().x + impulse.x / physicsComponent.getMass() > maxVel)
                            impulse = new Vec2d((maxVel - physicsComponent.getVel().x) * physicsComponent.getMass(), 0);
                        else if(physicsComponent.getVel().x + impulse.x / physicsComponent.getMass() < -maxVel) {
                            impulse = new Vec2d((-maxVel - physicsComponent.getVel().x) * physicsComponent.getMass(), 0);
                        }
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

    /**
     * Create a new character object.
     * @param position the original position of the game.
     * @param characterSize the size of the character in the game.
     * @return return the character game object.
     */
    public GameObject createCharacter(Vec2d position, Vec2d characterSize) {

        GameObject characterObject = new GameObject();

        characterObject.setTransformComponent(new TransformComponent(position, characterSize));
        StandAnimationComponent standAnimationComponent = new StandAnimationComponent("characterStand", new Vec2d(0, 0), characterSize);
        for(int i = 0; i <= 3; i++)
            standAnimationComponent.addSprite(new Vec2i(i, 0));
        characterObject.addComponent(standAnimationComponent);

        RunRightAnimationComponent runRightAnimationComponent = new RunRightAnimationComponent("characterRunX", new Vec2d(0, 0), characterSize);
        for(int i = 0; i <= 5; i++)
            runRightAnimationComponent.addSprite(new Vec2i(i, 0));
        RunLeftAnimationComponent runLeftAnimationComponent = new RunLeftAnimationComponent("characterRun-X", new Vec2d(0, 0), characterSize);
        for(int i = 0; i <= 5; i++)
            runLeftAnimationComponent.addSprite(new Vec2i(i, 0));
        characterObject.addComponent(runRightAnimationComponent);
        characterObject.addComponent(runLeftAnimationComponent);

        FireRayComponent fireRayComponent = new FireRayComponent();
        fireRayComponent.setFireKey(KeyCode.X);
        characterObject.addComponent(fireRayComponent);

        FireComponent fireComponent = new FireComponent(60);
        fireComponent.setFireKey(KeyCode.Z);
        for(int i = 0; i < 4; i++)
            fireComponent.addSpriteIndex(new Vec2i(i, 0));
        characterObject.addComponent(fireComponent);

        JumpComponent jumpComponent = new JumpComponent();
        jumpComponent.setJumpKey(KeyCode.SHIFT);
        characterObject.addComponent(jumpComponent);

        CollisionComponent groundDetect = new CollisionComponent(new AABShape(new Vec2d(characterSize.x / 4, characterSize.y), new Vec2d(characterSize.x / 2, 1)), false, false, false, false, false, true);
        characterObject.addComponent(groundDetect);

        GravityComponent gravityComponent = new GravityComponent();
        gravityComponent.setGroundDetect(groundDetect);
        characterObject.addComponent(gravityComponent);

        CharacterMoveComponent characterMoveComponent = new CharacterMoveComponent();
        characterObject.addComponent(characterMoveComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(45, 0);
        characterObject.addComponent(physicsComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, 0), characterSize));
        collisionComponent.setGroup(1);
        characterObject.addComponent(collisionComponent);
        KeyEventsComponent keyEventsComponent = new KeyEventsComponent();
        characterObject.addComponent(keyEventsComponent);
        HealthComponent healthComponent = new HealthComponent(60);
        characterObject.addComponent(healthComponent);

        return characterObject;
    }
}
