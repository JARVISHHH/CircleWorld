package engine.uikit;

import engine.game.GameWorld;
import engine.game.map.Map;
import engine.game.map.tileType;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MiniMap extends UIElement{
    protected GameWorld gameWorld;

    protected boolean[][] mapGrids;

    protected Vec2d sizePerGrid;

    protected Map map;

    public MiniMap(Vec2d position, Vec2d size, GameWorld gameWorld, Map map) {
        this.position = position;
        this.size = size;
        this.gameWorld = gameWorld;
        this.map = map;
        this.mapGrids = new boolean[map.grids.length][map.grids[0].length];
        this.sizePerGrid = new Vec2d(size.x / map.grids.length, size.y / map.grids[0].length);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        if(!active) return;
        for(int i = 0; i < mapGrids.length; i++) {
            for(int j = 0; j < mapGrids.length; j++) {
                if(map.grids[i][j] == tileType.exterior) g.setFill(Color.rgb(0, 0, 0));
                else g.setFill(Color.rgb(0, 120, 0));
                g.fillRect(position.x + i * sizePerGrid.x, position.y + j * sizePerGrid.y, sizePerGrid.x, sizePerGrid.y);
            }
        }
        Vec2d characterPos = gameWorld.getCenterGameObject().getTransformComponent().getPosition();
        g.setFill(Color.rgb(120, 0, 0));
        g.fillRect(position.x + characterPos.x / gameWorld.getSize().x * map.grids.length * sizePerGrid.x, position.y + characterPos.y / gameWorld.getSize().x * map.grids[0].length * sizePerGrid.y, sizePerGrid.x, sizePerGrid.y);
        super.onDraw(g);
    }

    @Override
    public void onResize(Vec2d newSize) {
        double ratio = newSize.x / size.x;
        sizePerGrid = sizePerGrid.smult(ratio);
        super.onResize(newSize);
    }
}
