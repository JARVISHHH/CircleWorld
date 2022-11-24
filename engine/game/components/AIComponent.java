package engine.game.components;

import engine.game.ai.TreeNode;

public class AIComponent extends Component{
    public TreeNode root = null;

    public AIComponent(TreeNode treeNode) {
        tag = "AI";
        setTickable(true);
        root = treeNode;
        root.setComponent(this);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(root != null) {
//            System.out.println("behavior tree tick!");
            root.update(nanosSincePreviousTick);
        }
    }
}
