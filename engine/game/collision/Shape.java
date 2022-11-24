package engine.game.collision;

import engine.game.components.CollisionComponent;
import engine.game.components.Component;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Shape {
    Vec2d getCollision(Shape shape);
    Vec2d getCollision(Vec2d point);
    Vec2d getCollision(CircleShape circleShape);
    Vec2d getCollision(AABShape aabShape);
    Vec2d getCollision(PolygonShape polygonShape);
    float rayCast(Ray ray);
    Shape getScreenPosition(Vec2d position);
    Element writeXML(Document doc);
    void readXML(Element e);

    void onDraw(GraphicsContext g);
}
