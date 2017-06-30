package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Touch;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

/**
 * Created by Beni on 2017-06-15.
 */

public class Paddle extends GameBody {

    //Vector2 startTouch = new Vector2(0,0);

    double maxSpeed = 32;

    public Paddle(GameManager gameManager, int identification, Vector2 pos, Vector2 size) {
        super(gameManager, identification);
        setShapeWithoutPosition(new ShapeRect(pos, size));
    }


    @Override
    public void update(double delta) {
        super.update(delta);



        if (Touch.isTouch()) {
            double desiredPos = gm.screenToGameCoords(Touch.touchCoords).getX();
            double currentPos = ((ShapeRect)shape).getCenterPosition().getX();
            if (desiredPos < currentPos- maxSpeed) {
                getPosition().setX(currentPos - maxSpeed - getSize().getX()/2);
            } else if (desiredPos > currentPos + maxSpeed) {
                getPosition().setX(currentPos + maxSpeed - getSize().getX()/2);
            } else {
                getPosition().setX( desiredPos - getSize().getX() /2);
            }
        }
        if (getPosition().getX() < 0) {
            getPosition().setX(0);
        }
        if (getPosition().getX() + getSize().getX() > gm.getGameWidth()) {
            getPosition().setX(gm.getGameWidth() - getSize().getX());
        }
        /*
        if (Touch.isTouchDown()) {
            startTouch = gm.screenToGameCoords(Touch.touchCoords);
        }
        if (Touch.isTouch()) {
            /*
            double diff = startTouch.sub(gm.screenToGameCoords(Touch.touchCoords)).getX();
            Log.d("touch", String.valueOf(diff));
            double percentage = -diff/sensitivity;
            if (percentage > 1.0) {
                percentage = 1.0;
            }

            if (percentage < -1.0) {
                percentage = -1.0;
            }

            this.velocity.setX(percentage * maxSpeed);
        } else {
            this.velocity = new Vector2(0,0);
        }*/

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
        paint.setColor(Color.GREEN);
        //Log.d("SIZE", String.valueOf(getSize().getXf()) + " " + String.valueOf(getSize().getYf())  );
        canvas.drawRect(
                getDrawPosition().getXf(),
                getDrawPosition().getYf(),
                getDrawPosition().getXf() + gm.gameToScreenCoords(getSize()).getXf(),
                getDrawPosition().getYf() + gm.gameToScreenCoords(getSize()).getYf(),
                paint);




    }

    @Override
    public void onCollide(GameBody body, double delta) {
        super.onCollide(body, delta);
        //gm.removeBody(this);
    }
}
