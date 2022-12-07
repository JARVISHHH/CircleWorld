package Final.levels;

import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.AABShape;
import engine.game.collision.PolygonShape;
import engine.game.components.*;
import engine.support.Vec2d;
import engine.support.Vec2i;

public class Level {

    protected static GameWorld gameWorld;

    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        System.out.println("level");
        gameWorld = null;
        return null;
    }

    /**
     * Create a new plain tile game object
     * @param position position in the game world
     * @param spriteSize size of the sprite
     * @param factor scale up factor
     * @return a plain tile game object
     */
    public GameObject createPlainTile(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject tileObject = new GameObject();

        tileObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("tile1", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        tileObject.addComponent(spriteComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, 0), spriteSize.smult(factor)), true);
        tileObject.addComponent(collisionComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(1000, 0);
        tileObject.addComponent(physicsComponent);

        return tileObject;
    }

    /**
     * Create a new bumped tile game object
     * @param position position in the game world
     * @param spriteSize size of the sprite
     * @param factor  scale up factor
     * @return  a bumped tile game object
     */
    public GameObject createBumpedTile(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject tileObject = new GameObject();

        tileObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("tile2", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        tileObject.addComponent(spriteComponent);

        Vec2d[] points = {new Vec2d(spriteSize.x, spriteSize.y).smult(factor), new Vec2d(spriteSize.x, 25.0 / 36 * spriteSize.y).smult(factor), new Vec2d(33.0 / 64 * spriteSize.x, 5.0 / 36 * spriteSize.y).smult(factor),
                new Vec2d(28.0 / 64 * spriteSize.x, 5.0 / 36 * spriteSize.y).smult(factor), new Vec2d(0, 25.0 / 36 * spriteSize.y).smult(factor), new Vec2d(0, spriteSize.y).smult(factor)};
        CollisionComponent collisionComponent = new CollisionComponent(new PolygonShape(points), true);
        tileObject.addComponent(collisionComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(1000, 0);
        tileObject.addComponent(physicsComponent);

        return tileObject;
    }

    /**
     * Create a new rock game object
     * @param position position in the game world
     * @param spriteSize size of one sprite(grid)
     * @param factor  scale up factor
     * @param index  index in the sprite sheet
     * @param restitution restitution of the rock
     * @return   new created game object
     */
    public GameObject createRock(Vec2d position, Vec2d spriteSize, double factor, Vec2i index, double restitution) {
        GameObject rockObject = new GameObject();

        rockObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("rocks", new Vec2d(0, 0), spriteSize.smult(factor), index);
        rockObject.addComponent(spriteComponent);

        Vec2d[] originalPoints = {
                new Vec2d(47, 78), new Vec2d(54, 73), new Vec2d(53, 50), new Vec2d(35, 5),
                new Vec2d(27, 3), new Vec2d(21, 5), new Vec2d(0, 65), new Vec2d(8, 78)
        };

        Vec2d[] points = new Vec2d[8];
        for(int i = 0; i < 8; i++) {
            points[i] = new Vec2d(originalPoints[i].x / 55.0 * spriteSize.x * factor, originalPoints[i].y / (160.0 / 2) * spriteSize.y * factor);
        }
        CollisionComponent collisionComponent = new CollisionComponent(new PolygonShape(points), false, false, true);
        rockObject.addComponent(collisionComponent);

        CollisionComponent groundDetect = new CollisionComponent(new AABShape(new Vec2d(spriteSize.x * factor / 4, spriteSize.y * factor), new Vec2d(spriteSize.x * factor / 2, 1)), false, false, false, false, false, true);
        rockObject.addComponent(groundDetect);

        GravityComponent gravityComponent = new GravityComponent();
        gravityComponent.setGroundDetect(groundDetect);
        rockObject.addComponent(gravityComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(100, restitution);
        rockObject.addComponent(physicsComponent);

        return rockObject;
    }

    /**
     * Create an upward spike object.
     * @param position the position of the spike
     * @param spriteSize the size of the spike
     * @param factor how much should the spike be scaled up/down
     * @return the spike game object
     */
    public GameObject createUpwardSpike(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject spikeObject = new GameObject();

        spikeObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("upwardSpike", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        spikeObject.addComponent(spriteComponent);

        Vec2d[] originalPoints = {
                new Vec2d(32, 32), new Vec2d(16, 0), new Vec2d(0, 32)
        };

        Vec2d[] points = new Vec2d[3];
        for(int i = 0; i < 3; i++) {
            points[i] = new Vec2d(originalPoints[i].x / 32.0 * spriteSize.x * factor, originalPoints[i].y / 32.0 * spriteSize.y * factor);
        }
        CollisionComponent collisionComponent = new CollisionComponent(new PolygonShape(points), true, false, false);
        spikeObject.addComponent(collisionComponent);

        AttackComponent attackComponent = new AttackComponent(60);
        attackComponent.setDetect(collisionComponent);
        spikeObject.addComponent(attackComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(50, 0);
        spikeObject.addComponent(physicsComponent);

        return spikeObject;
    }

    /**
     * Create a downward spike object.
     * @param position the position of the spike
     * @param spriteSize the size of the spike
     * @param factor how much should the spike be scaled up/down
     * @return the spike game object
     */
    public GameObject createDownWardSpike(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject spikeObject = new GameObject();

        spikeObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("downwardSpike", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        spikeObject.addComponent(spriteComponent);

        Vec2d[] originalPoints = {
                new Vec2d(32, 0), new Vec2d(0, 0), new Vec2d(16, 32)
        };

        Vec2d[] points = new Vec2d[3];
        for(int i = 0; i < 3; i++) {
            points[i] = new Vec2d(originalPoints[i].x / 32.0 * spriteSize.x * factor, originalPoints[i].y / 32.0 * spriteSize.y * factor);
        }
        CollisionComponent collisionComponent = new CollisionComponent(new PolygonShape(points), true, false, false);
        spikeObject.addComponent(collisionComponent);

        AttackComponent attackComponent = new AttackComponent(60);
        attackComponent.setDetect(collisionComponent);
        spikeObject.addComponent(attackComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(50, 0);
        spikeObject.addComponent(physicsComponent);

        return spikeObject;
    }

    /**
     * Create border for the game world
     * @param spriteSize size of each wall
     * @param mapGridNum how many grids are in the game world
     * @return the border game object
     */
    public GameObject createBorder(Vec2d spriteSize, Vec2i mapGridNum) {
        GameObject borderObject = new GameObject();

        borderObject.setTransformComponent(new TransformComponent(new Vec2d(0, 0), gameWorld.getSize()));

        for(int i = 0; i < mapGridNum.x; i++) {
            SpriteComponent spriteComponent = new SpriteComponent("wall2", new Vec2d(i * spriteSize.x, 0), spriteSize, new Vec2i(0, 0));
            CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(i * spriteSize.x, 0), spriteSize), true);

            borderObject.addComponent(spriteComponent);
            borderObject.addComponent(collisionComponent);
        }
        for(int i = 1; i < mapGridNum.y; i++) {
            SpriteComponent spriteComponent = new SpriteComponent("wall2", new Vec2d(0, i * spriteSize.y), spriteSize, new Vec2i(0, 0));
            CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, i * spriteSize.y), spriteSize), true);

            borderObject.addComponent(spriteComponent);
            borderObject.addComponent(collisionComponent);
        }
        for(int i = 1; i < mapGridNum.y; i++) {
            SpriteComponent spriteComponent = new SpriteComponent("wall2", new Vec2d((mapGridNum.x - 1) * spriteSize.x, i * spriteSize.y), spriteSize, new Vec2i(0, 0));
            CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d((mapGridNum.x - 1) * spriteSize.x, i * spriteSize.y), spriteSize), true);

            borderObject.addComponent(spriteComponent);
            borderObject.addComponent(collisionComponent);
        }
        for(int i = 1; i < mapGridNum.x - 1; i++) {
            SpriteComponent spriteComponent = new SpriteComponent("wall2", new Vec2d(i * spriteSize.x, (mapGridNum.y - 1) * spriteSize.y), spriteSize, new Vec2i(0, 0));
            CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(i * spriteSize.x, (mapGridNum.y - 1) * spriteSize.y), spriteSize), true);

            borderObject.addComponent(spriteComponent);
            borderObject.addComponent(collisionComponent);
        }

        PhysicsComponent physicsComponent = new PhysicsComponent(50, 0);
        borderObject.addComponent(physicsComponent);

        return borderObject;
    }

    /**
     * Create a destructible wall object
     * @param position the position of the wall
     * @param spriteSize the size of the wall
     * @param factor how much should the wall be scaled up/down
     * @return
     */
    public GameObject createDestructibleWall(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject wallObject = new GameObject();

        wallObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("wall1", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        wallObject.addComponent(spriteComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, 0), spriteSize.smult(factor)), true, false, true);
        wallObject.addComponent(collisionComponent);

        HealthComponent healthComponent = new HealthComponent(30);
        wallObject.addComponent(healthComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(50, 0);
        wallObject.addComponent(physicsComponent);

        return wallObject;
    }

    /**
     * Create a indestructible wall object
     * @param position the position of the wall
     * @param spriteSize the size of the wall
     * @param factor how much should the wall be scaled up/down
     * @return
     */
    public GameObject createIndestructibleWall(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject wallObject = new GameObject();

        wallObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("wall3", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        wallObject.addComponent(spriteComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, 0), spriteSize.smult(factor)), true, false, false);
        wallObject.addComponent(collisionComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent(50, 0);
        wallObject.addComponent(physicsComponent);

        return wallObject;
    }

    /**
     * Create a final goal object for the game
     * @param position the position of the flag
     * @param spriteSize the size of the flag
     * @param factor how much should the flag be scaled up/down
     * @return
     */
    public GameObject createFlag(Vec2d position, Vec2d spriteSize, double factor) {
        GameObject flagObject = new GameObject();

        flagObject.setTransformComponent(new TransformComponent(position, spriteSize.smult(factor)));

        SpriteComponent spriteComponent = new SpriteComponent("flag", new Vec2d(0, 0), spriteSize.smult(factor), new Vec2i(0, 0));
        flagObject.addComponent(spriteComponent);

        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(0, 0), spriteSize.smult(factor)), true, false, false, false, true);
        flagObject.addComponent(collisionComponent);

        return flagObject;
    }
}
