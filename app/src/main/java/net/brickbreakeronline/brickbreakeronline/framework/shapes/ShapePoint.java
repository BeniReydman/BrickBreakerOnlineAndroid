package net.brickbreakeronline.brickbreakeronline.framework.shapes;

import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class ShapePoint extends Shape {

    // Circles are set in the center.

    public ShapePoint(Vector2 position) {
        super(position);
    }

    @Override
    public boolean collidesWith(ShapeRect s) {
        return false;
    }

    @Override
    public boolean collidesWith(ShapeCircle s) {
        return false;
    }


}
