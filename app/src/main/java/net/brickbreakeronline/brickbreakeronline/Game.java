package net.brickbreakeronline.brickbreakeronline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private GameSurfaceView game;

    private Button mNewGameButton;

    private View mContentView;
    private View mControlsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = (GameSurfaceView) findViewById(R.id.surfaceView1);
        game.g = this;

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

    public void onClick(View v) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        game.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hide();
        game.resume();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        //POSSIBLE SOUND MANAGER?????
        //SOUNDMANAGER.cleanup();
    }

    /* TO BE LOOKED OVER LATER
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float xPosition1 = 0;
        float yPosition1 = 0;
        float xPosition2 = 0;
        float yPosition2 = 0;

        for (int pointerIndex = 0; pointerIndex < event.getPointerCount(); pointerIndex++)
        {
            if (pointerIndex == 0)
            {
                xPosition1 = event.getX(pointerIndex);
                yPosition1 = event.getY(pointerIndex);
            }

            if (pointerIndex == 1)
            {
                xPosition2 = event.getX(pointerIndex);
                yPosition2 = event.getX(pointerIndex);
            }
        }

        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                //setPaddlePosition(xPosition1, yPosition1, xPosition2, yPosition2);
                break;
        }
        return true;
    }
    */




}
