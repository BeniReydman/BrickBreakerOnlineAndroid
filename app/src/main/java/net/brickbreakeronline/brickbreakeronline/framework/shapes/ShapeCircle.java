package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import android.util.Log;

import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class ShapeCircle extends Shape {

    // Circles are set in the center.

    public double radius = 0; // not negative.

    public ShapeCircle(Vector2 position, double radius) {
        super(position);
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(ShapeRect s) {
        Vector2 circleDistance = new Vector2(0,0);
        circleDistance.setX(Math.abs(position.getX() - s.getCenterPosition().getX()));
        circleDistance.setY(Math.abs(position.getY() - s.getCenterPosition().getY()));

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
