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

public class Level5 extends Level{
    public Level5() {
        levelNumber = "5";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(spriteSize.x * 13, worldSize.y - 5 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(save);

        GameObject plainTile1 = createPlainTile(new Vec2d(spriteSize.x * 13, worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 2);
        gameWorld.addGameObject(plainTile1);

        for(int i = 0; i < 7; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * 13 + spriteSize.x * (i + 2), worldSize.y - 1 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(int i = 0; i < 5; i++) {
            GameObject plainTile = createPlainTile(new Vec2d(spriteSize.x * 13 + spriteSize.x * (i + 9), worldSize.y - 1 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(plainTile);
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * 13 + spriteSize.x * (i + 9), worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(int i = 0; i < 3; i++) {
            GameObject plainTile = createPlainTile(new Vec2d(spriteSize.x * 13 + spriteSize.x * (i + 14), worldSize.y - 3 * spriteSize.y - spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 3), 1);
            gameWorld.addGameObject(plainTile);
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x * 13 + spriteSize.x * (i + 14), worldSize.y - 4 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile2 = createPlainTile(new Vec2d(spriteSize.x * 13 + spriteSize.x * 4, worldSize.y - (6 + 6) * spriteSize.y - spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * 6), 1);
        gameWorld.addGameObject(plainTile2);

        GameObject plainTile3 = createPlainTile(new Vec2d(worldSize.x - spriteSize.x * 2, spriteSize.y), new Vec2d(spriteSize.x, worldSize.y - 2 * spriteSize.y), 1);
        gameWorld.addGameObject(plainTile3);

        GameObject plainTile4 = createPlainTile(new Vec2d(worldSize.x - spriteSize.x * 4, spriteSize.y), new Vec2d(spriteSize.x * 2, spriteSize.y * 9), 1);
        gameWorld.addGameObject(plainTile4);

        GameObject refresh = createRefresh(new Vec2d(worldSize.x - spriteSize.x * 10, spriteSize.y * 9), spriteSize, 1);
        gameWorld.addGameObject(refresh);

        GameObject plainTile5 = createPlainTile(new Vec2d(worldSize.x - spriteSize.x * 5, spriteSize.y * 9), spriteSize, 1);
        gameWorld.addGameObject(plainTile5);

        GameObject plainTile6 = createPlainTile(new Vec2d(spriteSize.x * 13 + spriteSize.x * 5, worldSize.y - 11 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(plainTile6);
        GameObject spike1 = createUpwardSpike(new Vec2d(spriteSize.x * 13 + spriteSize.x * 5, worldSize.y - 12 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike1);
        GameObject spike2 = createDownWardSpike(new Vec2d(spriteSize.x * 13 + spriteSize.x * 5, worldSize.y - 10 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(spike2);

        GameObject plainTile7 = createPlainTile(new Vec2d(spriteSize.x * 13 + spriteSize.x * 3, worldSize.y - 12 * spriteSize.y - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(plainTile7);

        GameObject plainTile8 = createPlainTile(new Vec2d(spriteSize.x, worldSize.y - spriteSize.y - spriteSize.y), new Vec2d(spriteSize.x * 10, spriteSize.y), 1);
        gameWorld.addGameObject(plainTile8);

        GameObject plainTile9 = createPlainTile(new Vec2d(spriteSize.x, spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * (mapGridNum.y - 2)), 1);
        gameWorld.addGameObject(plainTile9);

        GameObject plainTile10 = createPlainTile(new Vec2d(spriteSize.x * 2, spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * (mapGridNum.y - 5)), 1);
        gameWorld.addGameObject(plainTile10);

        GameObject plainTile11 = createPlainTile(new Vec2d(spriteSize.x * 3, spriteSize.y), new Vec2d(spriteSize.x, spriteSize.y * (mapGridNum.y - 8)), 1);
        gameWorld.addGameObject(plainTile11);

        for(int i = 0; i < 9; i++) {
            GameObject spike = createUpwardSpike(new Vec2d(spriteSize.x + spriteSize.x * (i + 1), worldSize.y - 2 * spriteSize.y - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        {
            GameObject tempPlainTile0 = createPlainTile(new Vec2d(spriteSize.x * 12, worldSize.y - spriteSize.y * 11 - spriteSize.y), spriteSize, 5);
            gameWorld.addGameObject(tempPlainTile0);
            GameObject tempPlainTile1 = createPlainTile(new Vec2d(spriteSize.x * 11, worldSize.y - spriteSize.y * 6 - spriteSize.y), new Vec2d(spriteSize.x * 2, spriteSize.y * 6), 1);
            gameWorld.addGameObject(tempPlainTile1);
            GameObject tempPlainTile2 = createPlainTile(new Vec2d(spriteSize.x * 6, spriteSize.y), new Vec2d(spriteSize.x * 3, spriteSize.y * 10), 1);
            gameWorld.addGameObject(tempPlainTile2);

            GameObject tempSpike0 = createUpwardSpike(new Vec2d(spriteSize.x * 11, worldSize.y - spriteSize.y * 7 - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(tempSpike0);
            for(int i = 0; i < 4; i++) {
                GameObject spike = createUpwardSpike( new Vec2d(spriteSize.x * (12 + i), worldSize.y - spriteSize.y * 12 - spriteSize.y), spriteSize, 1);
                gameWorld.addGameObject(spike);
            }
        }

        Character character = new Character(new Vec2d(spriteSize.x * 13, worldSize.y - 4 * spriteSize.y - spriteSize.y), spriteSize);
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

        GameObject flag = createFlag(new Vec2d(spriteSize.x * 5, worldSize.y / 2 - 2 * spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
