package net.brickbreakeronline.brickbreakeronline.Effects;

import android.graphics.Canvas;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

/**
 * Created by Beni on 2017-06-30.
 */

public class Particle {

    private GameManager gm;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int size;
    private int initSize;
    private int life;
    private double firstLife;
    private int color;
    Vector2 position;
    Paint paint = new Paint();


    public Particle(Vector2 position, int dx, int dy, int size, int life, int c)
    {
        this.position = position;
        x = (int)position.getX();
        y = (int)position.getY();
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        initSize = size;
        this.life = life;
        firstLife = (double)life;
        this.color = c;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public boolean update()
    {
        x += dx;
        y += dy;
        life--;
        if (life <= 0)
            return true;
        return false;
    }

    public void lowerDY()
    {
        if(dy > 0)
            dy--;
        if(dy < 0)
            dy++;
    }

    public void draw(Canvas canvas)
    {
        size = (int)((life / firstLife) * initSize);
        canvas.drawRect(x , y, (x + size), (y + size), paint);
    }
}
