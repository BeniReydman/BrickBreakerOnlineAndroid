package net.brickbreakeronline.brickbreakeronline;

import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;
import net.brickbreakeronline.brickbreakeronline.objects.Ball;
import net.brickbreakeronline.brickbreakeronline.objects.Brick;
import net.brickbreakeronline.brickbreakeronline.objects.BrickHolder;
import net.brickbreakeronline.brickbreakeronline.objects.Paddle;

/**
 * Created by Beni on 2017-06-16.
 */

public class CreateWorld {

    public static void createSinglePlayer(GameManager gm)
    {
        int id = 1;
        BrickHolder holder = new BrickHolder(gm, id++);
        gm.bodies.add( holder );

        int numOfBricksX = 6;
        int numOfBricksY = 9;
        boolean switchPattern = false;
        boolean switchHit = true;

        double spacingX = gm.getGameWidth() * 0.025;
        double spacingY = spacingX;
        double gameHeight = gm.getGameHeight() * 0.25;

        Vector2 pos = new Vector2(spacingX, gameHeight);
        Vector2 size = new Vector2((gm.getGameWidth() - (numOfBricksX + 1) * spacingX) / numOfBricksX,
                ((gm.getGameHeight() - (gameHeight * 2)) - (numOfBricksY - 1) * spacingY) / numOfBricksY);

        for(int y = 0; y < numOfBricksY; y++)
        {
            for(int x = 0; x < numOfBricksX; x++) {
                if(switchPattern) {
                    x++;
                    pos.setX(spacingX + size.getX()/2);
                    switchPattern = !switchPattern;
                    switchHit = false;
                }
                holder.addBrick(new Brick(gm, id++, size.clone(), pos.clone()));
                pos = pos.add(new Vector2(size.getX() + spacingX, 0));
            }
            pos = pos.add(new Vector2(0, size.getY() + spacingY));
            pos.setX(spacingX);
            if(switchHit)
            switchPattern = !switchPattern;
            if(!switchPattern && !switchPattern) {
                switchHit = !switchHit;
            }
        }
        holder.resize();

        Ball ball = new Ball(gm, id++);
        ball.setPosition(new Vector2(450, 200));
        gm.bodies.add( ball );


        Ball ball2 = new Ball(gm, id++);
        ball2.setPosition(new Vector2(450, 1400));
        gm.bodies.add( ball2 );

        Vector2 paddleSize = new Vector2(160, 40);
        Paddle paddle = new Paddle(gm, id++,
                new Vector2(450-paddleSize.getX()/2, 1600-paddleSize.getY()-60),
                paddleSize.clone());
        gm.bodies.add( paddle );


        Paddle paddle2 = new Paddle(gm, id++,
                new Vector2(450-paddleSize.getX()/2, 60),
                paddleSize.clone());
        gm.bodies.add( paddle2 );

    }

    public static void createMultiplayerGame(GameManager gm, String worldInfo)
    {


    }
}
