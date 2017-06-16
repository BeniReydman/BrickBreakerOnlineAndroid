package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

/**
 * Created by Beni on 2017-06-15.
 */

public abstract class GameBody {

    Vector2 velocity = Vector2.ZERO;
    Vector2 position = Vector2.ZERO;

    GameManager gm;
    int id;

    public GameBody (GameManager gameManager, int identification)
    {
        gm = gameManager;
        id = identification;
    }

    public int getID()
    {
        return id;
    }

    public void draw(Canvas canvas)
    {
    }


    public void networkUpdate()
    {

    }

    public void update() {

    }

    public void destroy() {

    }
}
