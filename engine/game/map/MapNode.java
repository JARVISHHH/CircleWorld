package engine.game.map;

import engine.support.Vec2d;
import engine.support.Vec2i;

import java.util.LinkedList;

public class MapNode {

    public MapNode(Vec2d topLeft, Vec2i topLeftGrid, Vec2i gridNum) {
        this.topLeft = topLeft;
        this.topLeftGrid = topLeftGrid;
        this.gridNum = gridNum;
    }

    public Vec2d topLeft;  // Topleft position in game world
    public Vec2i topLeftGrid;  // Topleft grid in game world
    public Vec2i gridNum;  // The number of grids in this node
    public Vec2i startGrid;  // The start index of real map (relative index of the node)
    public Vec2i gridSize;  // The number of grids of real map
    public MapNode leftChild;
    public MapNode rightChild;

    public boolean start = false;
    public boolean exit = false;

}
