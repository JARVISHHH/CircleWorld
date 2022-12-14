package Final.levels;

import Final.Character;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Level0 extends Level{

    public Level0() {
        levelNumber = "0";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(50, 250), spriteSize, 1);
        gameWorld.addGameObject(save);

        GameObject jumpGuide = createGuide(new Vec2d(150, 360),
                                       spriteSize,
                                       new Vec2d(spriteSize.x * 4.5, spriteSize.y * 2),
                                       1,
                                       "Press c to jump\n" +
                                               "Try double jump",
                                       Font.font(spriteSize.x / 2),
                                       new Vec2d(5, 25),
                                       Color.color(0, 0, 0));
        gameWorld.addGameObject(jumpGuide);

        GameObject fireGuide = createGuide(new Vec2d(210, 360),
                spriteSize,
                new Vec2d(spriteSize.x * 6.5, spriteSize.y * 2),
                1,
                "Press z to fire\n" +
                "Try to destroy some walls",
                Font.font(spriteSize.x / 2),
                new Vec2d(5, 25),
                Color.color(0, 0, 0));
        gameWorld.addGameObject(fireGuide);

        for(double y = 20; y < 260; y += 30) {
            GameObject wall = createIndestructibleWall(new Vec2d(700, y), spriteSize, 1);
            gameWorld.addGameObject(wall);
        }

        GameObject wall = createIndestructibleWall(new Vec2d(700, 380), spriteSize, 1);
        gameWorld.addGameObject(wall);

        GameObject wall1 = createDestructibleWall(new Vec2d(700, 290), spriteSize, 1);
        gameWorld.addGameObject(wall1);
        GameObject wall2 = createDestructibleWall(new Vec2d(700, 320), spriteSize, 1);
        gameWorld.addGameObject(wall2);

        GameObject wallSpike1 = createDownWardSpike(new Vec2d(700, 260), spriteSize, 1);
        gameWorld.addGameObject(wallSpike1);

        GameObject wallSpike3 = createUpwardSpike(new Vec2d(700, 400), spriteSize, 1);
        gameWorld.addGameObject(wallSpike3);

        GameObject wallSpike2 = createUpwardSpike(new Vec2d(700, 350), spriteSize, 1);
        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y * 2), new Vec2d(spriteSize.x, 2 * spriteSize.y)), false, false, false, false, false, true);
        collisionComponent.setGroup(2);
        wallSpike2.addComponent(collisionComponent);
        TrapComponent trapComponent = new TrapComponent(){
            @Override
            protected void doTrap() {
                MovingComponent movingComponent = new MovingComponent(new Vec2d(0, -1), 200);
                gameObject.addComponentQueue(movingComponent);
                CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, -spriteSize.y * 2), new Vec2d(spriteSize.x, 2 * spriteSize.y)), false, false, false, false, false, true);
                collisionComponent.setGroup(3);
                wallSpike3.addComponentQueue(collisionComponent);
                TrapComponent trapComponent1 = new TrapComponent() {
                    @Override
                    protected void doTrap() {
                        MovingComponent movingComponent = new MovingComponent(new Vec2d(0, -1), 300);
                        movingComponent.setDistance(50);
                        gameObject.addComponentQueue(movingComponent);
                    }
                };
                trapComponent1.setDetect(collisionComponent);
                wallSpike3.addComponentQueue(trapComponent1);
            }
        };
        trapComponent.setDetect(collisionComponent);
        wallSpike2.addComponent(trapComponent);
        gameWorld.addGameObject(wallSpike2);

        for(double x = 370; x < 605; x += 30) {
            GameObject spike = createUpwardSpike(new Vec2d(x, 480), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile1 = createPlainTile(new Vec2d(30, 390), spriteSize, 4);
        GameObject plainTile2 = createPlainTile(new Vec2d(145, 390), spriteSize, 4);
        GameObject plainTile3 = createPlainTile(new Vec2d(260, 390), spriteSize, 4);
        GameObject plainTile6 = createPlainTile(new Vec2d(605, 390), spriteSize, 4);
        GameObject plainTile7 = createPlainTile(new Vec2d(720, 390), spriteSize, 4);
        GameObject plainTile8 = createPlainTile(new Vec2d(835, 390), spriteSize, 4);

        gameWorld.addGameObject(plainTile1);
        gameWorld.addGameObject(plainTile2);
        gameWorld.addGameObject(plainTile3);
        gameWorld.addGameObject(plainTile6);
        gameWorld.addGameObject(plainTile7);
        gameWorld.addGameObject(plainTile8);

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

        GameObject flag = createFlag(new Vec2d(900, 370), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
