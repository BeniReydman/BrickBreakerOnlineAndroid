package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.util.Log;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

import java.util.ArrayList;

/**
 * Created by Beni on 2017-06-18.
 */

public class BrickHolder extends GameBody {

    ArrayList<Brick> bricks;
    ArrayList<Brick> removeQueue;

    public BrickHolder(GameManager gameManager, int identification) {
        super(gameManager, identification);
        this.shape = new ShapeRect(new Vector2(0,0), new Vector2(0,0));
        bricks = new ArrayList<>();
        removeQueue = new ArrayList<>();
    }

    public void addBrick(Brick brick)
    {
        bricks.add(brick);
        brick.holder = this;
    }


    public void removeBrick(Brick brick)
    {
        removeQueue.add(brick);
    }

    public void resize()
    {
        Vector2 min = new Vector2(Double.NaN,Double.NaN);
        Vector2 max = new Vector2(Double.NaN,Double.NaN);
        for (Brick brick : bricks) {
            if (brick.getPosition().getX() < min.getX() || Double.isNaN(min.getX())) {
                min.setX(brick.getPosition().getX());
            }
            if (brick.getPosition().getY() < min.getY() || Double.isNaN(min.getY())) {
                Log.d("posy", String.valueOf(brick.getPosition().getY()));
                Log.d("miny", String.valueOf(min.getY()));
                min.setY(brick.getPosition().getY());
            }
            if (brick.getPosition().getX() + brick.getSize().getX() > max.getX() || Double.isNaN(max.getX())) {
                max.setX(brick.getPosition().getX() + brick.getSize().getX());
            }
            if (brick.getPosition().getY() + brick.getSize().getY() > max.getY() || Double.isNaN(max.getY())) {
                max.setY(brick.getPosition().getY() + brick.getSize().getY());
            }
        }

        setPosition(min);
        ((ShapeRect)shape).setSize(max.sub(min));
        Log.d("RESIZED TO", ((ShapeRect)shape).getSize().toString());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (Brick b : bricks) {
            b.draw(canvas);
        }
    }

    @Override
    public void networkUpdate() {
        super.networkUpdate();
        for (Brick b : bricks) {
            b.networkUpdate();
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        for (Brick b : bricks) {
            b.update(delta);
        }

        for (Brick brick : removeQueue) {
            brick.destroy();
            bricks.remove(brick);
        }
        removeQueue.clear();
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Brick b : bricks) {
            b.destroy();
        }
    }

    @Override
    public void onCollide(GameBody body, double delta) {
        super.onCollide(body, delta);
        for(Brick brick : bricks) {
            if (brick.getShape().collidesWith(body.getShape())) {
                body.onCollide(brick, delta);
                brick.onCollide(body, delta);
            }
        }

    }
}
