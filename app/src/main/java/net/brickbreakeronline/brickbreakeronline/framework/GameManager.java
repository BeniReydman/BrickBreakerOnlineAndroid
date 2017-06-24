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
    public final ArrayList<GameBody> bodies;
    public final ArrayList<GameBody> addQueue;
    public final ArrayList<GameBody> removeQueue;
    public final Vector2 gameSize = new Vector2(900, 1600);
    public final Vector2 screenSize;

    public GameManager(GameSurfaceView view)
    {
        screenSize  = new Vector2(view.getWidth(), view.getHeight());
        bodies      = new ArrayList<GameBody>();
        addQueue    = new ArrayList<GameBody>();
        removeQueue = new ArrayList<GameBody>();
    }

    public GameBody getGameBodyByID(int id)
    {
        //iterate through game bodies; return game body with ID or null if not found
        // also iterate through brick holder
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

    public int getGameWidth()
    {
        return (int)gameSize.getX();
    }

    public int getGameHeight()
    {
        return (int)gameSize.getY();
    }
    public void update(double delta)
    {
        Touch.update();
        for (GameBody body : bodies) {
            body.update(delta);
        }

        ArrayList<GameBody> bodiesWithShape = getBodiesWithShapes();
        for (GameBody a : bodiesWithShape) {
            for (GameBody b : bodiesWithShape) {
                if (a != b && a.getShape().collidesWith(b.getShape())) {
                    a.onCollide(b, delta);
                }
            }
        }

        for (GameBody body : removeQueue) {
            body.destroy();
            bodies.remove(body);
        }
        removeQueue.clear();

        for (GameBody body : addQueue) {
            bodies.add(body);
        }
        addQueue.clear();
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

    public Vector2 getScreenRatio()
    {
        return new Vector2(
                (screenSize.x / gameSize.x),
                (screenSize.y / gameSize.y)
        );
    }

    public Vector2 gameToScreenCoords(Vector2 coord)
    {
        return new Vector2(
                coord.x * (screenSize.x / gameSize.x),
                coord.y * (screenSize.y / gameSize.y)
        );
    }

    public Vector2 screenToGameCoords(Vector2 coord)
    {
        return new Vector2(
                coord.x * (gameSize.x / screenSize.x),
                coord.y * (gameSize.y / screenSize.y)
        );
    }

}
