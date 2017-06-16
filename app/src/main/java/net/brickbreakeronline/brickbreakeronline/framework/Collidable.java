package net.brickbreakeronline.brickbreakeronline.framework;

/**
 * Created by Beni on 2017-06-15.
 */

public interface Collidable {

    boolean collidesWith(CircleCollidable c);
    boolean collidesWith(RectCollidable c);


}
