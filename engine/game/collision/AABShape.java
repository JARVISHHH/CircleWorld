package engine.game.collision;

import Final.XMLProcessor;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static java.lang.Math.abs;

public class AABShape implements Shape{
    protected Vec2d topLeft;
    protected Vec2d size;

    public AABShape() {
        XMLProcessor.tag2shape.put("AABShape", AABShape.class);
    }

    public AABShape(Vec2d topLeft, Vec2d size) {
        XMLProcessor.tag2shape.put("AABShape", AABShape.class);
        this.topLeft = topLeft;
        this.size = size;
    }

    private double getLeastDistance(double a, double b) {
        if(Math.min(a, b) >= 0) return Math.min(a, b);
        if(Math.max(a, b) <= 0) return Math.max(a, b);
        if(a + b > 0) {
            if(a > 0) return b;
            else return a;
        } else {
            if(a > 0) return a;
            else return b;
        }
    }

    private Vec2d getMTV(Vec2d minPoint1, Vec2d maxPoint1, Vec2d minPoint2, Vec2d maxPoint2) {
        Vec2d MTV;
        double up = minPoint2.y - maxPoint1.y;
        double down = maxPoint2.y - minPoint1.y;
        double left = minPoint2.x - maxPoint1.x;
        double right = maxPoint2.x - minPoint1.x;
        double magnitude;
        double magnitudeY = getLeastDistance(up, down);
        double magnitudeX = getLeastDistance(left, right);
        Vec2d MTVy = new Vec2d(0, 1);
        Vec2d MTVx = new Vec2d(1, 0);
        if(abs(magnitudeY) < abs(magnitudeX)) {
            magnitude = magnitudeY;
            MTV = MTVy;
        } else {
            magnitude = magnitudeX;
            MTV = MTVx;
        }
        MTV = MTV.smult(magnitude);
        return MTV;
    }

    public boolean checkCollision(Vec2d point) {
        double minX = topLeft.x, maxX = topLeft.x + size.x;
        double minY = topLeft.y, maxY = topLeft.y + size.y;
        return point.x > minX && point.x < maxX && point.y > minY && point.y < maxY;
    }

    public boolean checkCollision(CircleShape circleShape) {
        return circleShape.checkCollision(this);
    }

    public boolean checkCollision(AABShape aabShape) {
        Vec2d minPoint1 = topLeft, maxPoint1 = new Vec2d(topLeft.x + size.x, topLeft.y + size.y);
        Vec2d minPoint2 = aabShape.topLeft, maxPoint2 = new Vec2d(aabShape.topLeft.x + aabShape.size.x, aabShape.topLeft.y + aabShape.size.y);

        return minPoint1.x < maxPoint2.x && minPoint2.x < maxPoint1.x &&
                minPoint1.y < maxPoint2.y && minPoint2.y < maxPoint1.y;
    }

    @Override
    public Shape getScreenPosition(Vec2d position) {
        Vec2d newTopLeft = new Vec2d(topLeft.x + position.x, topLeft.y + position.y);
        return new AABShape(newTopLeft, size);
    }

    @Override
    public Vec2d getCollision(Shape shape) {
        return shape.getCollision(this);
    }

    @Override
    public Vec2d getCollision(Vec2d point) {
        if(!checkCollision(point)) return null;

        Vec2d minPoint1 = this.topLeft, maxPoint1 = new Vec2d(this.topLeft.x + this.size.x, this.topLeft.y + this.size.y);
        Vec2d minPoint2 = point, maxPoint2 = point;

        return getMTV(minPoint1, maxPoint1, minPoint2, maxPoint2);
    }

    @Override
    public Vec2d getCollision(AABShape aabShape) {
        if(!checkCollision(aabShape)) return null;

        Vec2d minPoint1 = this.topLeft, maxPoint1 = new Vec2d(this.topLeft.x + this.size.x, this.topLeft.y + this.size.y);
        Vec2d minPoint2 = aabShape.topLeft, maxPoint2 = new Vec2d(aabShape.topLeft.x + aabShape.size.x, aabShape.topLeft.y + aabShape.size.y);

        return getMTV(minPoint1, maxPoint1, minPoint2, maxPoint2);
    }

    @Override
    public Vec2d getCollision(CircleShape circleShape) {
        if(!checkCollision(circleShape)) return null;

        Vec2d MTV;
        if(checkCollision(circleShape.center)) {
            MTV = getCollision(circleShape.center);
            MTV = MTV.normalize().smult(MTV.mag() + circleShape.radius);
        } else {
            Vec2d minPoint = this.topLeft, maxPoint = new Vec2d(this.topLeft.x + this.size.x, this.topLeft.y + this.size.y);
            Vec2d clampPoint = new Vec2d(Math.max(minPoint.x, Math.min(maxPoint.x, circleShape.center.x)), Math.max(minPoint.y, Math.min(maxPoint.y, circleShape.center.y)));
            MTV = circleShape.getCollision(clampPoint);
            MTV = MTV.smult(-1).normalize().smult(circleShape.radius - MTV.mag());
        }
        return MTV;
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
    public void onDraw(GraphicsContext g) {
        g.setLineWidth(3);
        g.setStroke(Color.rgb(255, 0, 0));
        g.strokeRect(topLeft.x, topLeft.y, size.x, size.y);
        g.setLineWidth(1);
    }

    @Override
    public Element writeXML(Document doc) {
        Element shape = doc.createElement("AABShape");
        shape.setAttribute("topLeft", String.valueOf(topLeft));
        shape.setAttribute("size", String.valueOf(size));

        return shape;
    }

    @Override
    public void readXML(Element e) {
        topLeft = Vec2d.toVec2d(e.getAttribute("topLeft"));
        size = Vec2d.toVec2d(e.getAttribute("size"));
    }
}
