package net.brickbreakeronline.brickbreakeronline.Effects;

import android.graphics.Canvas;
import android.graphics.Color;

import net.brickbreakeronline.brickbreakeronline.framework.GameBody;
import net.brickbreakeronline.brickbreakeronline.framework.GameManager;
import net.brickbreakeronline.brickbreakeronline.framework.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Beni on 2017-06-30.
 */

public class Explosion extends GameBody{

    private ArrayList<Particle> particles = new ArrayList<Particle>();
    private int particleAmount;
    private int dx;
    private int dy;
    private int size;
    private int life;
    private int color;
    public boolean created = false;

    public Explosion(GameManager gameManager, int identification, Vector2 coords)
    {
        super(gameManager, identification);
        this.setPosition(coords);
    }


    public void explodeRandom()
    {
        particleAmount = (int) (Math.random()*50)+50;
        randomMaker();
    }
    public void randomMaker()
    {
        for(int x = 0; x < particleAmount; x++)
        {
            color = randomColor();
            size = (int) (Math.random() * 40);
            life = (int) Math.random() * (60) + 100;
            dx = (int) (Math.random() * 5);
            dy = (int) (Math.random() * 5);
            addParticle();
        }
    }

    public void explode(int dx, int dy, int size, int life, int color, int particleAmount)
    {
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.life = life;
        this.color = color;
        this.particleAmount = particleAmount;

        for(int x = 0; x < particleAmount; x++)
        {
            addParticle();
        }
        created = true;
    }

    public int randomColor()
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;


    }
    public void addParticle(){
        if(Math.random() < 0.5)
            dx = -dx;
        particles.add(new Particle(this.getDrawPosition(),dx,dy,size,life,color));
    }

    public void update(double delta)
    {
        for(int i = 0; i < particles.size();i++){
            if(particles.get(i).update())
                particles.remove(i);
        }
        if(particles.size() == 0)
        {
            gm.removeBody(this);
        }
    }

    public void draw(Canvas canvas)
    {
        for(int i = 0; i < particles.size();i++){
            particles.get(i).draw(canvas);
        }
    }



}
