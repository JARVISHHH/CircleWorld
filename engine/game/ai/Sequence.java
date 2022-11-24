package engine.game.ai;

import engine.game.components.Component;

public class Sequence extends Composite{

    @Override
    public void reset() {
        lastRunning = children.get(0);
        for(TreeNode treeNode: children)
            treeNode.reset();
    }

    @Override
    public State update(long nanosSincePreviousTick) {
        int index = 0;
        if(lastRunning != null && children.contains(lastRunning))
            index = children.indexOf(lastRunning);
        for(; index < children.size(); index++) {
            TreeNode treeNode = children.get(index);
            State nodeState = treeNode.update(nanosSincePreviousTick);
            if(nodeState != State.Success) {
                lastRunning = treeNode;
                return nodeState;
            }
        }
        reset();
        return State.Success;
    }
}
