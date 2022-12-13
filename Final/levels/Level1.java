package Final.levels;

import Final.Character;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.input.KeyCode;

public class Level1 extends Level{

    public Level1() {
        levelNumber = "1";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(50, 360), spriteSize, 1);
        gameWorld.addGameObject(save);

        GameObject plainTile1 = createPlainTile(new Vec2d(spriteSize.x, worldSize.y - spriteSize.y * 5), spriteSize, 4);
        gameWorld.addGameObject(plainTile1);

        for(int i = 5; i < 10; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * i, worldSize.y - spriteSize.y * 3), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(int i = 10; i < 12; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * i, worldSize.y - spriteSize.y * 2), spriteSize, 1);
            CollisionComponent spikeCollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), spriteSize), false, false, false, false, false, true);
            spike.addComponent(spikeCollisionComponent);
            TrapComponent spikeTrap = new TrapComponent() {
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

        GameObject plainTile2 = createPlainTile(new Vec2d(spriteSize.x * 5, worldSize.y - spriteSize.y * 2), new Vec2d(spriteSize.x * 7, spriteSize.y), 1);
        gameWorld.addGameObject(plainTile2);

        GameObject plainTile7 = createPlainTile(new Vec2d(spriteSize.x * 5, worldSize.y - spriteSize.y * 7 - spriteSize.y / 2), new Vec2d(spriteSize.x * 2, spriteSize.y), 1);
        SpriteComponent plainTile7SpriteComponent = (SpriteComponent)plainTile7.getComponent("Sprite");
        plainTile7SpriteComponent.setShow(false);
        CollisionComponent plainTile7CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(-3, -3), new Vec2d(spriteSize.x * 2 + 6, spriteSize.y + 6)), false, false, false, false, false, true);
        plainTile7CollisionComponent.setGroup(2);
        plainTile7.addComponent(plainTile7CollisionComponent);
        TrapComponent plainTile7Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                SpriteComponent spriteComponent = (SpriteComponent)gameObject.getComponent("Sprite");
                spriteComponent.setShow(true);
                super.doTrap();
            }
        };
        plainTile7Trap.setDetect(plainTile7CollisionComponent);
        plainTile7.addComponent(plainTile7Trap);
        gameWorld.addGameObject(plainTile7);

        for(int i = 0; i < 2; i++) {
            GameObject plainTile = createPlainTile(new Vec2d(spriteSize.x * 6, worldSize.y - spriteSize.y * (6 - i) - spriteSize.y / 2), spriteSize, 1);
            SpriteComponent plainTileSpriteComponent = (SpriteComponent)plainTile.getComponent("Sprite");
            plainTileSpriteComponent.setShow(false);
            CollisionComponent plainTileCollisionComponent = new CollisionComponent(new AABShape(new Vec2d(-3, -3), spriteSize.plus(6, 6)), false, false, false, false, false, true);
            plainTileCollisionComponent.setGroup(i + 2);
            plainTile.addComponent(plainTileCollisionComponent);
            TrapComponent plainTileTrap = new TrapComponent() {
                @Override
                protected void doTrap() {
                    SpriteComponent spriteComponent = (SpriteComponent)gameObject.getComponent("Sprite");
                    spriteComponent.setShow(true);
                    super.doTrap();
                }
            };
            plainTileTrap.setDetect(plainTileCollisionComponent);
            plainTile.addComponent(plainTileTrap);
            gameWorld.addGameObject(plainTile);
        }

        for(int i = 0; i < 3; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (12 + i), worldSize.y - spriteSize.y * 5), spriteSize, 1);
            CollisionComponent spikeCollisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y), spriteSize), false, false, false, false, false, true);
            spike.addComponent(spikeCollisionComponent);
            TrapComponent spikeTrap = new TrapComponent() {
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

        GameObject plainTile3 = createPlainTile(new Vec2d(spriteSize.x * 12, worldSize.y - spriteSize.y * 5), spriteSize, 5);
        gameWorld.addGameObject(plainTile3);

        for(int x = 17; x <= 23; x += 6) {
            GameObject plain = createPlainTile(new Vec2d(spriteSize.x * x, worldSize.y - spriteSize.y * 6), new Vec2d(spriteSize.x, spriteSize.y * 6), 1);
            gameWorld.addGameObject(plain);

            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * x, worldSize.y - spriteSize.y * 7), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile4 = createPlainTile(new Vec2d(spriteSize.x * 18, worldSize.y - spriteSize.y * 3), new Vec2d(spriteSize.x * 5, spriteSize.y * 2), 1);
        gameWorld.addGameObject(plainTile4);

        GameObject spike1 = createUpwardSpike(new Vec2d(spriteSize.x * 18, worldSize.y - spriteSize.y * 4), spriteSize, 1);
        CollisionComponent spike1CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(spriteSize.x, 0), new Vec2d(spriteSize.x * 3, spriteSize.y)), false, false, false, false, false, true);
        spike1CollisionComponent.setGroup(2);
        spike1.addComponent(spike1CollisionComponent);
        TrapComponent spike1Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(1, 0), 300);
                movingComponent.setDistance(spriteSize.x * 4);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        spike1Trap.setDetect(spike1CollisionComponent);
        spike1.addComponent(spike1Trap);
        gameWorld.addGameObject(spike1);

        GameObject spike2 = createUpwardSpike(new Vec2d(spriteSize.x * 22, worldSize.y - spriteSize.y * 4), spriteSize, 1);
        CollisionComponent spike2CollisionComponent = new CollisionComponent(new AABShape(new Vec2d(-spriteSize.x * 3, 0), new Vec2d(spriteSize.x * 3, spriteSize.y)), false, false, false, false, false, true);
        spike2CollisionComponent.setGroup(3);
        spike2.addComponent(spike2CollisionComponent);
        TrapComponent spike2Trap = new TrapComponent() {
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(-1, 0), 300);
                movingComponent.setDistance(spriteSize.x * 4);
                gameObject.addComponentQueue(movingComponent);
                super.doTrap();
            }
        };
        spike2Trap.setDetect(spike2CollisionComponent);
        spike2.addComponent(spike2Trap);
        gameWorld.addGameObject(spike2);

        GameObject plainTile5 = createPlainTile(new Vec2d(spriteSize.x * 25, worldSize.y - spriteSize.y * 7), new Vec2d(spriteSize.x * 3, spriteSize.y * 2), 1);
        gameWorld.addGameObject(plainTile5);

        for(int i = 25; i < 28; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * i, worldSize.y - spriteSize.y * 8), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(int i = 25; i < 28; i +=2 ) {
            GameObject spike = createDownWardSpike(new Vec2d(spriteSize.x * i, worldSize.y - spriteSize.y * 5), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile6 = createPlainTile(new Vec2d(spriteSize.x * 24, worldSize.y - spriteSize.y * 2), new Vec2d(spriteSize.x * 7, spriteSize.y), 1);
        gameWorld.addGameObject(plainTile6);

        GameObject spike3 = createUpwardSpike(new Vec2d(spriteSize.x * 26, worldSize.y - spriteSize.y * 3), spriteSize, 1);
        gameWorld.addGameObject(spike3);

        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();
        FireComponent fireComponent = new FireComponent(60);
        fireComponent.setFireKey(KeyCode.Z);
        for(int i = 0; i < 4; i++)
            fireComponent.addSpriteIndex(new Vec2i(i, 0));
        characterObject.addComponent(fireComponent);
        ((JumpComponent)(characterObject.getComponent("Jump"))).setMaxJumpTime(2);
        ((DashComponent)(characterObject.getComponent("Dash"))).setMaxDashTime(0);

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, worldSize.y - spriteSize.y * 3), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
