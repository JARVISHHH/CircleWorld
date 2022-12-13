package Final;

import Final.levels.Level;
import Final.levels.Level0;
import Final.levels.LevelController;
import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.Sound;
import engine.game.collision.AABShape;
import engine.game.collision.PolygonShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Game {

    protected Vec2d worldSize;
    protected GameWorld gameWorld;
    protected int currentLevelNumber;

    protected Clip audioClip = null;

    protected XMLProcessor xmlProcessor = new XMLProcessor();

    public void onStartup() {
        if(audioClip == null) audioClip = Sound.getAudio("BackGround");
        if(audioClip != null) {
            FloatControl gainControl =
                    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
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

    public void onShutdown() {
        if(audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0);
        }
    }
}
