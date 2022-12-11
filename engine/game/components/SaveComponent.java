package engine.game.components;

public class SaveComponent extends Component{
    protected CollisionComponent collisionDetect = null;
    protected boolean inCollision = false;

    public SaveComponent() {
        tag = "Save";
        setTickable(true);
    }

    public void setCollisionDetect(CollisionComponent collisionDetect) {
        this.collisionDetect = collisionDetect;
    }

    public void doSave() {

    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        if(collisionDetect == null) return;
        if(collisionDetect.movePosition.isZero()) inCollision = false;
        else if(inCollision) return;
        else {
            inCollision = true;
            doSave();
        }
    }
}
