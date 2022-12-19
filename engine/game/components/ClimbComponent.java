package engine.game.components;

import engine.support.Vec2d;
import javafx.scene.input.KeyCode;

public class ClimbComponent extends Component{
    KeyCode climbKey = KeyCode.Z;
    protected CollisionComponent detect = null;
    protected CollisionComponent groundDetect = null;
    protected boolean climbing = false;
    protected double noClimbingTime = 0;
    protected double maxClimbingVel = 100;
    protected float maxHoldingTime = 12;
    protected float lastHoldingTime = maxHoldingTime;
    protected double dx[] = {0, 0}, dy[] = {-1, 1};
    protected KeyCode direction[] = {KeyCode.UP, KeyCode.DOWN};

    public ClimbComponent() {
        tag = "Climb";
        setTickable(true);
    }

    public void setDetect(CollisionComponent detect) {
        this.detect = detect;
    }

    public void setGroundDetect(CollisionComponent groundDetect) {
        this.groundDetect = groundDetect;
    }

    public boolean isClimbing() {
        return climbing;
    }

    public void setMaxHoldingTime(float maxHoldingTime) {
        this.maxHoldingTime = maxHoldingTime;
        this.lastHoldingTime = maxHoldingTime;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        if(groundDetect != null && groundDetect.movePosition.y < 0) {
            lastHoldingTime = maxHoldingTime;
        }

        if(detect == null || detect.movePosition.x == 0) {
            noClimbingTime = 0;
            lastHoldingTime += nanosSincePreviousTick / 1000000000.0;
            climbing = false;
            return;
        }

        if(gameObject.keyPressing.containsKey(climbKey) && gameObject.keyPressing.get(climbKey) && lastHoldingTime > 0) {
            lastHoldingTime -= nanosSincePreviousTick / 1000000000.0;

            if(noClimbingTime > 0) {
                noClimbingTime -= nanosSincePreviousTick / 1000000000.0;
                if(noClimbingTime < 0) noClimbingTime = 0;
                climbing = false;
                return;
            }

            PhysicsComponent physicsComponent = (PhysicsComponent) gameObject.getComponent("Physics");
            GravityComponent gravityComponent = (GravityComponent) gameObject.getComponent("Gravity");
            JumpComponent jumpComponent = (JumpComponent) gameObject.getComponent("Jump");

            if(physicsComponent == null || gravityComponent == null) return;

            if(climbing) {
                if(jumpComponent != null && jumpComponent.checkJump()) {
                    noClimbingTime += 0.2;
                    lastHoldingTime -= 3;
                    climbing = false;
                    return;
                }
            }

            if(!climbing) {
                physicsComponent.setFixVel(new Vec2d(0, 0));
                if(jumpComponent != null) jumpComponent.setLeftJumpTime(jumpComponent.maxJumpTime);
                climbing = true;
            }

            Vec2d moveDirection = new Vec2d(0, 0);
            for(int k = 0; k < direction.length; k++) {
                if(gameObject.keyPressing.containsKey(direction[k]) && gameObject.keyPressing.get(direction[k])) {
                    moveDirection = moveDirection.plus((float)dx[k], (float)dy[k]);
                }
            }
            if(!moveDirection.isZero()) {
                Vec2d oldPosition = gameObject.getTransformComponent().getPosition();
                setTransformComponentPosition(new Vec2d(oldPosition.x, oldPosition.y + moveDirection.y * nanosSincePreviousTick / 1000000000.0 * maxClimbingVel));
                lastHoldingTime -= nanosSincePreviousTick / 1000000000.0;
            }

            if(physicsComponent.vel.y >= 0) gravityComponent.addNoGravityTime(nanosSincePreviousTick / 1000000000.0);
        } else climbing = false;
    }
}
