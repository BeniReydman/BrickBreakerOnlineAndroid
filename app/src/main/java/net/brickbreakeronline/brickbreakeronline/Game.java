package net.brickbreakeronline.brickbreakeronline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Game extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private Button mNewGameButton;

    private View mContentView;
    private View mControlsView;

    private double frameTime;

    private boolean isRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mNewGameButton = (Button) findViewById(R.id.switch_screen);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainMenu.class);
                startActivity(intent);
            }
        });
        isRunning = true;
        //run();
    }

    /*******************************************************************************/
    /* IGNORE, FULLSCREEN SETTINGS */
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }
    /*******************************************************************************/

    public void run()
    {
        Canvas canvas = null;
        long startTime = SystemClock.uptimeMillis();

        while (isRunning)
        {
            // UPDATE TIME
            long currentTime = SystemClock.uptimeMillis();
            frameTime = (currentTime - startTime);
            startTime = currentTime;

            //SLEEP
            if(frameTime < 16)
            {
                //sleep(16 - frameTime);
            }

            //UPDATE

            //CheckCollision();
            //CheckBallBounds();
            //AdjustBatts();
            //AdvanceBall();

            // RENDER STUFF
            try
            {
               // canvas = surfaceHolder.lockCanvas(null);
                //synchronized (surfaceHolder)
                {
                    //Draw(canvas);
                }
            }
            finally
            {
                if (canvas != null)
                {
                    //surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //GET SURFACE HOLDER
        /*
        synchronized (surfaceHolder)
        {
            setState(STATE_PAUSE);
        }
        */
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hide();
        //GET SURFACE HOLDER
        /*
        synchronized (surfaceHolder)
        {
            setState(STATE_UNPAUSE);
        }
        */
    }

    protected void onDestroy()
    {
        super.onDestroy();
        //POSSIBLE SOUND MANAGER?????
        //SOUNDMANAGER.cleanup();
    }




}
