package engine.game.components;

import Nin2.XMLProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HealthComponent extends Component{

    protected int health;

    public HealthComponent() {
        tag = "Health";
    }

    public HealthComponent(int health) {
        tag = "Health";
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void minus(int minus) {
        this.health -= minus;
    }

    public void plus(int plus) {
        this.health += plus;
    }

    @Override
    public Element writeXML(Document doc) {
        Element healthComponent = doc.createElement("HealthComponent");
        healthComponent.setAttribute("health", String.valueOf(health));

        return healthComponent;
    }

    @Override
    public void readXML(Element e) {
        health = Integer.parseInt(e.getAttribute("health"));
    }
}
