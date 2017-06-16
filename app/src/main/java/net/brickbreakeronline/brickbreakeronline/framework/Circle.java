package net.brickbreakeronline.brickbreakeronline.framework;

/**
 * Created by Beni on 2017-06-15.
 */

public class Circle {

    public static final Circle EMPTY = new Circle(0, Vector2.ZERO);

    public double radius;
    public Vector2 position;

    public Circle(double radius, Vector2 position) {
        this.radius = radius;
        this.position = position;
    }
}
