package engine.game.ai;

import engine.game.components.Component;

import java.util.ArrayList;

public class Composite implements TreeNode{

    protected Component component = null;
    public TreeNode lastRunning = null;

    public ArrayList<TreeNode> children = new ArrayList<>();

    @Override
    public void reset() {

    }

    @Override
    public State update(long nanosSincePreviousTick) {
        return State.Fail;
    }

    @Override
    public void setComponent(Component component) {
        this.component = component;
        for(TreeNode treeNode: children) {
            treeNode.setComponent(component);
        }
    }

    public void addChild(TreeNode treeNode) {
        children.add(treeNode);
    }
}
