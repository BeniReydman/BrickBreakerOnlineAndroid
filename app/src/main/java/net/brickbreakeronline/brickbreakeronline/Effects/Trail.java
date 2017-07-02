package net.brickbreakeronline.brickbreakeronline.Effects;

import android.graphics.Canvas;
import android.graphics.Paint;

import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

import java.util.ArrayList;

/**
 * Created by Beni on 2017-07-01.
 */

public class Trail {

    private ArrayList<Vector2> ballCoords = new ArrayList<Vector2>();
    private Vector2 vect;
    private float size;
    private Paint paint;
    private int arraySize;
    private int alpha;

    public Trail(Vector2 coords, float size)
    {
        ballCoords.add(coords);
        this.size = size;
    }

    public void update(float x, float y)
    {
        vect = new Vector2(x, y);
        ballCoords.add(vect);
        if(ballCoords.size() > 40)
            ballCoords.remove(0);

    }

    public void draw(Canvas canvas, Paint p)
    {
        paint = new Paint(p);
        arraySize = ballCoords.size();
        for(int i = 0; i < arraySize;i++){
                alpha = (int)(155 - (40 / (i + 1)) * 6.375);
                if(alpha < 0)
                    alpha = 0;
                paint.setAlpha(alpha);
                vect = ballCoords.get(i);
                float tempSize = size * ((float)(i+1) / (float)arraySize);
                canvas.drawCircle(vect.getXf(), vect.getYf(), tempSize, paint);
        }
    }

}
