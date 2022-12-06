package Final;

import Final.levels.Level;
import Final.levels.Level0;
import Final.levels.LevelController;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.collision.PolygonShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class Game {

    protected Vec2d worldSize;
    protected GameWorld gameWorld;
    protected int currentLevelNumber;

    protected XMLProcessor xmlProcessor = new XMLProcessor();

    public Game() {
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public void disposeCurrentGameWorld() {
        if(gameWorld != null) gameWorld.onShutdown();
    }

    public GameWorld createGameWorld(int levelNumber, Vec2d worldSize, Vec2i mapGridNum) {
        Class<? extends Level> level = LevelController.getLevelClass(levelNumber);
        if(level != null) {
            try {
                gameWorld = level.newInstance().createGameWorld(worldSize, mapGridNum);
                currentLevelNumber = levelNumber;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else System.out.println("no level");
        return gameWorld;
    }

    /**
     * Save the game world
     */
    public void save(){
        xmlProcessor.writeXML(gameWorld);
    }

    /**
     * Load previously saved game world
     */
    public void load() {
        gameWorld = xmlProcessor.readXML();
        worldSize = gameWorld.getSize();
    }
}
