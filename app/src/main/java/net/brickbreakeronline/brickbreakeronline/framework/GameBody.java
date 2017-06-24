package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

import net.brickbreakeronline.brickbreakeronline.framework.shapes.Shape;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapePoint;

/**
 * Created by Beni on 2017-06-15.
 */

public abstract class GameBody {

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 velocity;
    protected Shape shape;

    protected GameManager gm;
    protected int id;

    public GameBody (GameManager gameManager, int identification)
    {
        gm = gameManager;
        id = identification;
        velocity = new Vector2(0,0);
        shape = new ShapePoint(new Vector2(0,0));
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        shape.setPosition(this.shape.getPosition());
        this.shape = shape;
    }

    public void setShapeWithoutPosition(Shape shape) {
        this.shape = shape;
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

    public Vector2 getDrawPosition()
    {
        return gm.gameToScreenCoords(shape.position);
    }

    public void update(double delta) {
        setPosition(getPosition().add(getVelocity().multiply(delta)));

    }

    public void destroy()
    {

    }

    public void onCollide(GameBody body, double delta)
    {

    }
}
