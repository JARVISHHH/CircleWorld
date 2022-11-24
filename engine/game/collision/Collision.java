package engine.game.collision;

import engine.game.GameObject;
import engine.game.components.CollisionComponent;
import engine.game.components.Component;
import engine.support.Vec2d;

public class Collision {
    public Collision(CollisionComponent other, Vec2d mtv, Shape thisShape, Shape otherShape) {
        this.other = other;
        this.mtv = mtv;
        this.thisShape = thisShape;
        this.otherShape = otherShape;
    }

    public final CollisionComponent other;
    public final Vec2d mtv;
    public final Shape thisShape;
    public final Shape otherShape;
}
