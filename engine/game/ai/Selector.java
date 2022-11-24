package engine.game.ai;

public class Selector extends Composite{

    @Override
    public State update(long nanosSincePreviousTick) {
        State finalState = State.Fail;
        for(TreeNode treeNode: children) {
            if(finalState != State.Fail) {
                treeNode.reset();
                continue;
            }
            State nodeState = treeNode.update(nanosSincePreviousTick);
            if(nodeState != State.Fail) {
                lastRunning = treeNode;
                finalState = nodeState;
            }
        }
        return finalState;
    }
}
