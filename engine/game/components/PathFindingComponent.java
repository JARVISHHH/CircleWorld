package engine.game.components;

import engine.game.GameObject;
import engine.game.map.Map;
import engine.game.map.tileType;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class PathFindingComponent extends Component{

    public PathFindingComponent(Vec2d destPos, Map map) {
        tag = "PathFinding";
        this.map = map;
        this.destPos = destPos;
        this.destGrid = new Vec2i((int)(destPos.x / map.sizePerGrid.x), (int)(destPos.y / map.sizePerGrid.y));
        setTickable(true);
    }

    public PathFindingComponent(GameObject destObject, Map map) {
        tag = "PathFinding";
        this.destObject = destObject;
        this.map = map;
        destObject.getTransformComponent();
        this.destPos = destObject.getTransformComponent().getPosition();
        this.destGrid = new Vec2i((int)(destPos.x / map.sizePerGrid.x), (int)(destPos.y / map.sizePerGrid.y));
        setTickable(true);
    }

    protected GameObject destObject = null;

    protected Vec2d destPos;

    protected Vec2i destGrid;

    protected Map map;

    HashMap<Integer, Double> distance = new HashMap<>();
    HashMap<Integer, Integer> fromPos = new HashMap<>();
    HashMap<Integer, Integer> toPos = new HashMap<>();

    public void reset() {
        distance.clear();
        fromPos.clear();
        toPos.clear();
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(destObject != null) {
            this.destPos = destObject.getTransformComponent().position;
            this.destGrid = new Vec2i((int)(destPos.x / map.sizePerGrid.x), (int)(destPos.y / map.sizePerGrid.y));
        }
    }

    public GameObject getDestObject() {
        return destObject;
    }

    public Map getMap() {
        return map;
    }

    public Vec2d getDestPos() {
        return destPos;
    }

    public double computeCost(Vec2i pos, Vec2i destination) {
        Vec2d realPos = new Vec2d(map.sizePerGrid.x * pos.x, map.sizePerGrid.y * pos.y);
        Vec2d realDest = new Vec2d(map.sizePerGrid.x * destination.x, map.sizePerGrid.y * destination.y);
        return realPos.dist(realDest);
    }

    private int calculateIndex(Vec2i grid) {
        return grid.x + grid.y * map.grids.length;
    }

    public double aStar() {
        distance.clear();
        PriorityQueue<Pair<Double, Vec2i>> q = new PriorityQueue<>(new CustomComparator());

        int dx[] = {1, 0, -1, 0}, dy[] = {0, 1, 0, -1};

        Vec2d pos = gameObject.getTransformComponent().position;
        Vec2d size = gameObject.getTransformComponent().getSize();
        Vec2i source = new Vec2i((int)((pos.x + size.x / 2) / map.sizePerGrid.x), (int)((pos.y + size.y / 2) / map.sizePerGrid.y));

        distance.put(calculateIndex(source), 0.0);
        q.offer(new Pair<>(0.0, source));
        while(!q.isEmpty() && !distance.containsValue(calculateIndex(destGrid))) {
            Pair<Double, Vec2i> pair = q.poll();
            if(map.grids[pair.getValue().x][pair.getValue().y] == tileType.border ||
                    map.grids[pair.getValue().x][pair.getValue().y] == tileType.exterior ||
                    map.grids[pair.getValue().x][pair.getValue().y] == tileType.impassable) continue;
            for(int i = 0; i < 4; i++) {
                if(pair.getValue().x + dx[i] < 0 || pair.getValue().x + dx[i] >= map.grids[0].length) continue;
                if(pair.getValue().y + dy[i] < 0 || pair.getValue().y + dy[i] >= map.grids.length) continue;
                Vec2i newPos = new Vec2i(pair.getValue().x + dx[i], pair.getValue().y + dy[i]);
                if(distance.containsKey(calculateIndex(newPos))) continue;
                distance.put(calculateIndex(newPos), pair.getKey() + map.sizePerGrid.x + map.sizePerGrid.y);
                Pair<Double, Vec2i> newPair = new Pair<>(distance.get(calculateIndex(pair.getValue())) + computeCost(pair.getValue(), destGrid), newPos);
                q.offer(newPair);
                fromPos.put(calculateIndex(newPos), calculateIndex(pair.getValue()));
            }
        }

        getRoute(destGrid, source);

        return distance.get(calculateIndex(destGrid));
    }

    public void getRoute(Vec2i destination, Vec2i source) {
        Integer firstMove = calculateIndex(destination);
        while(firstMove != calculateIndex(source)) {
            toPos.put(fromPos.get(firstMove), firstMove);
            firstMove = fromPos.get(firstMove);
        }

    }

    public Vec2i getNextGrid() {
        Vec2d pos = gameObject.getTransformComponent().position;
        Vec2d size = gameObject.getTransformComponent().getSize();
        Vec2i source = new Vec2i((int)((pos.x + size.x / 2) / map.sizePerGrid.x), (int)((pos.y + size.y / 2) / map.sizePerGrid.y));
        if(!toPos.containsKey(calculateIndex(source))) return null;
        int res  = toPos.get(calculateIndex(source));
        return new Vec2i((res % map.grids.length), (res / map.grids.length));
    }
}

class CustomComparator implements Comparator<Pair<Double, Vec2i>> {

    @Override
    public int compare(Pair<Double, Vec2i> o1, Pair<Double, Vec2i> o2) {
        if(o1.getKey() > o2.getKey()) return 1;
        else if(o1.getKey() < o2.getKey()) return -1;
        return 0;
    }
}
