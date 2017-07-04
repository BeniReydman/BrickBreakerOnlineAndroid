package net.brickbreakeronline.brickbreakeronline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.JsonObject;

import net.brickbreakeronline.brickbreakeronline.networking.Account;
import net.brickbreakeronline.brickbreakeronline.networking.Session;


/**
 * Created by Kozak on 2017-07-02.
 */

public class Matchmaking extends AppCompatActivity {

    private static final int SEARCH_ERROR = 0;
    private static final int SEARCH_SEARCHING = 1;
    private static final int SEARCH_NOT_SEARCHING = 2;

    Session session;
    TextView resultText;
    boolean keepSessionAlive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_matchmaking);
        resultText = (TextView) findViewById(R.id.matchmaking_text);


        session = Session.mainSession;

        if (session == null) {
            System.out.println("null session.");
            goToMenu();
        }

        session.resetHandlers();
        session.resetCallbacks();

        session.setHeartbeatCallback(new Session.CallbackListener() {
            @Override
            public void call() {
                Log.d("heartbeat", "www");
            }
        });

        session.setDisconnectCallback(new Session.CallbackListener() {
            @Override
            public void call() {
                Log.d("Matchmaking", "Disconnected");
                setStatusText(R.string.matchmaking_error);
                goToMenu();
            }
        });


        session.on("SearchStateMessage", onSearchState);
        session.on("PrepGame", onGamePrep);

        session.request("Manager.IsAuthenticated", onIsAuthenticated);
        setStatusText(R.string.starting_searching);
    }

    @Override
    protected void onPause() {
        super.onPause();
        session.notify("Matchmaker.StopSearching");
        setStatusText(R.string.not_searching);
        if (!keepSessionAlive) {
            Log.d("Matchmaking", "Keeping session alive.");
            session.close();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!session.isConnected()) {
            goToMenu();
        }
    }


    private void setStatusText(final int Rid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultText.setText(Rid);
            }
        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMenu();
    }

    private Session.MessageListener onSearchState = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            try {

                int code = obj.get("code").getAsNumber().intValue();
                if (code != 200) {
                    setStatusText(R.string.matchmaking_error);
                    return;
                }

                JsonObject data = obj.get("data").getAsJsonObject();
                int searchCode = data.get("code").getAsInt();

                Log.d("Matchmaker", "Search code received: " + searchCode);

                switch (searchCode) {
                    case SEARCH_SEARCHING:
                        setStatusText(R.string.searching);
                        break;
                    case SEARCH_NOT_SEARCHING:
                        setStatusText(R.string.not_searching);
                        break;
                    default:
                        setStatusText(R.string.matchmaking_error);
                        goToMenu();
                        break;
                }

            } catch(NullPointerException e) {
                setStatusText(R.string.matchmaking_error);
                goToMenu();
            }
        }
    };

    private Session.MessageListener onGamePrep = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            try {

                Log.d("Matchmaking", "PREP: " + obj.toString());

                long gameID = obj.get("game_id").getAsLong();

                if (gameID > 0) {
                    setStatusText(R.string.game_found);

                    Intent intent = new Intent(getBaseContext(), Game.class);
                    intent.putExtra("GameID", gameID);
                    Log.d("Matchmaking", "Starting game...");
                    keepSessionAlive = true;
                    startActivity(intent);
                    finish();

                }

            } catch(NullPointerException e) {
                Log.d("Matchmaking", "Error prepping game: " + obj.toString());
                setStatusText(R.string.matchmaking_error);
                goToMenu();
            }
        }
    };

    private Session.MessageListener onIsAuthenticated = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {

            try {
                int code = obj.get("code").getAsNumber().intValue();
                boolean authenticated = obj.get("data").getAsJsonObject()
                        .get("authenticated").getAsBoolean();
                if (code == 200 && authenticated) {
                    session.request("Matchmaker.StartSearching", onSearchState);
                } else if (!authenticated) {
                    goToMenu();
                }

            } catch(NullPointerException e) {
                Log.d("Matchmaking", e.toString());
            }
        }
    };



}
