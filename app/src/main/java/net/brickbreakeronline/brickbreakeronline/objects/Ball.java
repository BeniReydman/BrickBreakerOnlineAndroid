package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeCircle;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

/**
 * Created by Beni on 2017-06-15.
 */

public class Ball extends GameBody {

    //double mHeight;
    double speed = 0.25;
    double angleAddtion = 45*Math.PI/180;
    double angleMax = Math.PI*3/24;
    float radius = 21;

    public Ball(GameManager gm, int identification) {
        super(gm, identification);
        velocity = new Vector2(1, 1);
        setSpeed(speed);
        Vector2 pos = new Vector2(gm.getGameWidth()/2, gm.getGameHeight()/2);
        setShape(new ShapeCircle(pos, radius));
        //mHeight = Math.sin(45) * 21.0;

    }

    @Override
    public void update(double delta) {
        super.update(delta);

        if (getPosition().getX() + ((ShapeCircle)shape).radius >= gm.getGameWidth()) {
            setVelocity(velocity.goLeft());
        }
        if (getPosition().getX() < 0) {
            setVelocity(velocity.goRight());
        }
        if (getPosition().getY() + ((ShapeCircle)shape).radius >= gm.getGameHeight()) {
            setVelocity(velocity.goUp());
        }
        if (getPosition().getY() < 0) {
            setVelocity(velocity.goDown());
        }

        /*Log.d("velocity", velocity.multiply(delta).toString());
        Log.d("delta", String.valueOf(delta));*/
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(
                getDrawPosition().getXf(),
                getDrawPosition().getYf(),
                gm.getScreenRatio().getYf()*radius,
                paint);
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
        setVelocity(velocity.normalize().multiply(speed));
    }

    public void increaseSpeed(double speed)
    {
        setSpeed(this.speed + speed);
    }

    @Override
    public void onCollide(GameBody body, double delta) {
        super.onCollide(body, delta);
        Vector2 collisionVelocity = velocity.clone();
        ShapeRect paddleShape = (ShapeRect)body.getShape();
        Vector2 center = getPosition();
        if (body.getClass() == Brick.class || body.getClass() == Paddle.class) {
            increaseSpeed(0.005);


            double ballVelX = velocity.getX();
            double ballVelY = velocity.getY();
            double centerY = center.getY() - (ballVelY * delta);
            double centerX = center.getX() - (ballVelX * delta);
            double top    = paddleShape.getPosition().getY();
            double right  = paddleShape.getPosition().getX() + paddleShape.getSize().getX();
            double bottom = paddleShape.getPosition().getY() + paddleShape.getSize().getY();
            double left   = paddleShape.getPosition().getX();

            /*
            if (centerY < top) {
                setVelocity(velocity.goUp());
            } else if (centerY > bottom) {
                setVelocity(velocity.goDown());
            } else if (centerX < left) {
                setVelocity(velocity.goLeft());
            } else if (centerX > right) {
                setVelocity(velocity.goRight());
            }
            */

            if(ballVelX >= 0 && ballVelY >= 0) // Ball is going Bottom Right
            {
                if (top - centerY > left - centerX) // Ball hit Top
                    setVelocity(velocity.goUp());
                else // Ball hit Left
                    setVelocity(velocity.goLeft());
            }
            else if (ballVelX < 0 && ballVelY >=0) // Ball is going Bottom Left
            {
                if (top - centerY > centerX - right) // Ball hit Top
                    setVelocity(velocity.goUp());
                else // Ball hit Right
                    setVelocity(velocity.goRight());
            }
            else if (ballVelX >= 0 && ballVelY < 0) // Ball is going Top Right
            {
                if (centerY - bottom > left - centerX) // Ball hit Bottom
                    setVelocity(velocity.goDown());
                else // Ball hit Left
                    setVelocity(velocity.goLeft());
            }
            else // Ball is going Top Left
            {
                if (centerY - bottom > centerX - right) // Ball hit Bottom
                    setVelocity(velocity.goDown());
                else // Ball hit Right
                    setVelocity(velocity.goRight());
            }
        }

        if (body.getClass() == Brick.class) {
            ((Brick)body).doDamage(34);
        }

        if (body.getClass() == Paddle.class) {
            Vector2 paddleCenter = paddleShape.getCenterPosition();

            double diff = paddleCenter.getX() - center.getX();
            double width = paddleShape.getSize().getX();
            double centerPercentage = diff / width * 2;

            double angle = velocity.angle() - Math.PI/2 + centerPercentage * angleAddtion;
            if (center.getY() < paddleCenter.getY()) { // top
                angle = angle > -Math.PI*1/2 + angleMax ? -Math.PI*1/2 + angleMax : angle;
                angle = angle < -Math.PI*1/2 - angleMax ? -Math.PI*1/2 - angleMax : angle;
            } else { // bottom
                angle = angle > Math.PI*1/2 + angleMax ? Math.PI*1/2 + angleMax : angle;
                angle = angle < Math.PI*1/2 - angleMax ? Math.PI*1/2 - angleMax : angle;
            }

            setVelocity((new Vector2(angle)).multiply(speed));
        }
    }
}