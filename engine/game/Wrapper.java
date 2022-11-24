package engine.game;

import engine.support.Vec2d;
import engine.support.Vec2i;


// Track position of subimages in the spriteSheet using index
public class Wrapper {
    protected Vec2d sheetSize;
    protected Vec2d spriteSize;
    protected Vec2i spriteNumber;

    public Vec2d getSpriteSize() {
        return spriteSize;
    }

    public Wrapper(Vec2d sheetSize, Vec2i spriteNumber) {
        this.sheetSize = sheetSize;
        this.spriteNumber = spriteNumber;
        this.spriteSize = new Vec2d(sheetSize.x / spriteNumber.x, sheetSize.y / spriteNumber.y);
    }

    public Vec2d getSprite(Vec2i index) {
        return new Vec2d(spriteSize.x * index.x, spriteSize.y * index.y);
    }
}
