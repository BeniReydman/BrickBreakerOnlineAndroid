package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeCircle;

/**
 * Created by Beni on 2017-06-15.
 */

public class Ball extends GameBody {



    public Ball(GameManager gm, int identification) {
        super(gm, identification);
        velocity = new Vector2(100,100);
        Vector2 pos = new Vector2(gm.getWidth()/2, gm.getHeight()/2);
        setShape(new ShapeCircle(pos, 21));
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
        canvas.drawCircle(getPosition().getXf(), getPosition().getYf(), 21, paint);
    }

    @Override
    public void onCollide(GameBody body) {
        super.onCollide(body);
        gm.removeBody(body);
    }
}