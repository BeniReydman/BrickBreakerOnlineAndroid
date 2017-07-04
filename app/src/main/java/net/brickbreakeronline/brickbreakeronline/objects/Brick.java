package net.brickbreakeronline.brickbreakeronline.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.gson.JsonObject;

import net.brickbreakeronline.brickbreakeronline.Effects.Explosion;
import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.framework.shapes.ShapeRect;

/**
 * Created by Beni on 2017-06-16.
 */

public class Brick extends GameBody {

    private float sizeDecreaser;
    private GameBody tempGB;
    public BrickHolder holder;
    private int hitCount = 0;
    private boolean hit = false;
    private float health = 100;
    private Paint paint;
    private Paint paintDark;

    int[] colorSpectrum = {Color.RED, Color.YELLOW, Color.WHITE};
    int color = colorSpectrum[colorSpectrum.length-1];

    public Brick(GameManager gameManager, int identification, JsonObject obj) throws NullPointerException {
        super(gameManager, identification);

        JsonObject box = obj.get("box").getAsJsonObject();
        JsonObject pos = box.get("pos").getAsJsonObject();
        double x = pos.get("x").getAsDouble();
        double y = pos.get("y").getAsDouble();
        double width = box.get("w").getAsDouble();
        double height = box.get("h").getAsDouble();

        setShapeWithoutPosition(new ShapeRect(new Vector2(x,y), new Vector2(width, height)));
        paint = new Paint();
        paintDark = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paintDark.setStyle(Paint.Style.FILL);
        sizeDecreaser = (float) (gm.gameToScreenCoords(getSize()).getYf() * 0.1);
    }

    public Brick(GameManager gameManager, int identification, Vector2 size, Vector2 pos) {
        super(gameManager, identification);
        setShapeWithoutPosition(new ShapeRect(pos, size));
        paint = new Paint();
        paintDark = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paintDark.setStyle(Paint.Style.FILL);
        sizeDecreaser = (float) (gm.gameToScreenCoords(getSize()).getYf() * 0.1);
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

        paint.setColor(color);
        paintDark.setColor(darker(color, (float)0.8));
        //Log.d("SIZE", String.valueOf(getSize().getXf()) + " " + String.valueOf(getSize().getYf())  );
        canvas.drawRect(
                getDrawPosition().getXf(),
                getDrawPosition().getYf(),
                getDrawPosition().getXf() + gm.gameToScreenCoords(getSize()).getXf(),
                getDrawPosition().getYf() + gm.gameToScreenCoords(getSize()).getYf(),
                paintDark);

        canvas.drawRect(
                getDrawPosition().getXf(),
                getDrawPosition().getYf(),
                getDrawPosition().getXf() + (gm.gameToScreenCoords(getSize()).getXf() - sizeDecreaser),
                getDrawPosition().getYf() + (gm.gameToScreenCoords(getSize()).getYf() - sizeDecreaser),
                paint);
        if(hitCount == 0)
            hit = false;
        else
            hitCount = 0;
    }

    public static int darker (int color, float factor) {
        int a = Color.alpha( color );
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

    public void doDamage(float damage)
    {
        if(!hit) {
            health -= damage;
            checkColor();
            if (health <= 0) {
                tempGB = (new Explosion(gm, -1, this.getPosition(), this.getSize()));
                gm.addBody(tempGB);
                ((Explosion) tempGB).explodeRandom();
                gm.removeBody(this);

                if (holder != null) {
                    holder.removeBrick(this);
                }
            }
            hit = true;
            hitCount = 1;
        }
    }

    private void checkColor()
    {
        color = colorSpectrum[(int)(health/100 * (colorSpectrum.length-1))];
    }
}
