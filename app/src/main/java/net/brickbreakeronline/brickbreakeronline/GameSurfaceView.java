package net.brickbreakeronline.brickbreakeronline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Beni on 2017-06-12.
 */


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    SurfaceHolder holder;
    private Timer timer;

    private long lastUpdate;
    Canvas canvas = null;
    GameManager gm;


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        lastUpdate = System.currentTimeMillis();

    }


    protected void createGame() {
        gm = new GameManager(this);
        gm.create();

        // networkupdate
        timer.schedule(new TimerTask() {
            public void run() {
                gm.networkUpdate();

            }
        }, 0, 1000 / 20);

        // update
        timer.schedule(new TimerTask() {
            public void run() {
                gm.update((System.currentTimeMillis() - lastUpdate) / 1000.0);
                lastUpdate = System.currentTimeMillis();

            }
        }, 0, 1000 / 60);

        // draw
        timer.schedule(new TimerTask() {
            public void run() {
                //UPDATE

                //CheckCollision();
                //CheckBallBounds();
                //AdjustBatts();
                //AdvanceBall();

                // RENDER STUFF
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
        }, 0, 1000 / 60);
    }

    @Override
    public void run() {

        timer.schedule(new TimerTask() {
            public void run() {
                createGame();
            }
        }, 350);
    }


    public void pause() {
        timer.cancel();
    }

    public void resume() {
        timer = new Timer();
        run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        timer = new Timer();
        run();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}
