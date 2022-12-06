package Final.levels;

import Final.Character;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class Level1 extends Level{
    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        System.out.println("level 1");

        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

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
        GameObject wallSpike2 = createUpwardSpike(new Vec2d(700, 350), spriteSize, 1);
        gameWorld.addGameObject(wallSpike2);

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

        for(double x = 370; x < 605; x += 30) {
            GameObject spike = createUpwardSpike(new Vec2d(x, 480), spriteSize, 1);
            gameWorld.addGameObject(spike);
        }

        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, 370), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
