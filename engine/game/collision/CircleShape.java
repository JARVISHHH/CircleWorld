package engine.game.collision;

import Nin2.XMLProcessor;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CircleShape implements Shape{
    protected Vec2d center;
    protected double radius;

    public CircleShape() {
        XMLProcessor.tag2shape.put("CircleShape", CircleShape.class);
    }

    public CircleShape(Vec2d center, double radius) {
        XMLProcessor.tag2shape.put("CircleShape", CircleShape.class);
        this.center = center;
        this.radius = radius;
    }


    public boolean checkCollision(Vec2d point) {
        double distance = (center.x - point.x) * (center.x - point.x) + (center.y - point.y) * (center.y - point.y);
        double maxDistance = radius * radius;
        return distance < maxDistance;
    }

    public boolean checkCollision(CircleShape circleShape) {
        double centerDistance = (center.x - circleShape.center.x) * (center.x - circleShape.center.x) + (center.y - circleShape.center.y) * (center.y - circleShape.center.y);
        double maxDistance = (radius + circleShape.radius) * (radius + circleShape.radius);
        return centerDistance < maxDistance;
    }

    public boolean checkCollision(AABShape aabShape) {
        Vec2d minPoint = aabShape.topLeft, maxPoint = new Vec2d(aabShape.topLeft.x + aabShape.size.x, aabShape.topLeft.y + aabShape.size.y);
        Vec2d clampPoint = new Vec2d(Math.max(minPoint.x, Math.min(maxPoint.x, center.x)), Math.max(minPoint.y, Math.min(maxPoint.y, center.y)));
        return checkCollision(clampPoint);
    }

    @Override
    public Shape getScreenPosition(Vec2d position) {
        Vec2d newCenter = new Vec2d(center.x + position.x, center.y + position.y);
        return new CircleShape(newCenter, radius);
    }

    @Override
    public Vec2d getCollision(Shape shape) {
        return shape.getCollision(this);
    }

    @Override
    public Vec2d getCollision(Vec2d point) {
        if(!checkCollision(point)) return null;
        double magnitude = this.center.dist(point);
        return this.center.minus(point).normalize().smult(magnitude);
    }

    @Override
    public Vec2d getCollision(AABShape aabShape) {
        if(!checkCollision(aabShape)) return null;
        return aabShape.getCollision(this).smult(-1);
    }

    @Override
    public Vec2d getCollision(CircleShape circleShape) {
        if(!checkCollision(circleShape)) return null;
        double magnitude = this.radius + circleShape.radius - this.center.dist(circleShape.center);
        return this.center.minus(circleShape.center).normalize().smult(magnitude);
    }

    @Override
    public Vec2d getCollision(PolygonShape polygonShape) {
        Vec2d mtv = polygonShape.getCollision(this);
        if(mtv == null) return null;
        return mtv.smult(-1);
    }

    @Override
    public float rayCast(Ray ray) {
        return ray.rayCast(this);
    }

    @Override
    public Element writeXML(Document doc) {
        Element shape = doc.createElement("CircleShape");
        shape.setAttribute("center", String.valueOf(center));
        shape.setAttribute("radius", String.valueOf(radius));

        return shape;
    }

    @Override
    public void readXML(Element e) {
        center = Vec2d.toVec2d(e.getAttribute("center"));
        radius = Double.parseDouble(e.getAttribute("radius"));
    }
}
