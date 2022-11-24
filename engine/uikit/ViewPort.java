package engine.uikit;

import engine.game.GameWorld;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;

import java.util.ArrayList;

public class ViewPort extends UIElement{

    protected Vec2d gamePosition;  // Upper left position of the game
    protected Affine affine;  // Transform coordinates from game space to screen space

    protected Scale scale;  // Scale between game space and screen space
    protected double maxScale = 1;

    protected double zoomFactor = 1.5;

    protected ArrayList<Boolean> move = new ArrayList<Boolean>();  // If arrow keys up, down, left, right is pressed
    protected double moveRate = 100;

    protected GameWorld gameWorld;

    protected boolean selfMovable = false;

    KeyCode direction[] = {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT};

    public ViewPort(Vec2d position, Vec2d size, Vec2d gamePosition, Scale scale) {
        this.position = position;
        this.size = size;
        this.gamePosition = gamePosition;
        this.scale = scale;
        setAffine();
        for(int i = 0; i < 4; i++)
            move.add(false);
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        gameWorld.setViewPort(this);
        gameWorld.setAffine();
    }

    // Update affine
    public void setAffine() {
        affine = new Affine();
        affine.appendTranslation(position.x, position.y);
        affine.append(scale);
        affine.appendTranslation(-gamePosition.x, -gamePosition.y);
        if(this.gameWorld != null) {
            gameWorld.setAffine();
        }
    }

    public Affine getAffine() {
        return affine;
    }

    public Vec2d getGamePosition() {
        return gamePosition;
    }

    public Scale getScale() {
        return scale;
    }

    // Transform screen position to game position
    public Vec2d transformGamePosition(Vec2d screenPosition) {
        return new Vec2d((screenPosition.x - position.x) / scale.getX() + gamePosition.x,
                (screenPosition.y - position.y) / scale.getY() + gamePosition.y);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setTransform(affine);
        gameWorld.onDraw(g);
        g.setTransform(new Affine());
        g.strokeRect(this.position.x, this.position.y, this.size.x, this.size.y);
        super.onDraw(g);
        if(!active) {
            g.setFill(Color.rgb(0, 0, 0, 0.5));
            g.fillRect(0, 0, size.x, size.y);
        }
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(!active) return;
        gameWorld.onTick(nanosSincePreviousTick);
        if(gameWorld.getCenterGameObject() != null) {
            Vec2d newPosition = gameWorld.getCenterGameObject().getTransformComponent().getPosition();
            gamePosition = new Vec2d(Math.max(0, Math.min(newPosition.x - (size.x / 2 / scale.getX()), gameWorld.getSize().x - (size.x - position.x) / scale.getX())),
                                     Math.max(0, Math.min(newPosition.y - (size.y / 2 / scale.getY()), gameWorld.getSize().y - (size.y - position.y) / scale.getY())));
            setAffine();
        }
        // Move
        if(selfMovable) {
            double dx[] = {0, 0, -1, 1}, dy[] = {-1, 1, 0, 0};
            double distance = moveRate * nanosSincePreviousTick / 1000000000;
            for (int k = 0; k < 4; k++) {
                if (move.get(k)) {
                    double x = Math.max(0, Math.min(gamePosition.x + dx[k] * distance, gameWorld.getSize().x - (size.x - position.x) / scale.getX()));
                    double y = Math.max(0, Math.min(gamePosition.y + dy[k] * distance, gameWorld.getSize().y - (size.y - position.y) / scale.getY()));
                    gamePosition = new Vec2d(x, y);
                }
            }
            setAffine();
        }
        super.onTick(nanosSincePreviousTick);
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if(!active) return;
        gameWorld.onKeyPressed(e);
        KeyCode keyCode = e.getCode();
        if(keyCode == KeyCode.UP) {
            move.set(0, true);
        }
        if(keyCode == KeyCode.DOWN) {
            move.set(1, true);
        }
        if(keyCode == KeyCode.LEFT) {
            move.set(2, true);
        }
        if(keyCode == KeyCode.RIGHT) {
            move.set(3, true);
        }
        super.onKeyPressed(e);
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        if(!active) return;
        gameWorld.onKeyReleased(e);
        KeyCode keyCode = e.getCode();
        if(keyCode == KeyCode.UP) {
            move.set(0, false);
        }
        if(keyCode == KeyCode.DOWN) {
            move.set(1, false);
        }
        if(keyCode == KeyCode.LEFT) {
            move.set(2, false);
        }
        if(keyCode == KeyCode.RIGHT) {
            move.set(3, false);
        }
        super.onKeyReleased(e);
    }

    @Override
    public void onMouseDragged(MouseEvent e) {
        if(!active) return;
        gameWorld.onMouseDragged(e);
        super.onMouseDragged(e);
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        if(!active) return;
        gameWorld.onMouseClicked(e);
        super.onMouseClicked(e);
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if(!active) return;
        gameWorld.onMouseReleased(e);
        super.onMouseReleased(e);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(!active) return;
        gameWorld.onMousePressed(e);
        super.onMousePressed(e);
    }

    @Override
    public void onMouseWheelMoved(ScrollEvent e) {
        if(!active) return;
        Vec2d screenMousePosition = new Vec2d(e.getX(), e.getY());
        Vec2d gameMousePosition = transformGamePosition(screenMousePosition);
        if(e.getDeltaY() < 0) {
            scale.setX(scale.getX() / zoomFactor);
            scale.setY(scale.getY() / zoomFactor);
        } else {
            scale.setX(scale.getX() * zoomFactor);
            scale.setY(scale.getY() * zoomFactor);
        }
        scale.setX(Math.max(maxScale, scale.getX()));
        scale.setY(Math.max(maxScale, scale.getY()));
        double x = Math.max(0, Math.min(gameMousePosition.x - (screenMousePosition.x - position.x) / scale.getX(), gameWorld.getSize().x - (size.x - position.x) / scale.getX()));
        double y = Math.max(0, Math.min(gameMousePosition.y - (screenMousePosition.y - position.y) / scale.getY(), gameWorld.getSize().y - (size.y - position.y) / scale.getY()));
        gamePosition = new Vec2d(x, y);
        setAffine();
        super.onMouseWheelMoved(e);
    }

    @Override
    public void onResize(Vec2d newSize) {
        double ratio = newSize.x / size.x;
        scale.setX(scale.getX() * ratio);
        scale.setY(scale.getY() * ratio);
        maxScale *= ratio;
        setAffine();
        super.onResize(newSize);
    }
}
