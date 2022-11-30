package Final;

import engine.game.GameObject;
import engine.game.GameWorld;
import engine.game.collision.Shape;
import engine.game.components.Component;
import engine.game.components.TransformComponent;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XMLProcessor {

    public static Map<String, Class<? extends Component>> tag2component = new HashMap<>();
    public static Map<String, Class<? extends Shape>> tag2shape = new HashMap<>();

    /**
     * Write the current game world to XML.
     * @param gameWorld the current game world
     */
    public void writeXML(GameWorld gameWorld){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException parserConfigurationException) {
            System.err.println(parserConfigurationException);
            System.exit(1);
        }

        Document doc = docBuilder.newDocument();
        doc.appendChild(gameWorld.writeXML(doc));

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding", "UTF-8");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            transformer.transform(new DOMSource(doc), new StreamResult((new File("Final/savings/saving.xml"))));
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Game saved!");

    }

    /**
     * Read the XML and create a new game world.
     * @return the created game world
     */
    public GameWorld readXML(){
        GameWorld gameWorld;

        // Set up the parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        Document doc = null;
        try {
            docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.parse("Final/savings/saving.xml");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        doc.getDocumentElement().normalize();

        Node node = doc.getDocumentElement();
        String worldSizeStr = ((Element)node).getAttribute("size");
        Vec2d worldSize = Vec2d.toVec2d(worldSizeStr);

        gameWorld = new GameWorld(worldSize);

        NodeList nodeList = node.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) continue;
            GameObject gameObject = new GameObject();
            NodeList componentNodeList = n.getChildNodes();
            for(int j = 0; j < componentNodeList.getLength(); j++) {
                if(componentNodeList.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
                Element componentElement = (Element)componentNodeList.item(j);
                try {
                    if(!tag2component.containsKey(componentElement.getTagName())) {
                        System.out.println(componentElement.getTagName() + " does not exist");
                        continue;
                    }
                    Component component = tag2component.get(componentElement.getTagName()).newInstance();
                    component.setGameObject(gameObject);
                    component.readXML(componentElement);
                    if(componentElement.getTagName().equals("TransformComponent"))
                        gameObject.setTransformComponent((TransformComponent)component);
                    else gameObject.addComponent(component);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
            gameWorld.addGameObject(gameObject);
            if(((Element)n).getTagName().equals("CenterGameObject")) {
                gameWorld.setCenterGameObject(gameObject);
            }
        }


        return gameWorld;
    }
}
