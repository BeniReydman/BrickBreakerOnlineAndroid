package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class ShapeCircle extends Shape {

    // Circles are set in the center.

    public double radius = 0; // not negative.

    public ShapeCircle(Vector2 position) {
        super(position);
    }

    @Override
    public boolean collidesWith(ShapeRect s) {
        Vector2 circleDistance = Vector2.ZERO;
        circleDistance.setX(Math.abs(position.getX() - s.getPosition().getX()));
        circleDistance.setY(Math.abs(position.getY() - s.getPosition().getY()));

        if (circleDistance.getX() > (s.getWidth()/2 + this.radius)) { return false; }
        if (circleDistance.getY() > (s.getHeight()/2 + this.radius)) { return false; }

        if (circleDistance.getX() <= (s.getWidth()/2)) { return true; }
        if (circleDistance.getY() <= (s.getHeight()/2)) { return true; }

        double cornerDistance_sq = Math.pow(circleDistance.getX() - s.getWidth()/2,2) +
                Math.pow(circleDistance.getY() - s.getHeight()/2,2);

        return (cornerDistance_sq <= (Math.pow( this.radius, 2) ));
    }

    @Override
    public boolean collidesWith(ShapeCircle s) {
        return false;
    }
}
