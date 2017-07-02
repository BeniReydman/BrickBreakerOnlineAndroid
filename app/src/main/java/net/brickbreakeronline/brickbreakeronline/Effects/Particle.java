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
    private int changeDX;
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int size;
    private int initSize;
    private int life;
    private double firstLife;
    private int color;
    Vector2 position;
    Paint paint = new Paint();


    public Particle(Vector2 position, double dx, double dy, int size, int life, int color)
    {
        this.position = position;
        x = (int)position.getX();
        y = (int)position.getY();
        this.dx = dx;
        changeDX = 0;
        this.dy = dy;
        this.size = size;
        initSize = size;
        this.life = life;
        firstLife = (double)life;
        this.color = color;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.color);
    }

    public boolean update()
    {
        x += dx;
        y += dy;
        life--;
        changeDX++;
        if(changeDX % 10 == 0)
            lowerDX();
        if (life <= 0)
            return true;
        return false;
    }

    public void lowerDX()
    {
        if(dx > 1)
            dx--;
        if(dx < -1)
            dx++;
    }

    public void draw(Canvas canvas)
    {
        size = (int)((life / firstLife) * initSize);
        canvas.drawCircle((float)x , (float)y, size, paint);
    }
}
