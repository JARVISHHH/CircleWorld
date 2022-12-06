package engine.game.components;

import engine.game.Sound;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;

import javax.sound.sampled.Clip;

public class DashComponent extends Component{
    protected int maxDashTime = 1;
    protected int leftDashTime = maxDashTime;
    protected boolean finishedDash = true;
    protected double dashImpulseFactor = 3.6;

    KeyCode dashKey = KeyCode.X;

    protected String audioTag = "Jump";
    protected Clip audioClip = null;

    protected CollisionComponent groundDetect = null;

    public DashComponent() {
        tag = "Dash";
        setTickable(true);
    }


    public void setMaxDashTime(int maxDashTime) {
        this.maxDashTime = maxDashTime;
        if(leftDashTime > maxDashTime) leftDashTime = maxDashTime;
    }

    public void setLeftDashTime(int leftDashTime) {
        this.leftDashTime = leftDashTime;
    }

    public void setGroundDetect(CollisionComponent groundDetect) {
        this.groundDetect = groundDetect;
    }

    public void setDashKey(KeyCode dashKey) {
        this.dashKey = dashKey;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(groundDetect == null || groundDetect.movePosition.y < 0) {
            setLeftDashTime(maxDashTime);
        }

        double t = nanosSincePreviousTick / 1000000000.0;

        PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
        if(physicsComponent == null) return;

        double forceFactor = physicsComponent.mass * 9.8;

        if(finishedDash && leftDashTime > 0 && gameObject.keyPressing.containsKey(dashKey) && gameObject.keyPressing.get(dashKey)) {
            Vec2d direction = new Vec2d(0, 0);

            MoveComponent moveComponent = (MoveComponent) getGameObject().getComponent("Move");
            if(moveComponent != null) {
                for(int i = 0; i < moveComponent.direction.length; i++) {
                    KeyCode keyCode = moveComponent.direction[i];
                    if(gameObject.keyPressing.containsKey(keyCode) && gameObject.keyPressing.get(keyCode))
                        direction = direction.plus(new Vec2d(moveComponent.dx[i], moveComponent.dy[i]));
                }
            }
            if(direction.isZero()) {
                if(moveComponent != null) direction = moveComponent.moveDirection;
                else direction = new Vec2d(1, 0);
            }
            else direction = direction.normalize();

            physicsComponent.vel = new Vec2d(0, 0);
            physicsComponent.applyImpulse(direction.smult(forceFactor * dashImpulseFactor));
            GravityComponent gravityComponent = (GravityComponent) getGameObject().getComponent("Gravity");
            if(gravityComponent != null) gravityComponent.setNoGravityTime(0.2);
            playSound();

            finishedDash = false;
            leftDashTime--;

        }

        if(!gameObject.keyPressing.containsKey(dashKey) || !gameObject.keyPressing.get(dashKey)) {
            finishedDash = true;
        }
    }

    private void playSound() {
        if(audioClip == null) audioClip = Sound.getReverbClip(audioTag, 20, 0.4F);;
        if(audioClip != null) {
            if(audioClip.isRunning()) audioClip = Sound.getReverbClip(audioTag, 20, 0.4F);
            else audioClip.setFramePosition(0);
            audioClip.start();
        }
    }

    private void stopSound() {
        if(audioClip == null) audioClip = Sound.getReverbClip(audioTag, 20, 0.4F);;
        if(audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0);
        }
    }

    @Override
    public void onShutdown() {
        if(audioClip != null && audioClip.isRunning())
            audioClip.close();
    }
}
