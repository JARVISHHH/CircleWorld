package engine.game.ai;

import engine.game.components.Component;

public interface TreeNode {
    public void reset();
    public State update(long nanosSincePreviousTick);

    public void setComponent(Component component);
}
