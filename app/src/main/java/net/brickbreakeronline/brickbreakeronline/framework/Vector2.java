package net.brickbreakeronline.brickbreakeronline.framework;

/**
 * Created by Beni on 2017-06-15.
 */

public class Vector2 {

    public static final Vector2 ZERO = new Vector2(0.0,0.0);

    double x;
    double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLength()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector2 getReversed()
    {
        return new Vector2(-x,-y);
    }
}
