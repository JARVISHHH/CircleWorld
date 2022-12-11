package engine.game.components;

public class GuideComponent extends Component{
    protected CollisionComponent collisionDetect = null;

    public GuideComponent() {
        tag = "Guide";
        setTickable(true);
    }

    public void setCollisionDetect(CollisionComponent collisionDetect) {
        this.collisionDetect = collisionDetect;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        super.onTick(nanosSincePreviousTick);
        RectangleTextComponent rectangleTextComponent = (RectangleTextComponent)this.gameObject.getComponent("RectangleText");
        if(rectangleTextComponent == null) return;
        if(collisionDetect == null) return;
        if(collisionDetect.movePosition.isZero()) rectangleTextComponent.show = false;
        else rectangleTextComponent.show = true;
    }
}
