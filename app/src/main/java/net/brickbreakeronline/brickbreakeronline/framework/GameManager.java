package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

import net.brickbreakeronline.brickbreakeronline.GameSurfaceView;
import net.brickbreakeronline.brickbreakeronline.objects.Ball;

import java.util.ArrayList;

/**
 * Created by Beni on 2017-06-13.
 */

public class GameManager {

    final String id = "";
    final int width;
    final int height;
    public final ArrayList<GameBody> bodies;
    public final ArrayList<GameBody> addQueue;
    public final ArrayList<GameBody> removeQueue;

    public GameManager(GameSurfaceView view)
    {
        width = view.getWidth();
        height = view.getHeight();
        bodies      = new ArrayList<GameBody>();
        addQueue    = new ArrayList<GameBody>();
        removeQueue = new ArrayList<GameBody>();
    }

    public GameBody getGameBodyByID(int id)
    {
        //iterate through game bodies; return game body with ID or null if not found
        return null;
    }

    public void addBody(GameBody gb)
    {
        addQueue.add(gb);
    }

    public void removeBody(GameBody gb)
    {
        removeQueue.add(gb);
    }

    public void create()
    {
        bodies.add( new Ball(this, 1) );
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
    public void update(double delta)
    {
        for (GameBody body : bodies) {
            body.update(delta);
        }

        for (GameBody body : removeQueue) {
            body.destroy();
            bodies.remove(body);
        }

        for (GameBody body : addQueue) {
            bodies.add(body);
        }


    }

    public void draw(Canvas canvas)
    {
        for (GameBody body : bodies) {
            body.draw(canvas);
        }
    }

    public void networkUpdate()
    {
        for (GameBody body : bodies) {
            body.networkUpdate();
        }

    }

}
