package Final.levels;

import Final.Character;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.input.KeyCode;

public class Level2 extends Level{
    public Level2() {
        levelNumber = "2";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        for(double x = 30 + spriteSize.x * 5; x < 840; x += spriteSize.x) {
            GameObject spike = createUpwardSpike(new Vec2d(x, worldSize.y - spriteSize.y * 2), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject save = createSave(new Vec2d(50, 300), spriteSize, 1);
        gameWorld.addGameObject(save);

        GameObject plainTile1 = createPlainTile(new Vec2d(30, 450), spriteSize, 5);
        gameWorld.addGameObject(plainTile1);

        GameObject plainTile2 = createPlainTile(new Vec2d(260, 390), spriteSize, 2);
        gameWorld.addGameObject(plainTile2);

        GameObject spike1 = createUpwardSpike(new Vec2d(290, 360), spriteSize, 1);
        CollisionComponent spike1CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y * 10),  new Vec2d(spriteSize.x, spriteSize.y * 5)), false, false, false, false, false, true);
        spike1CollisionComponent.setGroup(2);
        spike1.addComponent(spike1CollisionComponent);
        TrapComponent spike1Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(0, -1), 1000);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        spike1Trap.setDetect(spike1CollisionComponent);
        spike1.addComponent(spike1Trap);
        gameWorld.addGameObject(spike1);

        GameObject spike0 = createUpwardSpike(new Vec2d(390, 350), spriteSize, 1);
        CollisionComponent spike0CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), spriteSize), false, false, false, false, false, true);
        spike0CollisionComponent.setGroup(3);
        spike0.addComponent(spike0CollisionComponent);
        TrapComponent spike0Trap = new TrapComponent(){
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(0, -1), 300);
                movingComponent.setDistance(spriteSize.y);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        spike0Trap.setDetect(spike0CollisionComponent);
        spike0.addComponent(spike0Trap);
        gameWorld.addGameObject(spike0);

        GameObject plainTile3 = createPlainTile(new Vec2d(360, 350), spriteSize, 2);
        CollisionComponent plainTail3CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), spriteSize), false, false, false, false, false, true);
        plainTail3CollisionComponent.setGroup(2);
        plainTile3.addComponent(plainTail3CollisionComponent);
        TrapComponent plainTile3Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(1, 0), 300);
                movingComponent.setDistance(spriteSize.x);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        plainTile3Trap.setDetect(plainTail3CollisionComponent);
        plainTile3.addComponent(plainTile3Trap);
        gameWorld.addGameObject(plainTile3);

        GameObject spike2 = createUpwardSpike(new Vec2d(390, 320), spriteSize, 1);
        CollisionComponent spike2CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(spriteSize.x, spriteSize.y / 3 * 2), new Vec2d(spriteSize.x, spriteSize.y / 3)), false, false, false, false, false, true);
        spike2.addComponent(spike2CollisionComponent);
        TrapComponent spike2Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(1, 0), 100);
                movingComponent.setDistance(spriteSize.x);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        spike2Trap.setDetect(spike2CollisionComponent);
        spike2.addComponent(spike2Trap);
        gameWorld.addGameObject(spike2);

        GameObject[] downSpikes4 = {createDownWardSpike(new Vec2d(500, spriteSize.y), spriteSize, 1), createDownWardSpike(new Vec2d(500 + spriteSize.x, spriteSize.y), spriteSize, 1)};
        GameObject plainTile4 = createPlainTile(new Vec2d(500, 270), spriteSize, 2);
        CollisionComponent plainTile4Collision = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), new Vec2d(spriteSize.x * 2, spriteSize.y)), false, false, false, false, false, true);
        plainTile4.addComponent(plainTile4Collision);
        TrapComponent plainTile4Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                PhysicsComponent physicsComponent = (PhysicsComponent)gameObject.getComponent("Physics");
                if(physicsComponent != null) physicsComponent.applyImpulse(new Vec2d(0, -20 * physicsComponent.getMass()));
                for(GameObject spike: downSpikes4) {
                    gameWorld.addGameObject(spike);
                }
                super.doTrap();
            }
        };
        plainTile4Trap.setDetect(plainTile4Collision);
        plainTile4.addComponent(plainTile4Trap);
        gameWorld.addGameObject(plainTile4);

        GameObject plainTile6 = createPlainTile(new Vec2d(650, 100), spriteSize, 2);
        gameWorld.addGameObject(plainTile6);
        GameObject spike3 = createUpwardSpike(new Vec2d(650, 100 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike3);

        GameObject plainTile7 = createPlainTile(new Vec2d(840, 100), new Vec2d(spriteSize.x, spriteSize.y * 10), 3);
        gameWorld.addGameObject(plainTile7);

        GameObject plainTile8 = createPlainTile(new Vec2d(840, spriteSize.y), new Vec2d(spriteSize.x, 100 - spriteSize.y), 1);
        SpriteComponent plainTile8SpriteComponent = (SpriteComponent)plainTile8.getComponent("Sprite");
        plainTile8SpriteComponent.setShow(false);
        CollisionComponent plainTile8CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(-3, 0), new Vec2d(3, 100 - spriteSize.y)), false, false, false, false, false, true);
        plainTile8.addComponent(plainTile8CollisionComponent);
        TrapComponent plainTile8Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                SpriteComponent spriteComponent = (SpriteComponent)gameObject.getComponent("Sprite");
                spriteComponent.setShow(true);
                createRealFlag(spriteSize);
                super.doTrap();
            }
        };
        plainTile8Trap.setDetect(plainTile8CollisionComponent);
        plainTile8.addComponent(plainTile8Trap);
        gameWorld.addGameObject(plainTile8);

        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();
//        FireComponent fireComponent = new FireComponent(60);
//        fireComponent.setFireKey(KeyCode.Z);
//        for(int i = 0; i < 4; i++)
//            fireComponent.addSpriteIndex(new Vec2i(i, 0));
//        characterObject.addComponent(fireComponent);
        ((JumpComponent)(characterObject.getComponent("Jump"))).setMaxJumpTime(2);
        ((DashComponent)(characterObject.getComponent("Dash"))).setMaxDashTime(0);

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, 100 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }

    private void createRealFlag(Vec2d spriteSize) {
        for(int i = 0; i <= 2; i += 2) {
            GameObject spike = createUpwardSpike(new Vec2d(390 + spriteSize.x * i, 200), spriteSize, 1);
            CollisionComponent spikeCollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), spriteSize), false, false, false, false, false, true);
            spike.addComponent(spikeCollisionComponent);
            TrapComponent spikeTrap = new TrapComponent(){
                @Override
                protected void doTrap() {
                    MovingComponent movingComponent = new MovingComponent(new Vec2d(0, -1), 300);
                    movingComponent.setDistance(spriteSize.y);
                    gameObject.addComponentQueue(movingComponent);
                    super.doTrap();
                }
            };
            spikeTrap.setDetect(spikeCollisionComponent);
            spike.addComponent(spikeTrap);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile1 = createPlainTile(new Vec2d(390, 200), spriteSize, 3);
        gameWorld.addGameObject(plainTile1);

        GameObject plainTile2 = createPlainTile(new Vec2d(30, 150), new Vec2d(spriteSize.x * 5.5, spriteSize.y * 4), 1);
        gameWorld.addGameObject(plainTile2);

        for(int i = 0; i < 2; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(260 + spriteSize.x * i, 200), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile3 = new GameObject();
        plainTile3.setTransformComponent(new TransformComponent(new Vec2d(260, 200), new Vec2d(spriteSize.x * 2, spriteSize.y)));
        SpriteComponent spriteComponent = new SpriteComponent("tile1", new Vec2d(0, 0), new Vec2d(spriteSize.x * 2, spriteSize.y), new Vec2i(0, 0));
        plainTile3.addComponent(spriteComponent);
        CollisionComponent plainTile3CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y / 2), new Vec2d(spriteSize.x * 2, spriteSize.y / 2 * 3)), false, false, false,false, false, true);
        plainTile3CollisionComponent.setGroup(2);
        plainTile3.addComponent(plainTile3CollisionComponent);
        TrapComponent plainTile3Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                SpriteComponent spriteComponent = (SpriteComponent)gameObject.getComponent("Sprite");
                spriteComponent.setShow(false);
                super.doTrap();
            }
        };
        plainTile3Trap.setDetect(plainTile3CollisionComponent);
        plainTile3.addComponent(plainTile3Trap);
        gameWorld.addGameObject(plainTile3);

        GameObject[] downSpikes4 = {createDownWardSpike(new Vec2d(750, spriteSize.y), spriteSize, 1), createDownWardSpike(new Vec2d(750 + spriteSize.x, spriteSize.y), spriteSize, 1)};
        GameObject plainTile4 = createPlainTile(new Vec2d(750, 400), spriteSize, 2);
        CollisionComponent plainTile4Collision = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), new Vec2d(spriteSize.x * 2, spriteSize.y)), false, false, false, false, false, true);
        plainTile4.addComponent(plainTile4Collision);
        TrapComponent plainTile4Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                PhysicsComponent physicsComponent = (PhysicsComponent)gameObject.getComponent("Physics");
                if(physicsComponent != null) physicsComponent.applyImpulse(new Vec2d(0, -20 * physicsComponent.getMass()));
                for(GameObject spike: downSpikes4) {
                    gameWorld.addGameObject(spike);
                }
                super.doTrap();
            }
        };
        plainTile4Trap.setDetect(plainTile4Collision);
        plainTile4.addComponent(plainTile4Trap);
        gameWorld.addGameObject(plainTile4);

        GameObject flag = createFlag(new Vec2d(30, 150 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
    }
}
