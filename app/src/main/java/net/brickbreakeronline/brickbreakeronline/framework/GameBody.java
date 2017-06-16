package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

import net.brickbreakeronline.brickbreakeronline.framework.shapes.Shape;

/**
 * Created by Beni on 2017-06-15.
 */

public abstract class GameBody {

    public Vector2 velocity;
    public Shape shape;

    protected GameManager gm;
    protected int id;

    public GameBody (GameManager gameManager, int identification)
    {
        gm = gameManager;
        id = identification;
        velocity = Vector2.ZERO;
        shape = null;
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

    public void setPosition(Vector2 pos)
    {
        shape.setPosition(pos);
    }

    public Vector2 getPosition()
    {
        return shape.position;
    }

    public void update(double delta) {
        setPosition(getPosition().add(velocity.multiply(delta)));
    }

    public void destroy() {

    }

    public void onCollide(GameBody body)
    {

    }
}
