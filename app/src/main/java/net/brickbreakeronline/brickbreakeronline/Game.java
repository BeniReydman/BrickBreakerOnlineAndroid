package net.brickbreakeronline.brickbreakeronline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.networking.Session;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();

    private GameSurfaceView game;

    private Button goToMenuButton;
    private boolean keepSessionAlive = false;
    private Session session;

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

        goToMenuButton = (Button) findViewById(R.id.go_to_menu);

        goToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            goToMenu();
            return;
        }

        if (extras.getBoolean("isSinglePlayer", false)) {
            game.setMode(GameManager.MODE_SINGLE_PLAYER);
            Log.d("Game", "SinglePlayer mode!");
        } else {
            session = Session.mainSession;
            long gameID = extras.getLong("GameID", 0);
            if (session.isRunning() && session.isConnected() && gameID > 0) {
                session.resetHandlers();
                session.resetCallbacks();
                initiateMultiplayer(gameID);
            } else {
                goToMenu();
            }
        }
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
        Log.d("Game", "paused");
        game.pause();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hide();
        Log.d("Game", "resumed");
        game.resume();
    }

    private void goToMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainMenu.class);
                startActivity(intent);
                keepSessionAlive = true;
                finish();
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if (!keepSessionAlive) {
            session.close();
        }

        //Do sound manager cleanup
        //SOUNDMANAGER.cleanup();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMenu();
    }

    private void initiateMultiplayer(long gameID) {
        initiateHandlers();
        game.setMode(GameManager.MODE_MULTIPLAYER);
        game.setSession(session, gameID);
    }

    private void initiateHandlers() {
        session.setDisconnectCallback(disconnectCallback);
        session.setFatalErrorCallback(errorCallback);
        session.setErrorCallback(errorCallback);
        //session.setKickCallback(kickCallback);
    }

    private Session.CallbackListener disconnectCallback = new Session.CallbackListener() {
        @Override
        public void call() {
            Log.d("Game", "Disconnected");
            goToMenu();
        }
    };

    private Session.ExceptionListener errorCallback = new Session.ExceptionListener() {
        @Override
        public void call(Exception e) {
            Log.d("Game", "Error: " + e.getMessage());
        }
    };

}
