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

public class Level3 extends Level{
    public Level3() {
        levelNumber = "3";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(30, 250), spriteSize, 1);
        gameWorld.addGameObject(save);

        for(double x = 205; x < 940; x += 30) {
            GameObject spike = createUpwardSpike(new Vec2d(x, 480), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        GameObject plainTile1 = createPlainTile(new Vec2d(30, 500), spriteSize, 2);
        GameObject plainTile2 = createPlainTile(new Vec2d(145, 390), new Vec2d(spriteSize.x, 2 * spriteSize.y), 2);
        GameObject plainTile3 = createPlainTile(new Vec2d(260, 240), new Vec2d(spriteSize.x, 4 * spriteSize.y), 2);
        GameObject plainTile4 = createPlainTile(new Vec2d(500, 150), new Vec2d(spriteSize.x, 5 * spriteSize.y), 2);

        gameWorld.addGameObject(plainTile1);
        gameWorld.addGameObject(plainTile2);
        gameWorld.addGameObject(plainTile3);
        gameWorld.addGameObject(plainTile4);

        GameObject dashGuide = createGuide(new Vec2d(60, 470),
                spriteSize,
                new Vec2d(spriteSize.x * 8, spriteSize.y * 2.5),
                1,
                "Press X to dash\n" +
                        "Try to control the dash direction\n" +
                        "No more double jump!",
                Font.font(spriteSize.x / 2),
                new Vec2d(5, 25),
                Color.color(0, 0, 0));
        gameWorld.addGameObject(dashGuide);

        for(double x = 600; x < 750 - 2 * spriteSize.x; x += spriteSize.x) {
            GameObject plainTile = createPlainTile(new Vec2d(x, 125), spriteSize, 1);
            gameWorld.addGameObject(plainTile);
        }

        for(double x = 600; x < 750 - 2 * spriteSize.x; x += spriteSize.x) {
            GameObject plainTile = createPlainTile(new Vec2d(x, 30), spriteSize, 1);
            gameWorld.addGameObject(plainTile);
        }

        for(double x = 600; x < 750 - 2 * spriteSize.x; x += spriteSize.x) {
            GameObject spike = createUpwardSpike(new Vec2d(x, 125 - spriteSize.y), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        for(double x = 725; x < 940; x += spriteSize.x) {
            GameObject plainTile = createPlainTile(new Vec2d(x, 150), spriteSize, 1);
            gameWorld.addGameObject(plainTile);
        }

        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, 150 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
