package net.brickbreakeronline.brickbreakeronline.framework;

/**
 * Created by Beni on 2017-06-21.
 */

public class Touch {

    public static boolean lastUpdateTouched = false;
    public static boolean updateTouched = false;
    public static boolean currentlyTouched = false;
    public static Vector2 touchCoords = new Vector2(0,0);

    public static void update()
    {
        lastUpdateTouched = updateTouched;
        updateTouched = currentlyTouched;
    }

    public static boolean isTouch()
    {
        return updateTouched;
    }

    public static boolean isTouchDown()
    {
        return updateTouched && !lastUpdateTouched;
    }

    public static boolean isTouchUp()
    {
        return !updateTouched && lastUpdateTouched;
    }
}
