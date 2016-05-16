package com.bensach.saul.bullets;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

/**
 * Created by saul- on 05/05/2016.
 */
public class BulletHandler {

    private ArrayList<Bullet> bullets;

    public BulletHandler(){
        bullets = new ArrayList<Bullet>();
    }

    public void update(float delta){
        for(Bullet bullet : bullets){
            bullet.update(delta);
        }
        for(int i = 0; i < bullets.size(); i++){
            if(bullets.get(i).toRemove)bullets.remove(i);
        }
    }

    public void draw(Batch batch){
        for(Bullet bullet : bullets){
            bullet.draw(batch);
        }
    }

    public void add(Bullet bullet){
        bullets.add(bullet);
    }
}
