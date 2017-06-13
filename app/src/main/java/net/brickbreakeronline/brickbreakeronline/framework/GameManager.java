package net.brickbreakeronline.brickbreakeronline.framework;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.GameSurfaceView;

/**
 * Created by Beni on 2017-06-13.
 */

public class GameManager {

    static int x;
    final int width;
    final int height;

    public GameManager(GameSurfaceView view)
    {
        width = view.getWidth();
        height = view.getHeight();
        x = width;
    }

    public void create(int width, int height)
    {

    }

    public void update(double delta)
    {


    }

    public void draw(Canvas canvas)
    {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(x, canvas.getHeight() / 2, 100, paint);
        x++;
    }

    public void networkUpdate()
    {

    }

}
