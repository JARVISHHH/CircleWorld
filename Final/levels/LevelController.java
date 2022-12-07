package Final.levels;

import java.util.HashMap;

public class LevelController {
    private static HashMap<Integer, Class<? extends Level>> h = new HashMap<>();

    public static void load() {
        LevelController.h.put(0, Level0.class);
        LevelController.h.put(1, Level1.class);
    }

    public static Class<? extends Level> getLevelClass(int levelNumber) {
        return h.getOrDefault(levelNumber, null);
    }
}