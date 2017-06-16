package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

/**
 * Created by Beni on 2017-06-15.
 */

public abstract class GameBody {

    public Vector2 velocity;
    public Vector2 position;

    protected GameManager gm;
    protected int id;

    public GameBody (GameManager gameManager, int identification)
    {
        gm = gameManager;
        id = identification;
        velocity = Vector2.ZERO;
        position = Vector2.ZERO;
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

    public void update(double delta) {
        position = position.add(velocity.multiply(delta));
    }

    public void destroy() {

    }
}
