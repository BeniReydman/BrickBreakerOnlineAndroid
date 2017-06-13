package net.brickbreakeronline.brickbreakeronline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Beni on 2017-06-12.
 */


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    SurfaceHolder holder;
    private Timer timer = new Timer();


    private boolean isRunning;

    private double frameTime;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void run () {
        timer.schedule(new TimerTask() {
            public void run() {
                float x = getWidth();
                Canvas canvas = null;
                long startTime = SystemClock.uptimeMillis();
                while (isRunning) {
                    // UPDATE TIME
                    long currentTime = SystemClock.uptimeMillis();
                    frameTime = (currentTime - startTime);
                    startTime = currentTime;
                    
                    //UPDATE

                    //CheckCollision();
                    //CheckBallBounds();
                    //AdjustBatts();
                    //AdvanceBall();

                    // RENDER STUFF
                    try {
                        if (!holder.getSurface().isValid())
                            continue;
                        canvas = holder.lockCanvas(null);
                        synchronized (holder) {
                            Paint paint = new Paint();
                            paint.setStyle(Paint.Style.FILL);
                            paint.setColor(Color.BLUE);
                            canvas.drawARGB(0, 0, 0, 0);
                            canvas.drawCircle(x, getHeight() / 2, 100, paint);
                            x++;
                            //Draw(canvas);
                        }
                    } finally {
                        if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                    //isRunning = false; //FOR DEBUGGING PURPOSES
                }
            }
        }, 0, 60 * 1000);
    }


    public void pause()
    {
        isRunning = false;
        timer.cancel();
    }

    public void resume()
    {
        isRunning = true;
        run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        run();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

}
