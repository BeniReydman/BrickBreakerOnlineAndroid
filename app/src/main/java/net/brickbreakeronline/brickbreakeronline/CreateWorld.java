package net.brickbreakeronline.brickbreakeronline;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.objects.Ball;
import net.brickbreakeronline.brickbreakeronline.objects.Brick;

/**
 * Created by Beni on 2017-06-16.
 */

public class CreateWorld {

    public static void createSinglePlayer(GameManager gm)
    {
        double spacingX = gm.getWidth() * 0.01;
        double spacingY = gm.getHeight() * 0.01;
        Vector2 pos = new Vector2(spacingX, gm.getHeight() * 0.25);
        Vector2 size = new Vector2(gm.getWidth() * 0.1314, gm.getHeight() * 0.045555);


        int id = 1;
        for(int x = 0; x < 9; x++)
        {
            for(int y = 0; y < 7; y++) {
                //Log.d("NewBrick", pos.toString());
                gm.bodies.add(new Brick(gm, id++, size.clone(), pos.clone()));
                pos = pos.add(new Vector2(size.getX() + spacingX, 0));
            }
            pos = pos.add(new Vector2(0, size.getY() + spacingY));
            pos.setX(spacingX);
        }
        Ball a = new Ball(gm, id++);
        gm.bodies.add( a );

    }

    public static void createMultiplayerGame(GameManager gm, String worldInfo)
    {


    }
}
