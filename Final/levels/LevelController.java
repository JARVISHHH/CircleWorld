package Final.levels;

import java.util.HashMap;

public class LevelController {
    private static HashMap<Integer, Class<? extends Level>> h = new HashMap<>();

    public static void load() {
        LevelController.h.put(0, Level0.class);
        LevelController.h.put(1, Level1.class);
        LevelController.h.put(2, Level2.class);
        LevelController.h.put(3, Level3.class);
        LevelController.h.put(4, Level4.class);
        LevelController.h.put(5, Level5.class);
    }

    public static Class<? extends Level> getLevelClass(int levelNumber) {
        return h.getOrDefault(levelNumber, null);
    }
}
