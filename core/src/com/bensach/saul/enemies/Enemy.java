package com.bensach.saul.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bensach.saul.map.Level;
import com.bensach.saul.player.Player;
import sun.invoke.empty.Empty;

import java.awt.geom.AffineTransform;

/**
 * Created by saul- on 03/05/2016.
 */
public class Enemy extends Sprite {

    private Body body;
    private Level level;
    private Sprite cannon;
    public Player player;
    private float currentTime;
    private int enemyVelocity, health;
    private String name;
    private BitmapFont font;

    public Enemy(Vector2 enemyPos, Level level, String name){
        super(new Texture("enemies/orangeTank.png"));
        font = new BitmapFont();
        cannon = new Sprite(new Texture("enemies/orangeCannon.png"));
        currentTime = 0;
        health = 100;
        this.name = name;
        enemyVelocity = 60;
        cannon.setFlip(true,false);
        cannon.setOrigin(cannon.getWidth() - 8,cannon.getHeight() / 2);
        this.level = level;
        setPosition(enemyPos.x, enemyPos.y);
        EnemyBuilder builder = new EnemyBuilder((int)enemyPos.x, (int)enemyPos.y, (int)getWidth() / 2, (int)getHeight() / 2, level.getWorld(), 0.0001f,0.8f,0.01f, this,name);
        body = builder.getBody();
    }

    public void update(float delta){
        currentTime -= delta;
        if(currentTime < -1)currentTime = -1;
        level.getWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().getBody().getUserData() instanceof Player){
                    if(contact.getFixtureB().getBody().getUserData() instanceof Enemy){
                        Enemy data = (Enemy) contact.getFixtureB().getBody().getUserData();
                        data.player = (Player) contact.getFixtureA().getBody().getUserData();
                        data.shoot();
                        data.follow();
                        Enemy.this.player = (Player) contact.getFixtureA().getBody().getUserData();
                    }

                }
            }

            @Override
            public void endContact(Contact contact) {
                if(contact.getFixtureA().getBody().getUserData() instanceof Player){
                    Enemy.this.player = null;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        if(player != null){
            follow();
            if(currentTime <= 0)
                shoot();
        }
        body.setLinearDamping(5);
        setPosition(body.getPosition().x, body.getPosition().y);
    }

    public void shoot(){
        //Bloquear a 1 vez por segundo
        Vector2 endPos = new Vector2();
        Vector2 startPosition = new Vector2(body.getPosition().x, body.getPosition().y);
        float[] pt = {startPosition.x * 0,startPosition.y * 1};
        AffineTransform.getRotateInstance(Math.toRadians(getRotation()), startPosition.x, startPosition.y).transform(pt,0,pt,0,1);
        endPos.x = pt[0];
        endPos.y = pt[1];
        level.enemyShoot(startPosition,endPos, getRotation());
        currentTime += 0.8f;
    }

    public void follow(){
        setRotation ((float) ((Math.atan2 (player.getX() - getX(), -(player.getY() - getY()))*180.0d/Math.PI)+90.0f));
        cannon.setRotation(getRotation());
        Vector2 direc = new Vector2(player.getX(), player.getY());
        direc.sub(getX(), getY());
        direc.nor();
        body.setLinearVelocity(direc.scl(enemyVelocity));
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        cannon.draw(batch);
        font.draw(batch, ""+getHealth(),getX() + getWidth() / 2 - 10, getY());
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition( x - getWidth() / 2, y - getHeight() / 2);
        cannon.setPosition((x - getWidth() / 2), (y - getHeight() / 2) + getHeight() / 2 - cannon.getHeight() / 2);
    }

    public void delete(){
        level.getWorld().destroyBody(body);
    }

    @Override
    public boolean equals(Object object){
        if(object == null)return false;
        if(object == this)return true;
        if(object instanceof Enemy){
            Enemy enemy = (Enemy) object;
            if(enemy.name.equals(name))
                return true;
        }
        return false;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
