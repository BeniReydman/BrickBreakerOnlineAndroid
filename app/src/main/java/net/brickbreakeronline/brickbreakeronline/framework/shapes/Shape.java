package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import net.brickbreakeronline.brickbreakeronline.framework.Rect;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public abstract class Shape {
    public static final ShapeRect EMPTY_RECT = new ShapeRect(Rect.ZERO);

    public Vector2 position = Vector2.ZERO;

    public Shape(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public boolean collidesWith(Shape s)
    {
        if (s.getClass().equals(ShapeRect.class)) {
            return collidesWith((ShapeRect)s);
        } else if (s.getClass().equals(ShapeCircle.class)) {
            return collidesWith((ShapeCircle)s);
        }

        return false;
    }

    public abstract boolean collidesWith(ShapeRect s);
    public abstract boolean collidesWith(ShapeCircle s);

}
