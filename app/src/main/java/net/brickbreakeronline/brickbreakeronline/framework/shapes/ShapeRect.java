package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import net.brickbreakeronline.brickbreakeronline.framework.Rect;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class ShapeRect extends Shape {

    public Vector2 size = Vector2.ZERO;

    public ShapeRect(Vector2 position, Vector2 size) {
        super(position);
        this.position = position;
        this.size = size;
    }


    public ShapeRect(Rect rect) {
        super(rect.getPosition());
        this.size = rect.getSize();
    }

    public Vector2 getSize() {
        return size;
    }

    public double getWidth() {
        return size.getX();
    }


    public double getHeight() {
        return size.getY();
    }

    @Override
    public boolean collidesWith(ShapeRect s) {
        return false;
    }

    @Override
    public boolean collidesWith(ShapeCircle circle) {

        Vector2 circleDistance = Vector2.ZERO;
        circleDistance.setX(Math.abs(circle.position.getX() - this.getPosition().getX()));
        circleDistance.setY(Math.abs(circle.position.getY() - this.getPosition().getY()));

        if (circleDistance.getX() > (this.getWidth()/2 + circle.radius)) { return false; }
        if (circleDistance.getY() > (this.getHeight()/2 + circle.radius)) { return false; }

        if (circleDistance.getX() <= (this.getWidth()/2)) { return true; }
        if (circleDistance.getY() <= (this.getHeight()/2)) { return true; }

        double cornerDistance_sq = Math.pow(circleDistance.getX() - this.getWidth()/2,2) +
                Math.pow(circleDistance.getY() - this.getHeight()/2,2);

        return (cornerDistance_sq <= (Math.pow( circle.radius, 2) ));
    }
}
