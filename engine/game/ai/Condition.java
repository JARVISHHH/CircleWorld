package engine.game.ai;

import engine.game.components.Component;

public class Condition implements TreeNode {

    protected Component component = null;

    @Override
    public void reset() {

    }

    @Override
    public State update(long nanosSincePreviousTick) {
        if(checkCondition(nanosSincePreviousTick)) return State.Success;
        else return State.Fail;
    }

    @Override
    public void setComponent(Component component) {
        this.component = component;
    }

    public boolean checkCondition(long nanosSincePreviousTick) {
        return false;
    }
}
