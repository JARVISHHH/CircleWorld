package engine.game.components;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FrictionComponent extends Component{

    protected double cofk;
    protected double cofs;  // Unused

    public FrictionComponent() {
        tag = "Friction";
    }

    public FrictionComponent(double cofk, double cofs) {
        tag = "Friction";
        this.cofk = cofk;
        this.cofs = cofs;
    }

    @Override
    public Element writeXML(Document doc) {
        Element frictionComponent = doc.createElement("FrictionComponent");
        frictionComponent.setAttribute("cofk", String.valueOf(cofk));
        frictionComponent.setAttribute("cofs", String.valueOf(cofs));

        return frictionComponent;
    }

    @Override
    public void readXML(Element e) {
        cofk = Double.parseDouble(e.getAttribute("cofk"));
        cofs = Double.parseDouble(e.getAttribute("cofs"));
    }
}
