package com.bensach.saul.enemies;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

/**
 * Created by saul- on 03/05/2016.
 */
public class EnemiesHandler {

    private ArrayList<Enemy> enemies;

    public EnemiesHandler(){
        enemies = new ArrayList<Enemy>();
    }

    public void updateEnemies(float delta){
        for(Enemy enemy : enemies){
            enemy.update(delta);
        }
    }

    public void dealDamage(Enemy enemy){

    }

    public void drawEnemies(Batch batch){
        for(Enemy enemy : enemies){
            enemy.draw(batch);
        }
    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy){
        enemy.delete();
        enemies.remove(enemy);
    }

}
