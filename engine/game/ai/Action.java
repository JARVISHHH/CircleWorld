package engine.game.ai;

import engine.game.components.Component;

public class Action implements TreeNode{

    protected Component component = null;

    public State state = State.Fail;

    public boolean resetState = false;

    @Override
    public void reset() {
        if(state == State.Running) {
            resetState = true;
        } else {
            realReset();
        }
    }

    protected void realReset() {
        state = State.Fail;
    }

    @Override
    public State update(long nanosSincePreviousTick) {
        if(state == State.Fail) {
            state = State.Running;
            doAction(nanosSincePreviousTick);
        }
        return state;
    }

    @Override
    public void setComponent(Component component) {
        this.component = component;
    }

    public void doAction(long nanosSincePreviousTick) {
        if(resetState) {
            realReset();
            resetState = false;
        } else {
            state = State.Success;
        }
    }
}
