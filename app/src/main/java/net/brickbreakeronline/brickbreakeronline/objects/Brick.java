package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.Effects.Explosion;
import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

/**
 * Created by Beni on 2017-06-16.
 */

public class Brick extends GameBody {

    private GameBody tempGB;
    public BrickHolder holder;
    private float health = 100;

    int[] colorSpectrum = {Color.RED, Color.YELLOW, Color.WHITE};
    int color = colorSpectrum[colorSpectrum.length-1];

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
    public void destroy() {
        super.destroy();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        //Log.d("SIZE", String.valueOf(getSize().getXf()) + " " + String.valueOf(getSize().getYf())  );
        canvas.drawRect(
                getDrawPosition().getXf(),
                getDrawPosition().getYf(),
                getDrawPosition().getXf() + gm.gameToScreenCoords(getSize()).getXf(),
                getDrawPosition().getYf() + gm.gameToScreenCoords(getSize()).getYf(),
                paint);

    }

    public void doDamage(float damage)
    {
        health -= damage;
        checkColor();
        if (health <= 0) {
            tempGB = (new Explosion(gm, -1, this.getPosition()));
            gm.addBody(tempGB);
            ((Explosion)tempGB).explodeRandom();
            gm.removeBody(this);

            if (holder != null) {
                holder.removeBrick(this);
            }
        }
    }

    private void checkColor()
    {
        color = colorSpectrum[(int)(health/100 * (colorSpectrum.length-1))];
    }
}
