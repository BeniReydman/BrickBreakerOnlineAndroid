package net.brickbreakeronline.brickbreakeronline.framework;

/**
 * Created by Beni on 2017-06-15.
 */

public class Rect {

    public static final Rect ZERO = new Rect(0,0,0,0);

    double x;
    double y;
    double width;
    double height;

    public Rect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect(Vector2 position, Vector2 size) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSize(Vector2 size)
    {
        this.width  = size.x;
        this.height = size.y;
    }

    public void setSize(double width, double height)
    {
        this.width  = width;
        this.height = height;
    }

    public void setPosition(Vector2 position)
    {
        this.x  = position.x;
        this.y = position.y;
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Vector2 getSize()
    {
        return new Vector2(width, height);
    }

    public Vector2 getPosition()
    {
        return new Vector2(x,y);
    }

    /*public boolean collidesWith(Rect b)
    {
        Rect a = this;

        if (a.x > a.x)


    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rect rect = (Rect) o;

        if (Double.compare(rect.x, x) != 0) return false;
        if (Double.compare(rect.y, y) != 0) return false;
        if (Double.compare(rect.width, width) != 0) return false;
        return Double.compare(rect.height, height) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


}
