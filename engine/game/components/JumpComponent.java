package engine.game.components;

import engine.game.Sound;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sound.sampled.*;
import java.util.ArrayList;

public class JumpComponent extends Component{

    protected int maxJumpTime = 1;
    protected int leftJumpTime = maxJumpTime;
    protected boolean jumped;
    protected boolean finishedJump = true;  // If current jump has been executed
    protected boolean getHighest = false;

    protected double firstJumpImpulseFactor = 2.4;

    protected double maxImpulseFactor = 0.8;

    protected double impulseFactorPerSecond = 2.4;

    protected double currentImpulseFactor = 0;

    KeyCode jumpKey = KeyCode.SPACE;

    protected String audioTag = "Jump";
    protected Clip audioClip = null;
    protected ArrayList<CollisionComponent> detects = new ArrayList<>();

    public JumpComponent() {
        tag = "Jump";
        setTickable(true);
    }

    public void setMaxJumpTime(int maxJumpTime) {
        this.maxJumpTime = maxJumpTime;
        if(leftJumpTime > maxJumpTime) leftJumpTime = maxJumpTime;
    }

    public void setLeftJumpTime(int leftJumpTime) {
        this.leftJumpTime = leftJumpTime;
    }

    public void setJumpKey(KeyCode jumpKey) {
        this.jumpKey = jumpKey;
    }

    public void addDetect(CollisionComponent detect) {
        this.detects.add(detect);
    }
    public CollisionComponent getDetect(int index) {
        if(detects.size() < index) return detects.get(index);
        return null;
    }

    public boolean checkJump() {
        if(finishedJump && leftJumpTime > 0 && gameObject.keyPressing.containsKey(jumpKey) && gameObject.keyPressing.get(jumpKey) || jumped) {
            return true;
        }
        return false;
    }

    @Override
    public void onTick(long nanosSincePreviousTick) {
        jumped = false;

        boolean touchGround = false;
        for(CollisionComponent collisionComponent: detects) {
            if(collisionComponent != null && !collisionComponent.movePosition.isZero()) {
                setLeftJumpTime(maxJumpTime);
                touchGround = true;
            }
        }
        if(!touchGround && leftJumpTime == maxJumpTime) leftJumpTime = maxJumpTime - 1;

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
                jumped = true;
                finishedJump = false;
                leftJumpTime -= 1;
            } else {
                addImpulseFactor = Math.max(Math.min(maxImpulseFactor - currentImpulseFactor, t * impulseFactorPerSecond), 0);
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
        if(audioClip == null) audioClip = Sound.getReverbClip(audioTag, 20, (float)0.4);
        if(audioClip != null) {
            if(audioClip.isRunning()) audioClip = Sound.getReverbClip(audioTag, 20, (float)0.4);
            else audioClip.setFramePosition(0);
            audioClip.start();
        }
    }

    private void stopSound() {
        if(audioClip == null) audioClip = Sound.getReverbClip(audioTag, 20, (float)0.4);
        if(audioClip != null) {
            audioClip.stop();
            audioClip.setFramePosition(0);
        }
    }

    @Override
    public Element writeXML(Document doc) {
        Element jumpComponent = doc.createElement("JumpComponent");
        jumpComponent.setAttribute("maxJumpTime", String.valueOf(maxJumpTime));
        jumpComponent.setAttribute("leftJumpTime", String.valueOf(leftJumpTime));
        jumpComponent.setAttribute("finishedJump", Boolean.toString(finishedJump));
        jumpComponent.setAttribute("firstJumpImpulseFactor", String.valueOf(firstJumpImpulseFactor));
        jumpComponent.setAttribute("maxImpulseFactor", String.valueOf(maxImpulseFactor));
        jumpComponent.setAttribute("oneTimeImpulseFactor", String.valueOf(impulseFactorPerSecond));
        jumpComponent.setAttribute("currentImpulseFactor", String.valueOf(currentImpulseFactor));
        jumpComponent.setAttribute("jumpKey", String.valueOf(jumpKey));

//        jumpComponent.appendChild(groundDetect.writeXML(doc));

        return jumpComponent;
    }

    @Override
    public void readXML(Element e) {
        maxJumpTime = Integer.parseInt(e.getAttribute("maxJumpTime"));
        leftJumpTime = Integer.parseInt(e.getAttribute("leftJumpTime"));
        finishedJump = Boolean.parseBoolean(e.getAttribute("finishedJump"));
        firstJumpImpulseFactor = Double.parseDouble(e.getAttribute("firstJumpImpulseFactor"));
        maxImpulseFactor = Double.parseDouble(e.getAttribute("maxImpulseFactor"));
        impulseFactorPerSecond = Double.parseDouble(e.getAttribute("oneTimeImpulseFactor"));
        currentImpulseFactor = Double.parseDouble(e.getAttribute("currentImpulseFactor"));
        jumpKey = KeyCode.valueOf(e.getAttribute("jumpKey"));

//        NodeList nodeList = e.getChildNodes();
//        for(int i = 0; i < nodeList.getLength(); i++) {
//            if(e.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
//            Element element = (Element) e.getChildNodes().item(i);
//            if(element.getTagName().equals("CollisionComponent")) {
//                this.groundDetect = new CollisionComponent();
//                this.groundDetect.readXML(element);
//            }
//        }
//
//        if(groundDetect != null) gameObject.addComponent(groundDetect);
    }

    @Override
    public void onShutdown() {
        if(audioClip != null && audioClip.isRunning())
            audioClip.close();
    }
}
