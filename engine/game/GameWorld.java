package engine.game;

import engine.game.systems.*;
import engine.support.Vec2d;
import engine.uikit.ViewPort;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.sound.sampled.*;
import java.util.*;

public class GameWorld {
    protected boolean debugMode = false;
    protected ViewPort viewPort;  // Which viewport it belongs to
    protected Vec2d size;  // Size of the game world

    protected GameObject centerGameObject;

    protected long maxTickTime = 5000000;
    public int currentZIndex = 1;
    protected Affine affine;  // Transform coordinates from screen space to game space
    protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();  // All gameObjects the game world has
    protected Queue<GameObject> addQueue = new LinkedList<GameObject>();  // Game objects waiting to be added to the game world
    protected Queue<GameObject> removeQueue = new LinkedList<GameObject>();  // Game objects waiting to be removed from the game world

    protected HashMap<KeyCode, Boolean> inputState = new HashMap<KeyCode, Boolean>();  // Unused for now

    protected GraphicsSystem graphicsSystem = new GraphicsSystem();
    protected TickSystem tickSystem = new TickSystem();
    protected MouseEventsSystem mouseEventsSystem = new MouseEventsSystem();
    protected KeyEventsSystem keyEventsSystem = new KeyEventsSystem();

    protected CollisionSystem collisionSystem = new CollisionSystem();

    protected boolean hasResult = false;
    protected boolean win = false;

    protected Clip audioClip = null;

    public GameWorld(Vec2d size) {
        this.size = size;
        onStartUp();
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    public void setAffine() {
        affine = new Affine();
        affine.appendTranslation(viewPort.getGamePosition().x, viewPort.getGamePosition().y);
        affine.appendScale(1 / viewPort.getScale().getX(), 1 / viewPort.getScale().getY());
        affine.appendTranslation(-viewPort.getPosition().x, -viewPort.getPosition().y);
    }

    public void setCenterGameObject(GameObject centerGameObject) {
        this.centerGameObject = centerGameObject;
    }

    public Affine getAffine() {
        return affine;
    }

    public Vec2d getSize() {
        return size;
    }

    public GameObject getCenterGameObject() {
        return centerGameObject;
    }

    public boolean isHasResult() {
        return hasResult;
    }

    public boolean isWin() {
        return win;
    }

    public void addGameObject(GameObject gameObject) {
        gameObject.setActive(true);
        addQueue.offer(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObject.setActive(false);
        removeQueue.offer(gameObject);
    }

    public Element writeXML(Document doc) {
        Element gameWorld = doc.createElement("GameWorld");
        gameWorld.setAttribute("size", String.valueOf(size));

        for(GameObject gameObject: gameObjects) {
            if(gameObject == centerGameObject) {
                gameWorld.appendChild(gameObject.writeXML(doc, "CenterGameObject"));
            } else {
                gameWorld.appendChild(gameObject.writeXML(doc, "GameObject" + String.valueOf(gameObjects.indexOf(gameObject))));
            }
        }
        return gameWorld;
    }

    public void onStartUp() {
        if(audioClip == null) audioClip = Sound.getAudio("BackGround");
        if(audioClip != null) {
            FloatControl gainControl =
                    (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f); // Reduce volume by 10 decibels.
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void onDraw(GraphicsContext g) {
        graphicsSystem.onDraw(g);
    }

    protected void addGameObjects() {
        // Add game objects to the game world
        while(!addQueue.isEmpty()) {
            GameObject gameObject = addQueue.poll();
            gameObjects.add(gameObject);
            gameObject.setGameWorld(this);
            gameObject.zIndex = currentZIndex++;
            if(gameObject.isTickable()) {
                tickSystem.addGameObject(gameObject);
            }
            if(gameObject.isDrawable()) {
                graphicsSystem.addGameObject(gameObject);
            }
            if(gameObject.doResponseMouseEvents()) {
                mouseEventsSystem.addGameObject(gameObject);
            }
            if(gameObject.checkCollide()) {
                collisionSystem.addGameObject(gameObject);
            }
            if(gameObject.doResponseKeyEvents()) {
                keyEventsSystem.addGameObject(gameObject);
            }
        }
    }

    protected void removeGameObjects() {
        // Remove game objects from the game world
        while(!removeQueue.isEmpty()) {
            GameObject gameObject = removeQueue.poll();
            if(gameObject.isTickable()) {
                tickSystem.removeGameObject(gameObject);
            }
            if(gameObject.isDrawable()) {
                graphicsSystem.removeGameObject(gameObject);
            }
            if(gameObject.doResponseMouseEvents()) {
                mouseEventsSystem.removeGameObject(gameObject);
            }
            if(gameObject.checkCollide()) {
                collisionSystem.removeGameObject(gameObject);
            }
            if(gameObject.doResponseKeyEvents()) {
                keyEventsSystem.removeGameObject(gameObject);
            }
        }
    }

    protected void updateGameObjects() {
        addGameObjects();
        removeGameObjects();
    }

    public void onTick(long nanosSincePreviousTick) {
        while(nanosSincePreviousTick > 0) {
            updateGameObjects();
            if(nanosSincePreviousTick > maxTickTime) {
                collisionSystem.onTick(maxTickTime);
                updateGameObjects();
                tickSystem.onTick(maxTickTime);
                updateGameObjects();
                nanosSincePreviousTick -= maxTickTime;
            } else {
                collisionSystem.onTick(nanosSincePreviousTick);
                updateGameObjects();
                tickSystem.onTick(nanosSincePreviousTick);
                updateGameObjects();
                nanosSincePreviousTick = 0;
            }
        }
    }

    public void onKeyPressed(KeyEvent e) {
        keyEventsSystem.onKeyPressed(e);
    }

    public void onKeyReleased(KeyEvent e) {
        keyEventsSystem.onKeyReleased(e);
    }

    public void onMouseDragged(MouseEvent e) {
        mouseEventsSystem.onMouseDragged(e);
    }

    public void onMouseClicked(MouseEvent e) {
        mouseEventsSystem.onMouseClicked(e);
    }

    public void onMousePressed(MouseEvent e) {
        mouseEventsSystem.onMousePressed(e);
    }

    public void onMouseReleased(MouseEvent e) {
        mouseEventsSystem.onMouseReleased(e);
        collisionSystem.onMouseReleased(e);
    }

    public void onShutdown() {
        if(audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0);
        }
        for(GameObject gameObject: gameObjects)
            gameObject.onShutdown();
    }
}
