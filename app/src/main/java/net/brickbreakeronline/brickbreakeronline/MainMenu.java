package net.brickbreakeronline.brickbreakeronline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.gson.JsonObject;

import net.brickbreakeronline.brickbreakeronline.networking.Account;
import net.brickbreakeronline.brickbreakeronline.networking.Session;

public class MainMenu extends AppCompatActivity {

    public static final String host = "192.168.0.182";
    public static final int port = 3250;

    private Session session;
    private Account acc;
    boolean accountLoaded = false;
    boolean keepSessionAlive = false;

    private ImageButton mNewGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainmenu);

        mNewGameButton = (ImageButton) findViewById(R.id.switch_screen);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (accountLoaded) {
                Intent intent = new Intent(getBaseContext(), Matchmaking.class);
                startActivity(intent);
                keepSessionAlive = true;
                finish();
            } else if (!session.isConnected()) {
                startConnection();
            }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (session != null && !keepSessionAlive) {
            accountLoaded = false;
            session.close();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        startConnection();
    }

    private void startConnection() {

        if (Session.mainSession != null && Session.mainSession.isConnected()) {
            session = Session.mainSession;
            resetHandlers();
            checkAuthenticated();
        } else {
            session = new Session(host, port);
            Session.mainSession = session;
            resetHandlers();
            session.connect();
            session.start();
        }
    }

    private void resetHandlers() {
        if (session == null) {
            startConnection();
        } else {
            session.resetHandlers();
            session.resetCallbacks();
            session.setConnectFailedCallback(connectFailedCallback);
            session.setHandshakeCallback(handshakeCallback);
            session.setFatalErrorCallback(fatalErrorCallback);
            session.setFatalErrorCallback(errorCallback);
            session.setDisconnectCallback(disconnectCallback);
        }
    }

    private void checkAuthenticated() {
        session.request("Manager.IsAuthenticated", onIsAuthenticated);
    }

    private void doAuthenticate() {
        acc = retrieveAccount();
        if (acc.id == 0) {
            doRegister();
        } else {
            doLogin(acc.id, acc.key);
        }
    }

    private String generateName()
    {
        return "Willis";
    }

    private void doRegister() {
        Log.d("MainMenuDebug", "Register");

        JsonObject obj = new JsonObject();
        obj.addProperty("name", generateName());

        session.request("Manager.Register", obj, onRegister);
    }

    private void doLogin(long id, String key) {
        Log.d("MainMenuDebug", "Login");

        JsonObject auth = new JsonObject();
        auth.addProperty("id", id);
        auth.addProperty("key", key);
        session.request("Manager.Authenticate", auth, onLogin);
    }

    private void storeAccount(Account acc) {
        this.acc = acc;

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("u_id", acc.id);
        editor.putString("name", acc.name);

        if (acc.key != null) {
            editor.putString("key", acc.key);
        }
        editor.commit();
    }

    private Account retrieveAccount() {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);;
        long userID = prefs.getLong("u_id", 0); // retrieve user id
        String key = prefs.getString("key", null);
        String name = prefs.getString("name", null);

        if (userID == 0 || key == null) {
            return new Account();
        } else {
            return new Account(userID, key, name);
        }
    }


    ///////////////////////////////////////////////////////////////////////
    ////////////////////////////// HANDLERS ///////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    private Session.MessageListener onRegister = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {

            try {

                int code = obj.get("code").getAsNumber().intValue();
                if (code != 200) {
                    Log.d("Register Error", "");
                    return;
                }

                long id = obj.get("data").getAsJsonObject().get("id").getAsNumber().longValue();
                String key = obj.get("data").getAsJsonObject().get("key").getAsString();

                acc.id = id;
                acc.key = key;
                Log.d("MainMenuDebug", "Account set to " + id + ":" + key);

                doLogin(id, key);
            } catch(NullPointerException e) {
                Log.d("Register Error", e.getMessage());
            }
        }
    };

    private Session.MessageListener onLogin = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {

            try {
                int code = obj.get("code").getAsNumber().intValue();
                if (code != 200) {
                    Log.d("MainMenuDebug - Login", obj.toString());
                    return;
                }

                if (acc == null) {
                    Log.d("Login Error", "Tried to log in but account was null");
                    return;
                }

                session.request("Manager.RetrieveAccountInfo", onAccountRetrieval);

            } catch(NullPointerException e) {
                Log.d("MainMenuDebug - Login", obj.toString());
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
                    acc = new Account();
                    session.request("Manager.RetrieveAccountInfo", onAccountRetrieval);
                } else if (!authenticated) {
                    doAuthenticate();
                }

            } catch(NullPointerException e) {
                Log.d("MainMenuDebug - auth", e.getMessage());

            }
        }
    };

    private Session.MessageListener onAccountRetrieval = new Session.MessageListener() {
        @Override
        public void call(JsonObject obj) {
            try {

                int code = obj.get("code").getAsNumber().intValue();
                if (code != 200) {
                    Log.d("Retrieval Error", "");
                    return;
                }

                JsonObject accObj = obj.get("data").getAsJsonObject();
                acc.id = accObj.get("id").getAsNumber().longValue();
                acc.name = accObj.get("name").getAsString();
                storeAccount(acc);
                accountLoaded = true;
                Log.d("Loaded", "Account successfully loaded as " + acc.name);


            } catch(NullPointerException e) {
                Log.d("MainMenuDebug - Retr", obj.toString());
            }

            // update settings to current account state here.
        }
    };

    ///////////////////////////////////////////////////////////////////////
    ////////////////////////////// CALLBACKS //////////////////////////////
    ///////////////////////////////////////////////////////////////////////

    private Session.CallbackListener connectFailedCallback = new Session.CallbackListener() {
        @Override
        public void call() {
            Log.d("connectionError","");
        }
    };


    private Session.CallbackListener handshakeCallback = new Session.CallbackListener() {
        @Override
        public void call() {
            doAuthenticate();
        }
    };
    private Session.CallbackListener disconnectCallback = new Session.CallbackListener() {
        @Override
        public void call() {
            accountLoaded = false;
            session.reconnect();
            Log.d("main menu", "Attempting to reconnect...");
        }
    };

    private Session.ExceptionListener fatalErrorCallback = new Session.ExceptionListener() {
        @Override
        public void call(Exception e) {
            Log.d("fatal error",e.getMessage());

        }
    };

    private Session.ExceptionListener errorCallback = new Session.ExceptionListener() {
        @Override
        public void call(Exception e) {
            Log.d("error",e.getMessage());
        }
    };

}
