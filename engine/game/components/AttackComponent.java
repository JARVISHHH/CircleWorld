package engine.game.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AttackComponent extends Component{
    protected int damage;

    protected CollisionComponent detect = null;

    public AttackComponent() {
        tag = "Attack";
    }

    public AttackComponent(int damage) {
        tag = "Attack";
        this.damage = damage;
    }

    public void setDetect(CollisionComponent detect) {
        this.detect = detect;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public Element writeXML(Document doc) {
        Element attackComponent = doc.createElement("AttackComponent");

        attackComponent.setAttribute("damage", String.valueOf(damage));

        return attackComponent;
    }

    @Override
    public void readXML(Element e) {
        damage = Integer.parseInt(e.getAttribute("damage"));
    }
}
