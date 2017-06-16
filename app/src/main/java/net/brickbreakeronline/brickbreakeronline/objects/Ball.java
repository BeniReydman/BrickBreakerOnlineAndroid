package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;

/**
 * Created by Beni on 2017-06-15.
 */

public class Ball extends GameBody {


    private int x;
    private int y;

    public Ball(GameManager gameManager, int identification) {
        super(gameManager, identification);
        x = gameManager.getWidth();
        y = gameManager.getHeight();
    }


    @Override
    public void update() {
        super.update();
        x--;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(x, y / 2, 21, paint);
    }
}
