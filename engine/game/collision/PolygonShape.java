package engine.game.collision;

import Final.XMLProcessor;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;


public class PolygonShape implements Shape {
    protected Vec2d[] points;

    public PolygonShape() {
        XMLProcessor.tag2shape.put("PolygonShape", PolygonShape.class);
    }
    public PolygonShape(Vec2d ... points) {
        XMLProcessor.tag2shape.put("PolygonShape", PolygonShape.class);
        this.points = points;
    }

    public int getNumPoints() {
        return points.length;
    }

    public Vec2d getPoint(int i) {
        return points[i];
    }

    private Vec2d isColliding(Vec2d[] points1, Vec2d[] points2, Vec2d axis) {
        double maxPoint1, minPoint1;
        double maxPoint2, minPoint2;

        maxPoint1 = axis.dot(points1[0]);
        minPoint1 = axis.dot(points1[0]);
        for(Vec2d point: points1) {
            double projection = axis.dot(point);
            if(maxPoint1 < projection) maxPoint1  = projection;
            if(minPoint1 > projection) minPoint1 = projection;
        }

        maxPoint2 = axis.dot(points2[0]);
        minPoint2 = axis.dot(points2[0]);
        for(Vec2d point: points2) {
            double projection = axis.dot(point);
            if(maxPoint2 < projection) maxPoint2  = projection;
            if(minPoint2 > projection) minPoint2 = projection;
        }

        if(maxPoint2 <= minPoint1 || maxPoint1 <= minPoint2) return null;

        Vec2d mtv = null, mtv1 = null, mtv2 = null;
        if(minPoint2 < maxPoint1) mtv1 = axis.smult(maxPoint1 - minPoint2);
        if(maxPoint2 > minPoint1) mtv2 = axis.smult(minPoint1 - maxPoint2);
        if(mtv1 == null) mtv = mtv2;
        else if(mtv2 == null) mtv = mtv1;
        else if(mtv1.mag() < mtv2.mag()) mtv = mtv1;
        else mtv = mtv2;

        return mtv;
    }

    @Override
    public Vec2d getCollision(Shape shape) {
        return shape.getCollision(this);
    }

    @Override
    public Vec2d getCollision(Vec2d point) {
        Vec2d edge, axis;

        Vec2d mtv = null, tmp;

        Vec2d[] points = {point};

        for(int i = 0; i < getNumPoints(); i++) {
            edge = getPoint((i + 1) % getNumPoints()).minus(getPoint(i));
            axis = edge.perpendicular().normalize();
            tmp = isColliding(points, this.points, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag() > tmp.mag()) mtv = tmp;
        }
        return mtv;
    }

    @Override
    public Vec2d getCollision(CircleShape circleShape) {
        Vec2d edge, axis;
        Vec2d mtv = null, tmp;
        Vec2d[] circlePoints;

        double minDistance = getPoint(0).dist(circleShape.center);
        axis = getPoint(0).minus(circleShape.center).normalize();
        for(Vec2d point: points) {
            double curDist = point.dist(circleShape.center);
            if(curDist < minDistance) {
                minDistance = curDist;
                axis = point.minus(circleShape.center).normalize();
            }
        }

        circlePoints = new Vec2d[]{circleShape.center.plus(axis.smult(circleShape.radius)), circleShape.center.minus(axis.smult(circleShape.radius))};
        tmp = isColliding(points, circlePoints, axis);
        if(tmp == null) return null;
        else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp;

        for(int i = 0; i < getNumPoints(); i++) {
            edge = getPoint((i + 1) % getNumPoints()).minus(getPoint(i));
            axis = edge.perpendicular().normalize();
            circlePoints = new Vec2d[]{circleShape.center.plus(axis.smult(circleShape.radius)), circleShape.center.minus(axis.smult(circleShape.radius))};
            tmp = isColliding(circlePoints, points, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp;
        }

        return mtv;
    }

    @Override
    public Vec2d getCollision(AABShape aabShape) {
        Vec2d edge, axis;
        Vec2d[] AABPoints = {aabShape.topLeft,
                aabShape.topLeft.plus((float)aabShape.size.x, 0),
                aabShape.topLeft.plus(0, (float)aabShape.size.y),
                aabShape.topLeft.plus(aabShape.size)};

        Vec2d mtv = null, tmp;

        Vec2d[] aabEdges = {new Vec2d(0, 1), new Vec2d(-1, 0), new Vec2d(0, -1), new Vec2d(1, 0)};
        for(Vec2d aabEdge: aabEdges) {
            edge = aabEdge;
            axis = edge.perpendicular().normalize();
            tmp = isColliding(points, AABPoints, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp.smult(-1);
        }

        for(int i = 0; i < getNumPoints(); i++) {
            edge = getPoint((i + 1) % getNumPoints()).minus(getPoint(i));
            axis = edge.perpendicular().normalize();
            tmp = isColliding(AABPoints, points, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp;
        }

        return mtv;
    }

    @Override
    public Vec2d getCollision(PolygonShape polygonShape) {
        Vec2d edge, axis;

        Vec2d mtv = null, tmp;

        for(int i = 0; i < getNumPoints(); i++) {
            edge = getPoint((i + 1) % getNumPoints()).minus(getPoint(i));
            axis = edge.perpendicular().normalize();
            tmp = isColliding(polygonShape.points, points, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp;
        }

        for(int i = 0; i < polygonShape.getNumPoints(); i++) {
            edge = polygonShape.getPoint((i + 1) % polygonShape.getNumPoints()).minus(polygonShape.getPoint(i));
            axis = edge.perpendicular().normalize();
            tmp = isColliding(points, polygonShape.points, axis);
            if(tmp == null) return null;
            else if(mtv == null || mtv.mag2() > tmp.mag2()) mtv = tmp.smult(-1);
        }

        return mtv;
    }

    @Override
    public Shape getScreenPosition(Vec2d position) {
        Vec2d[] newPoints = new Vec2d[getNumPoints()];
        for(int i = 0; i < getNumPoints(); i++) {
            newPoints[i] = getPoint(i).plus(position);
        }
        return new PolygonShape(newPoints);
    }

    @Override
    public float rayCast(Ray ray) {
        return ray.rayCast(this);
    }

    @Override
    public void onDraw(GraphicsContext g) {
        g.setLineWidth(3);
        g.setStroke(Color.rgb(255, 0, 0));
        double[] x = new double[points.length];
        double[] y = new double[points.length];
        for(int i = 0; i < points.length; i++) {
            x[i] = points[i].x;
            y[i] = points[i].y;
        }
        g.strokePolygon(x, y, points.length);
        g.setLineWidth(1);
    }

    @Override
    public Element writeXML(Document doc) {
        Element shape = doc.createElement("PolygonShape");
        shape.setAttribute("points", Arrays.toString(points));

        return shape;
    }

    @Override
    public void readXML(Element e) {
        String pointsStr = e.getAttribute("points").replace("[", "").replace("]", "");
        String[] indices = pointsStr.split("\\),");
        ArrayList<Vec2d> points = new ArrayList<>();
        for(String index: indices) {
            points.add(Vec2d.toVec2d(index));
        }
        this.points = points.toArray(new Vec2d[points.size()]);
    }
}
