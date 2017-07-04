package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.brickbreakeronline.brickbreakeronline.CreateWorld;
import net.brickbreakeronline.brickbreakeronline.GameSurfaceView;
import net.brickbreakeronline.brickbreakeronline.R;
import net.brickbreakeronline.brickbreakeronline.networking.Session;
import net.brickbreakeronline.brickbreakeronline.objects.Brick;

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
    public static final int GAME_STATE_CANCELED      = 5;

    private static final int BODY_TYPE_BRICK            = 0;
    private static final int BODY_TYPE_BALL             = 1;
    private static final int BODY_TYPE_PADDLE           = 2;
    private static final int BODY_TYPE_PLAYER_PADDLE    = 3;

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
            setHandlers();
            doJoinGame();
        }
    }

    private void setHandlers() {

        session.on("GameStateMessage", onGameStateMessage);
        session.on("GameStartTime", onGameStartTime);

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
        // gets moving and non moving objects
        ArrayList<GameBody> moving = new ArrayList<GameBody>();
        ArrayList<GameBody> nonmoving = new ArrayList<GameBody>();
        for (GameBody body : getBodiesWithShapes()) {
            if (body.velocity.getX() == 0 && body.velocity.getY() == 0) {
                nonmoving.add(body);
            } else {
                moving.add(body);
            }
        }

        for (GameBody a : moving) {
            // check moving bodies with nonmoving bodies
            for (GameBody b : nonmoving) {
                if (a != b && a.getShape().collidesWith(b.getShape())) {
                    // since nonmoving only iterates once we need to call b.onCollide(a, delta)
                    a.onCollide(b, delta);
                    b.onCollide(a, delta);
                }
            }

            // check moving bodies with moving bodies
            for (GameBody b : moving) {
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


    public void setSession(Session s, long gameID) {
        if (mode != GameManager.MODE_MULTIPLAYER) {
            return;
        }

        session = s;
        this.gameID = gameID;
    }

    private void doJoinGame() {

        JsonObject obj = new JsonObject();
        obj.addProperty("game_id", gameID);

        session.request("GameService.JoinGame", obj, onJoinGame);
    }

    private void addBodies(JsonArray bodies) throws NullPointerException {
        for (int i = 0; i < bodies.size(); i++) {
            JsonObject body = bodies.get(i).getAsJsonObject();

            int id = body.get("id").getAsInt();
            int type = body.get("t").getAsInt();
            JsonObject info = body.get("b").getAsJsonObject();

            switch (type) {
                case BODY_TYPE_BRICK:
                    Brick brick = new Brick(this, id, info);
                    this.bodies.add(brick);
                    break;
            }

        }


    }

    private Session.MessageListener onJoinGame = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            Log.d("Game", "Join Game: " + obj.toString());

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
                int width = data.get("width").getAsNumber().intValue();
                int height = data.get("height").getAsNumber().intValue();
                String opposingName = data.get("opposing_name").getAsString();

                JsonArray bodies = data.get("bodies").getAsJsonArray();
                addBodies(bodies);

                Log.d("Game", "Opposing name: " + opposingName);
                view.drawGame();


            } catch(NullPointerException e) {
                Log.d("Game", "Error joining game. Null: " + obj.toString());
                // TODO: return to matchmaking
            }
        }
    };

    private Session.MessageListener onGameStartTime = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            Log.d("GAME", "Object: " + obj.toString());
            int duration;
            long start;
            try {
                duration = obj.get("dur").getAsInt();
                start = obj.get("start").getAsLong();
            } catch(NullPointerException e) {
                Log.d("Game", "Start Error: " + obj.toString());
                return;
            }

            Log.d("GAME", "START: " + start);
            Log.d("GAME", "DURATION: " + duration);
            Log.d("GAME", "TIME - NOW: " + duration);
            /*
            session.doPing
             */

            // TODO: Check ping, make sure it is within a good range, and correct duration.
            view.startGameAt(System.currentTimeMillis() + duration);
        }
    };

    private Session.MessageListener onGameStateMessage = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {

            int state = -1;
            try {
                state = obj.get("state").getAsInt();
            } catch(NullPointerException e) {
                Log.d("Game", "State error: " + obj.toString());
                return;
            }

            Log.d("Game", "State Change: " + state);

            switch (state) {
                case GAME_STATE_READY:
                    break;
                case GAME_STATE_ENDED:
                    view.goToMenu();
                    break;
                case GAME_STATE_CANCELED:
                    view.goToMatchmaking();
                    break;


            }
        }
    };

}
