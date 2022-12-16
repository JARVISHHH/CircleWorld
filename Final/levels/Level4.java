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

public class Level4 extends Level{
    public Level4() {
        levelNumber = "4";
    }

    /**
     * Create a game world of size worldSize.
     * @param worldSize worldSize is the size of the game world
     */
    @Override
    public GameWorld createGameWorld(Vec2d worldSize, Vec2i mapGridNum) {
        gameWorld = new GameWorld(worldSize);

        Vec2d spriteSize = worldSize.pdiv(mapGridNum.x, mapGridNum.y);  // Size of each grid

        GameObject save = createSave(new Vec2d(30, 355), spriteSize, 1);
        gameWorld.addGameObject(save);



        Character character = new Character(new Vec2d(50, 370), spriteSize);
        GameObject characterObject = character.getCharacter();
        CollisionComponent collisionComponent = new CollisionComponent(new AABShape(new Vec2d(-1, character.getCharacterSize().y / 3), new Vec2d(character.getCharacterSize().x + 2, character.getCharacterSize().y / 3)), false, false, false, false, false, true);
        characterObject.addComponent(collisionComponent);
        ClimbComponent climbComponent = new ClimbComponent();
        climbComponent.setDetect(collisionComponent);
        characterObject.addComponent(climbComponent);
        JumpComponent jumpComponent = (JumpComponent)characterObject.getComponent("Jump");
        jumpComponent.addDetect(collisionComponent);

        GameObject border = createBorder(spriteSize, mapGridNum);

        GameObject flag = createFlag(new Vec2d(900, 150 - spriteSize.y), spriteSize, 1);
        gameWorld.addGameObject(flag);
        gameWorld.addGameObject(border);
        gameWorld.addGameObject(characterObject);
        gameWorld.setCenterGameObject(characterObject);

        return gameWorld;
    }
}
