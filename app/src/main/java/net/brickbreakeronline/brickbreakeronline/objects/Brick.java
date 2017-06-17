package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

/**
 * Created by Beni on 2017-06-16.
 */

public class Brick extends GameBody {

    public Brick(GameManager gameManager, int identification, Vector2 size, Vector2 pos) {
        super(gameManager, identification);
        setShapeWithoutPosition(new ShapeRect(pos, size));
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    public Vector2 getSize()
    {
        return ((ShapeRect)shape).getSize();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        //Log.d("SIZE", String.valueOf(getSize().getXf()) + " " + String.valueOf(getSize().getYf())  );
        canvas.drawRect(
                getPosition().getXf(),
                getPosition().getYf(),
                getPosition().getXf() + getSize().getXf(),
                getPosition().getYf() + getSize().getYf(),
                paint);

    }
}
