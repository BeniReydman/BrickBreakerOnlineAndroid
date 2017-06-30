package net.brickbreakeronline.brickbreakeronline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    public static final String host = "192.168.0.182";
    public static final int port = 3250;

    private SessionHolder s;

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
                Intent intent = new Intent(getBaseContext(), Game.class);
                startActivity(intent);
            }
        });
        s = new SessionHolder(host ,port);
    }

}
