package engine.game.components;

public class TrapComponent extends Component{
    protected CollisionComponent detect = null;
    protected boolean trapped = false;

    public TrapComponent() {
        tag = "Trap";
        setTickable(true);
    }

    public void setDetect(CollisionComponent detect) {
        this.detect = detect;
    }

    protected void doTrap() {
        gameObject.removeComponent(this);
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(detect == null) return;

        if(!trapped && !detect.movePosition.isZero()) {
            doTrap();
            trapped = true;
        }
    }
}
