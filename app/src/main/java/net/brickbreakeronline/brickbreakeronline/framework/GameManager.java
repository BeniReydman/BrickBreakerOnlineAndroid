package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;
import android.util.Log;

import com.google.gson.JsonObject;

import net.brickbreakeronline.brickbreakeronline.CreateWorld;
import net.brickbreakeronline.brickbreakeronline.GameSurfaceView;
import net.brickbreakeronline.brickbreakeronline.R;
import net.brickbreakeronline.brickbreakeronline.networking.Session;

import java.util.ArrayList;

/**
 * Created by Beni on 2017-06-13.
 */

public class GameManager {

    public static final int MODE_SINGLE_PLAYER = 0;
    public static final int MODE_MULTIPLAYER = 1;

    public static final int GAME_STATE_PREP          = 0;
    public static final int GAME_STATE_READY         = 1;
    public static final int GAME_STATE_STARTING      = 2;
    public static final int GAME_STATE_STARTED       = 3;
    public static final int GAME_STATE_ENDED         = 4;

    final String id = "";
    public final ArrayList<GameBody> bodies;
    public final ArrayList<GameBody> addQueue;
    public final ArrayList<GameBody> removeQueue;
    public final Vector2 gameSize = new Vector2(900, 1600);
    public final Vector2 screenSize;
    private GameSurfaceView view;

    private Session session;
    private int mode;
    private int status = GAME_STATE_PREP;

    private long gameID = 0;

    public GameManager(GameSurfaceView view, int mode)
    {
        screenSize  = new Vector2(view.getWidth(), view.getHeight());
        this.view = view;
        bodies      = new ArrayList<GameBody>();
        addQueue    = new ArrayList<GameBody>();
        removeQueue = new ArrayList<GameBody>();
        this.mode = mode;
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
        if (this.mode == MODE_SINGLE_PLAYER) {
            CreateWorld.createSinglePlayer(this);
        } else if (this.mode == MODE_MULTIPLAYER && session != null) {
            doJoinGame();
        }
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


        ArrayList<GameBody> bodiesWithShape = getBodiesWithShapes();
        for (GameBody a : bodiesWithShape) {
            for (GameBody b : bodiesWithShape) {
                if (a != b && a.getShape().collidesWith(b.getShape())) {
                    a.onCollide(b, delta);
                }
            }
        }

        Touch.update();
        for (GameBody body : bodies) {
            body.update(delta);
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


    private void doJoinGame() {

        JsonObject obj = new JsonObject();
        obj.addProperty("game_id", gameID);

        session.request("GameService.JoinGame", obj, onJoinGame);
    }

    private Session.MessageListener onJoinGame = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            Log.d("Matchmaking", "Join Game: " + obj.toString());

            try {

                int code = obj.get("code").getAsNumber().intValue();
                if (code != 200) {
                    // TODO: return to matchmaking
                    Log.d("Game", "Error joining game. Code: " + obj.toString());
                    return;
                }

                JsonObject data = obj.get("data").getAsJsonObject();
                int gameID = data.get("game_id").getAsNumber().intValue();
                int state = data.get("state").getAsNumber().intValue();
                String opposingName = data.get("opposing_name").getAsString();
                Log.d("Game", "Opposing name: " + opposingName);


            } catch(NullPointerException e) {
                Log.d("Game", "Error joining game. Null: " + obj.toString());
                // TODO: return to matchmaking
            }
        }
    };

    public void setSession(Session s, long gameID) {
        if (mode != GameManager.MODE_MULTIPLAYER) {
            return;
        }

        session = s;
        this.gameID = gameID;
    }
}
