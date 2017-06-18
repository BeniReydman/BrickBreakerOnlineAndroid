package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import net.brickbreakeronline.brickbreakeronline.framework.Rect;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class ShapeRect extends Shape {

    private Vector2 size;

    public ShapeRect(Vector2 position, Vector2 size) {
        super(position);
        this.position = position;
        this.size = size;
    }


    public Vector2 getCenterPosition()
    {
        return getPosition().add(getSize().multiply(0.5));
    }

    public ShapeRect(Rect rect) {
        super(rect.getPosition());
        this.size = rect.getSize();
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
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
        return circle.collidesWith(this);
    }
}
