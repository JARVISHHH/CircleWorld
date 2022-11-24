package engine.game.components;

import Nin2.XMLProcessor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeyEventsComponent extends Component{

    public KeyEventsComponent() {
        tag = "KeyEvents";
        setResponseKeyEvents(true);
    }

    @Override
    public Component copy() {
        KeyEventsComponent component = new KeyEventsComponent();
        component.setGameObject(this.gameObject);
        component.setDrawable(this.drawable);
        component.setTransformComponent(this.gameObject.getTransformComponent().getPosition(), this.gameObject.getTransformComponent().getSize());
        component.setTickable(this.tickable);
        component.setResponseKeyEvents(this.responseKeyEvents);
        component.setResponseMouseEvents(this.responseMouseEvents);
        return component;
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        gameObject.keyPressing.put(keyCode, true);
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        gameObject.keyPressing.put(keyCode, false);
    }

    @Override
    public Element writeXML(Document doc) {
        Element keyEventsComponent = doc.createElement("KeyEventsComponent");

        return keyEventsComponent;
    }

    @Override
    public void readXML(Element e) {
        super.readXML(e);
    }
}
