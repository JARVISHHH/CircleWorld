package engine.game.collision;


import engine.game.components.CollisionComponent;
import engine.support.Vec2d;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static java.lang.Math.sqrt;

public class Ray{

    public final Vec2d src;

    public final Vec2d dir;

    public Ray(Vec2d src, Vec2d dir) {
        this.src = src;
        this.dir = dir;
    }

    public Ray getScreenPosition(Vec2d position) {
        return new Ray(src.plus(position), dir);
    }

    private float rayCastEdges(Vec2d[] points) {
        float t = -1;

        for(int i = 0; i < points.length; i++) {
            double sign1 = points[(i + 1) % points.length].minus(src).cross(dir);
            double sign2 = points[i].minus(src).cross(dir);
            if(sign1 * sign2 > 0) continue;
            Vec2d edge = points[(i + 1) % points.length].minus(points[i]);
            Vec2d perpendicular = edge.perpendicular().normalize();
            float tmp = (float)(points[i].minus(src).dot(perpendicular) / dir.dot(perpendicular));
            if(t == -1 || tmp > 0 && tmp < t) t = tmp;
        }

        return t;
    }

    public float rayCast(AABShape aabShape) {
        Vec2d points[] = {aabShape.topLeft,
                          aabShape.topLeft.plus(0, (float)aabShape.size.y),
                          aabShape.topLeft.plus(aabShape.size),
                          aabShape.topLeft.plus((float)aabShape.size.x, 0)};
        float t = rayCastEdges(points);
        return t;
    }

    public float rayCast(CircleShape circleShape) {
        double src2center = src.dist(circleShape.center);
        double projectionLength = dir.dot(circleShape.center.minus(src));
        if(projectionLength < 0 && src2center > circleShape.radius) return -1;
        Vec2d projectionPoint = src.plus(dir.smult(projectionLength));
        double projection2center = projectionPoint.dist(circleShape.center);
        if(src2center < circleShape.radius) {
            float t = (float)(projectionLength + sqrt(circleShape.radius * circleShape.radius - projection2center * projection2center));
            return t;
        }
        if(projection2center > circleShape.radius) return -1;
        float t = (float)(projectionLength	- sqrt(circleShape.radius * circleShape.radius - projection2center * projection2center));
        return t;
    }

    public float rayCast(PolygonShape polygonShape) {
        Vec2d points[] = polygonShape.points;

        float t = rayCastEdges(points);
        return t;
    }

    public Element writeXML(Document doc) {
        Element shape = doc.createElement("shape");
        shape.setAttribute("src", String.valueOf(src));
        shape.setAttribute("dir", String.valueOf(dir));

        return shape;
    }
}
