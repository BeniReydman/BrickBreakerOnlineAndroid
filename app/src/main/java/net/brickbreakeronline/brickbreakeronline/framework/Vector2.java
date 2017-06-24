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

    public Vector2(double angle) {
        x = -Math.cos(angle);
        y = Math.sin(angle);
    }

    public Vector2 reverseY() {
        return new Vector2(x,-y);
    }

    public Vector2 reverseX() {
        return new Vector2(-x,y);
    }

    public Vector2 add(Vector2 v)
    {
        return new Vector2(this.x+v.x, this.y+v.y);
    }

    public Vector2 sub(Vector2 v)
    {
        return new Vector2(this.x-v.x, this.y-v.y);
    }

    public Vector2 multiply(double f)
    {
        return new Vector2(this.x*f, this.y*f);
    }

    public Vector2 normalize()
    {
        return new Vector2(x/getLength(), y/getLength());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2 goLeft()
    {
        return new Vector2(-Math.abs(x), y);
    }


    public Vector2 goRight()
    {
        return new Vector2(Math.abs(x), y);
    }


    public Vector2 goUp()
    {
        return new Vector2(x, -Math.abs(y));
    }

    public Vector2 goDown()
    {
        return new Vector2(x, Math.abs(y));
    }

    public Vector2 clone()
    {
        return new Vector2(x, y);
    }

    public float getXf() {
        return (float)x;
    }

    public float getYf() {
        return (float)y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static double angle(Vector2 a, Vector2 b)
    {
        return Math.asin((a.x*b.x+a.y*b.y)/(a.getLength() * b.getLength()));
    }

    public double angle()
    {
        return angle(this, new Vector2(-1,0));
    }

    public double getLength()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector2 getReversed()
    {
        return new Vector2(-x,-y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
