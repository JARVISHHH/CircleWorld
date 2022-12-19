package Final.levels;

import Final.Character;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.components.ClimbComponent;
import engine.game.components.CollisionComponent;
import engine.game.components.JumpComponent;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Level4 extends Level{
    public Level4() {
        levelNumber = "4";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(35, 325), spriteSize, 1);
        gameWorld.addGameObject(save);

        GameObject climbGuide = createGuide(new Vec2d(100, worldSize.y - 4 * spriteSize.y - spriteSize.y),
                spriteSize,
                new Vec2d(spriteSize.x * 6.2, spriteSize.y * 2),
                1,
                "Press Z to climb the wall\n" +
                        "I will get tired!",
                Font.font(spriteSize.x / 2),
                new Vec2d(5, 25),
                Color.color(0, 0, 0));

        GameObject plainTile1 = createPlainTile(new Vec2d(spriteSize.x, worldSize.y - 3 * spriteSize.y - spriteSize.y), spriteSize, 3);
        gameWorld.addGameObject(plainTile1);

        for(int i = 0; i < 4; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (4 + i), worldSize.y - spriteSize.y * 2), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile2 = createPlainTile(new Vec2d(spriteSize.x * 8, worldSize.y - spriteSize.y * 8 - spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 8), 1);
        gameWorld.addGameObject(plainTile2);
        GameObject spike1 = createUpwardSpike(new Vec2d(spriteSize.x * 8, worldSize.y - spriteSize.y * 9 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike1);

        GameObject plainTile3 = createPlainTile(new Vec2d(spriteSize.x * 9, worldSize.y - spriteSize.y * 9 - spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 9), 1);
        gameWorld.addGameObject(plainTile3);
        GameObject spike2 = createUpwardSpike(new Vec2d(spriteSize.x * 9, worldSize.y - spriteSize.y * 10 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike2);

        GameObject plainTile4 = createPlainTile(new Vec2d(spriteSize.x, spriteSize.y), new Vec2d(spriteSize.x * 5.5, worldSize.y - spriteSize.y * 9 - spriteSize.y), 1);
        gameWorld.addGameObject(plainTile4);

        GameObject plainTile5 = createPlainTile(new Vec2d(spriteSize.x * 10, worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 5);
        gameWorld.addGameObject(plainTile5);
        for(int i = 0; i < 5; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (10 + i), worldSize.y - 3 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile6 = createPlainTile(new Vec2d(spriteSize.x * 15, worldSize.y - 6 * spriteSize.y - spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 6), 1);
        gameWorld.addGameObject(plainTile6);
        GameObject spike3 = createUpwardSpike(new Vec2d(spriteSize.x * 15, worldSize.y - 7 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike3);

        GameObject plainTile7 = createPlainTile(new Vec2d(spriteSize.x * 16, worldSize.y - 1 * spriteSize.y - spriteSize.y), spriteSize, 3);
        gameWorld.addGameObject(plainTile7);
        for(int i = 0; i < 3; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (16 + i), worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile8 = createPlainTile(new Vec2d(spriteSize.x * 19, worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 3);
        gameWorld.addGameObject(plainTile8);
        for(int i = 0; i < 3; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (19 + i), worldSize.y - 3 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile9 = createPlainTile(new Vec2d(spriteSize.x * 22, worldSize.y - 1 * spriteSize.y - spriteSize.y), spriteSize, 3);
        gameWorld.addGameObject(plainTile9);
        for(int i = 0; i < 3; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * (22 + i), worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(float x = (float)spriteSize.x * 25; x < worldSize.x - spriteSize.x; x += spriteSize.x) {
            GameObject spike = createUpwardSpike(new Vec2d(x, worldSize.y - spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile10 = createPlainTile(new Vec2d(spriteSize.x * 24, worldSize.y - 9 * spriteSize.y), spriteSize, 4);
        gameWorld.addGameObject(plainTile10);

        GameObject plainTile11 = createPlainTile(new Vec2d(spriteSize.x * 27, worldSize.y - 13 * spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 2), 4);
        gameWorld.addGameObject(plainTile11);

        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();
        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(-0.1, character.getCharacterSize().y / 5 * 2), new Vec2d(character.getCharacterSize().x + 0.2, character.getCharacterSize().y / 5)), false, false, false, false, false, true);
        characterObject.addComponent(collisionComponent);
        ClimbComponent climbComponent = new ClimbComponent();
        climbComponent.setDetect(collisionComponent);
        characterObject.addComponent(climbComponent);
        JumpComponent jumpComponent = (JumpComponent)characterObject.getComponent("Jump");
        jumpComponent.addDetect(collisionComponent);
        climbComponent.setGroundDetect(jumpComponent.getDetect(0));

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, 150 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(climbGuide);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
