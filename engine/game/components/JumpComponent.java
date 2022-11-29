package engine.game.components;

import engine.game.Sound;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.sound.sampled.*;

public class JumpComponent extends Component{

    protected boolean jumpable = true;  // Can jump or not
    protected int maxJumpTime = 1;
    protected int leftJumpTime = maxJumpTime;
    protected boolean finishedJump = true;  // If current jump has been executed
    protected boolean getHighest = false;

    protected double firstJumpImpulseFactor = 1.6;

    protected double maxImpulseFactor = 0.6;

    protected double oneTimeImpulseFactor = 1.2;

    protected double currentImpulseFactor = 0;

    KeyCode jumpKey = KeyCode.SPACE;

    protected String audioTag = "Jump";
    protected Clip audioClip = null;

    public JumpComponent() {
        tag = "Jump";
        setTickable(true);
    }

    public void setJumpable(boolean jumpable) {
        this.jumpable = jumpable;
    }

    public void setMaxJumpTime(int maxJumpTime) {
        this.maxJumpTime = maxJumpTime;
        this.leftJumpTime = maxJumpTime;
    }

    public void resetLeftJumpTime() {
        leftJumpTime = maxJumpTime;
    }

    public void setJumpKey(KeyCode jumpKey) {
        this.jumpKey = jumpKey;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        double t = nanosSincePreviousTick / 1000000000.0;
        PhysicsComponent physicsComponent = (PhysicsComponent)getGameObject().getComponent("Physics");
        if(physicsComponent == null) return;
        double forceFactor = physicsComponent.mass * 9.8;
        // If the jump button is pressed, the game object is currently jumpable and the jump has not been executed,
        // then jump
        if((!getHighest || (finishedJump && leftJumpTime > 0)) && gameObject.keyPressing.containsKey(jumpKey) && gameObject.keyPressing.get(jumpKey)) {
            Vec2d force = new Vec2d(0, -1);
            double addImpulseFactor;
            if(finishedJump && leftJumpTime > 0) {
                physicsComponent.vel = new Vec2d(physicsComponent.vel.x, 0);
                physicsComponent.applyImpulse(force.smult(forceFactor * firstJumpImpulseFactor));
                playSound();
                finishedJump = false;
                leftJumpTime -= 1;
            } else {
                addImpulseFactor = Math.max(Math.min(maxImpulseFactor - currentImpulseFactor, t * oneTimeImpulseFactor), 0);
                physicsComponent.applyImpulse(force.smult(forceFactor * addImpulseFactor));
                currentImpulseFactor += addImpulseFactor;
            }
            if(currentImpulseFactor >= maxImpulseFactor) {
                currentImpulseFactor = 0;
                getHighest = true;
            }
        }
        if(!gameObject.keyPressing.containsKey(jumpKey) || !gameObject.keyPressing.get(jumpKey)) {
            currentImpulseFactor = 0;
            finishedJump = true;
            getHighest = false;
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
    public Element writeXML(Document doc) {
        Element jumpComponent = doc.createElement("JumpComponent");
        jumpComponent.setAttribute("jumpable", Boolean.toString(jumpable));
        jumpComponent.setAttribute("finishedJump", Boolean.toString(finishedJump));
        jumpComponent.setAttribute("firstJumpImpulseFactor", String.valueOf(firstJumpImpulseFactor));
        jumpComponent.setAttribute("maxImpulseFactor", String.valueOf(maxImpulseFactor));
        jumpComponent.setAttribute("oneTimeImpulseFactor", String.valueOf(oneTimeImpulseFactor));
        jumpComponent.setAttribute("currentImpulseFactor", String.valueOf(currentImpulseFactor));
        jumpComponent.setAttribute("jumpKey", String.valueOf(jumpKey));

        return jumpComponent;
    }

    @Override
    public void readXML(Element e) {
        jumpable = Boolean.parseBoolean(e.getAttribute("jumpable"));
        finishedJump = Boolean.parseBoolean(e.getAttribute("finishedJump"));
        firstJumpImpulseFactor = Double.parseDouble(e.getAttribute("firstJumpImpulseFactor"));
        maxImpulseFactor = Double.parseDouble(e.getAttribute("maxImpulseFactor"));
        oneTimeImpulseFactor = Double.parseDouble(e.getAttribute("oneTimeImpulseFactor"));
        currentImpulseFactor = Double.parseDouble(e.getAttribute("currentImpulseFactor"));
        jumpKey = KeyCode.valueOf(e.getAttribute("jumpKey"));
    }

    @Override
    public void onShutdown() {
        if(audioClip != null && audioClip.isRunning())
            audioClip.close();
    }
}
