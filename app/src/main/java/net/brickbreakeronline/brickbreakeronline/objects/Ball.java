package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.CircleCollidable;
import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.RectCollidable;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-15.
 */

public class Ball extends GameBody implements CircleCollidable {



    public Ball(GameManager gm, int identification) {
        super(gm, identification);
        //velocity = new Vector2(50,50);
        position = new Vector2(gm.getWidth()/2, gm.getHeight()/2);
    }


    @Override
    public void update(double delta) {
        super.update(delta);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(position.getXf(), position.getYf(), 21, paint);
    }

    @Override
    public boolean collidesWith(CircleCollidable c) {
        return false;
    }

    @Override
    public boolean collidesWith(RectCollidable c) {
        return false;
    }
}
