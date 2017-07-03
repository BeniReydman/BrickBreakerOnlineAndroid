package net.brickbreakeronline.brickbreakeronline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Touch;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.networking.Session;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Beni on 2017-06-12.
 */


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    Game g;
    TextView t;
    SurfaceHolder holder;
    private Thread thread;
    private boolean looping = false;

    private long lastUpdate = System.currentTimeMillis();
    private long lastDraw = System.currentTimeMillis();
    private long lastNetworkUpdate = System.currentTimeMillis();

    private int UPS = 40;
    private int FPS = 30;
    private int NPS = 20;

    private long lastRealUPSUpdate = System.currentTimeMillis();
    private int updatesDone = 0;
    private int framesDone = 0;

    private int mode = 0;

    Session session;
    private long gameID = 0;

    Canvas canvas = null;
    Timer preGameTimer;
    Timer waitTimer;
    Timer gameStart;
    GameManager gm;


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        thread = new Thread(this);
        waitTimer = new Timer();
        preGameTimer = new Timer();
        gameStart = new Timer();


    }

    private void startLoop() {
        if (gm != null && !looping) {
            looping = true;
            thread.start();
        }
    }

    private void stopLoop() {
        looping = false;
    }

    private void createGameManager()
    {
        gm = new GameManager(this, mode);
        gm.setSession(session, gameID);
        gm.create();
    }

    /**
     * Starts the game at a specific time alerting the user of when the game will start.
     * @param time
     */
    protected void startGameAt(long time) {
        int delay = (int) (time - System.currentTimeMillis());
        resetReadyText(delay/3);
    }

    protected void startGame() {
        lastUpdate = System.currentTimeMillis();
        lastNetworkUpdate = System.currentTimeMillis();
        startLoop();
    }



    protected void createSinglePlayerGame() {
        startGameAt(System.currentTimeMillis() + 3000);
    }

    protected void createMultiplayerGame() {}

    protected void createGame() {
        g.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t = GameSurfaceView.super.getRootView().findViewById(R.id.fullscreen_content);
            }
        });


        waitTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                createGameManager();
                drawGame();
            }
        }, 350);

        setReadyText("Preparing...");

        switch (mode) {
            case GameManager.MODE_SINGLE_PLAYER:
                createSinglePlayerGame();
                break;
            case GameManager.MODE_MULTIPLAYER:
                createMultiplayerGame();
                break;
        }

    }


    private void drawGame()
    {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas(null);

            if (canvas == null) {
                return;
            }

            canvas.drawColor(Color.BLACK);

            gm.draw(canvas);

            holder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Touch.currentlyTouched = true;
                Touch.touchCoords = new Vector2(x,y);
                return true;
            case MotionEvent.ACTION_MOVE:
                Touch.touchCoords = new Vector2(x,y);
                return true;
            case MotionEvent.ACTION_UP:
                Touch.currentlyTouched = false;
                Touch.touchCoords = new Vector2(x,y);
                return true;
        }
        return false;
    }

    private void setReadyText(final int text_r_id) {
        g.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.setText(text_r_id);
            }
        });
    }

    private void setReadyText(final String text) {
        g.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.setText(text);
            }
        });
    }

    public void resetReadyText(int interval) { resetReadyText(interval, 0); }
    public void resetReadyText(final int interval, final int step)
    {

        switch (step) {
            case 0:
                setReadyText("Get Ready!");
                break;
            case 1:
                setReadyText("Get Set!");
                break;
            case 2:
                setReadyText("GO!");
                break;
            case 3:
                setReadyText("");
                startGame();
                return;
            default:
                return;
        }


        preGameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                resetReadyText(interval, step + 1);
            }
            }, interval);
    }

    @Override
    public void run() {
        while(looping && gm != null) {
            if (lastRealUPSUpdate + 1000 < System.currentTimeMillis()) {
                Log.d("UPS | FPS", String.valueOf(updatesDone)
                        + " | " + String.valueOf(framesDone));
                updatesDone = 0;
                framesDone = 0;
                lastRealUPSUpdate = System.currentTimeMillis();
            }

            if (lastUpdate + 1000.0 / UPS < System.currentTimeMillis()) {
                gm.update((double) (System.currentTimeMillis() - lastUpdate));
                lastUpdate = System.currentTimeMillis();
                updatesDone++;
            }


            if (lastDraw + 1000 / FPS < System.currentTimeMillis()) {
                drawGame();
                lastDraw = System.currentTimeMillis();
                framesDone++;
            }


            if (lastNetworkUpdate + 1000 / NPS < System.currentTimeMillis()) {
                gm.networkUpdate();
                lastNetworkUpdate = System.currentTimeMillis();
            }
            Thread.yield();
        }
    }


    public void pause() {
        stopLoop();
    }

    public void resume() {
        startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        createGame();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void setSession(Session s, long gameID) {
        if (mode != GameManager.MODE_MULTIPLAYER) {
            return;
        }

        session = s;
        this.gameID = gameID;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
