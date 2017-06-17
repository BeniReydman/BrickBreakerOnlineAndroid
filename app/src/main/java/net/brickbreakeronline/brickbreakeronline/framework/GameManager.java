package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;

import net.brickbreakeronline.brickbreakeronline.CreateWorld;
import net.brickbreakeronline.brickbreakeronline.GameSurfaceView;

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
        CreateWorld.createSinglePlayer(this);
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

        ArrayList<GameBody> bodiesWithShape = getBodiesWithShapes();
        for (GameBody a : bodiesWithShape) {
            for (GameBody b : bodiesWithShape) {
                if (a != b && a.getShape().collidesWith(b.getShape())) {
                    a.onCollide(b);
                }
            }
        }

        for (GameBody body : removeQueue) {
            body.destroy();
            bodies.remove(body);
        }

        for (GameBody body : addQueue) {
            bodies.add(body);
        }
    }

    public ArrayList<GameBody> getBodiesWithShapes()
    {
        ArrayList<GameBody> bodiesWithShape = new ArrayList<>();
        for (GameBody body : bodies) {
            if (body.getShape() != null) {
                bodiesWithShape.add(body);
            }
        }
        return bodiesWithShape;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawARGB(0, 0, 0, 0);
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
