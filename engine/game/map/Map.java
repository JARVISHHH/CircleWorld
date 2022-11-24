package engine.game.map;

import engine.support.Vec2d;
import engine.support.Vec2i;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Map {

    public Map(Vec2d size, int depth, long seed) {
        this.size = size;
        this.depth = depth;
        this.random = new Random(seed);
    }

    public Vec2d size;  // Real size of the map
    public Vec2i gridNum;  // Total number of grids

    public Vec2d sizePerGrid;
    public int depth;  // Depth of the tree
    public MapNode root;

    public tileType[][] grids;
    public Random random;

    public Vec2i startPosition;  // Start position of the game
    public Vec2i exitPosition;  // Exit position of the game

    public void spacePartitioning(Vec2i gridNum, Vec2i minimumSize) {
        this.gridNum = gridNum;
        this.sizePerGrid = new Vec2d(size.x / gridNum.x, size.y / gridNum.y);
        this.grids = new tileType[gridNum.x][gridNum.y];
        // Every grid is exterior at the beginning
        for(int i = 0; i < gridNum.x; i++)
            for(int j = 0; j < gridNum.y; j++)
                grids[i][j] = tileType.exterior;
        // Create a new tree node
        root = new MapNode(new Vec2d(0, 0), new Vec2i(0, 0), gridNum);
        // Use queue to breadth-firstly generate a tree
        int level = 0;
        Queue<MapNode> q = new LinkedList<MapNode>();
        q.offer(root);
        while(level < depth) {
            int queueSize = q.size();
            while(queueSize > 0) {
                MapNode currentNode = q.poll();
                MapNode leftChild, rightChild;
                queueSize--;
                // Split the longer axis
                if(currentNode.gridNum.x >= currentNode.gridNum.y) {
                    if(currentNode.gridNum.x <= 2 * minimumSize.x) continue;
                    int split = random.nextInt(currentNode.gridNum.x - 2 * minimumSize.x);
                    leftChild = new MapNode(currentNode.topLeft,
                                            currentNode.topLeftGrid,
                                            new Vec2i(split + minimumSize.x, currentNode.gridNum.y));
                    rightChild = new MapNode(new Vec2d(currentNode.topLeft.x + split + 1, currentNode.topLeft.y),
                                            new Vec2i(currentNode.topLeftGrid.x + split + minimumSize.x + 1, currentNode.topLeftGrid.y),
                                            new Vec2i(currentNode.gridNum.x - split - minimumSize.x - 1, currentNode.gridNum.y));
                } else {
                    if(currentNode.gridNum.y <= 2 * minimumSize.y) continue;
                    int split = random.nextInt(currentNode.gridNum.y - 2 * minimumSize.y);
                    leftChild = new MapNode(currentNode.topLeft,
                            currentNode.topLeftGrid,
                            new Vec2i(currentNode.gridNum.x, split + minimumSize.y));
                    rightChild = new MapNode(new Vec2d(currentNode.topLeft.x, currentNode.topLeft.y + split + 1),
                            new Vec2i(currentNode.topLeftGrid.x, currentNode.topLeftGrid.y + split + minimumSize.y + 1),
                            new Vec2i(currentNode.gridNum.x, currentNode.gridNum.y - split - minimumSize.y - 1));
                }
                currentNode.leftChild = leftChild;
                currentNode.rightChild = rightChild;
                q.offer(leftChild);
                q.offer(rightChild);
            }
            level++;
        }
        // The left-most leaf node will be the start room
        MapNode start = q.poll();
        start.start = true;
        while(q.size() > 1) {
            q.poll();
        }
        // The right-most node will be the exit room
        MapNode exit = q.poll();
        exit.exit = true;
        dfs(this.root);
    }

    private boolean isLeafNode(MapNode node) {
        return node.leftChild == null && node.rightChild == null;
    }

    /**
     * Connect 2 nodes
     * @param left the left node
     * @param right the right node
     * @param parent their parent node
     */
    private void connect(MapNode left, MapNode right, MapNode parent) {
        // If the 2 nodes is split to upper node and downer node
        if(left.topLeftGrid.x == right.topLeftGrid.x) {  // up and down
            int leftX = Math.max(left.startGrid.x, right.startGrid.x);
            int rightX = Math.min(left.startGrid.x + left.gridSize.x, right.startGrid.x + right.gridSize.x);
            // Random the position of the connector(bridge)
            int pos = left.topLeftGrid.x + leftX + random.nextInt(rightX - leftX - 2) + 1;
            // Set grids in between to be connector, and add walls
            for(int i = left.topLeftGrid.y + left.gridNum.y - 1; i >= left.topLeftGrid.y && grids[pos][i] != tileType.interior && grids[pos][i] != tileType.connector; i--) {
                grids[pos][i] = tileType.connector;
                if(pos - 1 >= 0 && grids[pos - 1][i] == tileType.exterior) grids[pos - 1][i] = tileType.border;
                if(pos + 1 < grids.length && grids[pos + 1][i] == tileType.exterior) grids[pos + 1][i] = tileType.border;
            }
            for(int i = right.topLeftGrid.y; i < right.topLeftGrid.y + right.gridNum.y && grids[pos][i] != tileType.interior && grids[pos][i] != tileType.connector; i++) {
                grids[pos][i] = tileType.connector;
                if(pos - 1 >= 0 && grids[pos - 1][i] == tileType.exterior) grids[pos - 1][i] = tileType.border;
                if(pos + 1 < grids.length && grids[pos + 1][i] == tileType.exterior) grids[pos + 1][i] = tileType.border;
            }
            grids[pos][right.topLeftGrid.y - 1] = tileType.connector;
            if(pos - 1 >= 0 && grids[pos - 1][right.topLeftGrid.y - 1] == tileType.exterior) grids[pos - 1][right.topLeftGrid.y - 1] = tileType.border;
            if(pos + 1 < grids.length && grids[pos + 1][right.topLeftGrid.y - 1] == tileType.exterior) grids[pos + 1][right.topLeftGrid.y - 1] = tileType.border;
            // Update variables of the parent node
            parent.startGrid = new Vec2i(Math.min(left.startGrid.x, right.startGrid.x),
                                                left.startGrid.y);
            Vec2i bottomRight = new Vec2i(Math.max(left.startGrid.x + left.gridSize.x, right.startGrid.x + right.gridSize.x),
                                        left.gridNum.y + right.startGrid.y + right.gridSize.y);
            parent.gridSize = bottomRight.minus(parent.startGrid);
        }
        // If the 2 nodes is split to lefter node and righter node
        else {  // left and right
            int upY = Math.max(left.startGrid.y, right.startGrid.y);
            int downY = Math.min(left.startGrid.y + left.gridSize.y, right.startGrid.y + right.gridSize.y);
            // Random the position of the connector(bridge)
            int pos = left.topLeftGrid.y + upY + random.nextInt(downY - upY - 2) + 1;
            // Set grids in between to be connector, and add walls
            for(int i = left.topLeftGrid.x + left.gridNum.x - 1; i >= left.topLeftGrid.x && grids[i][pos] != tileType.interior && grids[i][pos] != tileType.connector; i--) {
                grids[i][pos] = tileType.connector;
                if(pos - 1 >= 0 && grids[i][pos - 1] == tileType.exterior) grids[i][pos - 1] = tileType.border;
                if(pos + 1 < grids[0].length && grids[i][pos + 1] == tileType.exterior) grids[i][pos + 1] = tileType.border;
            }
            for(int i = right.topLeftGrid.x; i < right.topLeftGrid.x + right.gridNum.x && grids[i][pos] != tileType.interior && grids[i][pos] != tileType.connector; i++) {
                grids[i][pos] = tileType.connector;
                if(pos - 1 >= 0 && grids[i][pos - 1] == tileType.exterior) grids[i][pos - 1] = tileType.border;
                if(pos + 1 < grids[0].length && grids[i][pos + 1] == tileType.exterior) grids[i][pos + 1] = tileType.border;
            }
            grids[right.topLeftGrid.x - 1][pos] = tileType.connector;
            if(pos - 1 >= 0 && grids[right.topLeftGrid.x - 1][pos - 1] == tileType.exterior) grids[right.topLeftGrid.x - 1][pos - 1] = tileType.border;
            if(pos + 1 < grids[0].length && grids[right.topLeftGrid.x - 1][pos + 1] == tileType.exterior) grids[right.topLeftGrid.x - 1][pos + 1] = tileType.border;
            // Update variables of the parent node
            parent.startGrid = new Vec2i(left.startGrid.x,
                    Math.min(left.startGrid.y, right.startGrid.y));
            Vec2i bottomRight = new Vec2i( left.gridNum.x + right.startGrid.x + right.gridSize.x,
                    Math.max(left.startGrid.y + left.gridSize.y, right.startGrid.y + right.gridSize.y));
            parent.gridSize = bottomRight.minus(parent.startGrid);
        }
    }

    /**
     * Dfs to create rooms and connect rooms
     * @param root
     */
    private void dfs(MapNode root) {
        // If current node is a leaf node, create a room
        if(isLeafNode(root)) {
            int width = root.gridNum.x - random.nextInt(root.gridNum.x / 2);
            int height = root.gridNum.y - random.nextInt(root.gridNum.y / 2);
            root.startGrid = new Vec2i(random.nextInt(root.gridNum.x - width + 1), random.nextInt(root.gridNum.y - height + 1));
            root.gridSize = new Vec2i(width, height);
            for(int i = root.topLeftGrid.x + root.startGrid.x; i < root.topLeftGrid.x + root.startGrid.x + root.gridSize.x; i++) {
                grids[i][root.topLeftGrid.y + root.startGrid.y] = tileType.border;
                grids[i][root.topLeftGrid.y + root.startGrid.y + root.gridSize.y - 1] = tileType.border;
            }
            for(int i = root.topLeftGrid.y + root.startGrid.y; i < root.topLeftGrid.y + root.startGrid.y + root.gridSize.y; i++) {
                grids[root.topLeftGrid.x + root.startGrid.x][i] = tileType.border;
                grids[root.topLeftGrid.x + root.startGrid.x + root.gridSize.x - 1][i] = tileType.border;
            }
            for(int i = root.topLeftGrid.x + root.startGrid.x + 1; i < root.topLeftGrid.x + root.startGrid.x + root.gridSize.x - 1; i++)
                for(int j = root.topLeftGrid.y + root.startGrid.y + 1; j < root.topLeftGrid.y + root.startGrid.y + root.gridSize.y - 1; j++)
                    grids[i][j] = tileType.interior;
            if(root.start) startPosition = root.topLeftGrid.plus(root.startGrid.plus(1, 1));
            if(root.exit) exitPosition = root.topLeftGrid.plus(root.startGrid.plus(1, 1));
        }
        // If it's not a leaf node, connect it's children
        else {
            dfs(root.leftChild);
            dfs(root.rightChild);
            connect(root.leftChild, root.rightChild, root);
        }
    }

}
