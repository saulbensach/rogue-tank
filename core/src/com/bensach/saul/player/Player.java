package com.bensach.saul.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.bensach.saul.map.Level;

import java.util.ArrayList;

public class Player extends Sprite implements InputProcessor {

    private Vector2 playerPos;
    private Body body;
    private Level level;
    private int health, bullets, maxBullets;
    private Sprite cannon;
    private float speed, rotVelocity, shootDelay;
    private boolean forward,backwards,rotLeft,rotRight, shoot, arrowRight, arrowLeft;

    public Player (Vector2 playerPos, Level level){
        super(new Texture("player/cuerpoTanque.png"));
        cannon = new Sprite(new Texture("player/cannon.png"));
        cannon.setFlip(true,false);
        cannon.setOrigin(cannon.getWidth() - 8,cannon.getHeight() / 2);
        speed = 150000 ;rotVelocity = 0.05f;shootDelay = 0f;health = 100;bullets = 10;maxBullets = 50;
        PlayerBuilder builder = new PlayerBuilder((int)playerPos.x, (int)playerPos.y, (int)getWidth() / 2, (int)getHeight() / 2, level.getWorld(), 0.0001f,0.4f,0.01f, this);
        body = builder.getBody();
        this.playerPos = playerPos;
        this.level = level;
        setPosition(playerPos.x , playerPos.y);
        cannon.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public void update(float delta){
        shootDelay -= delta;
        if(shootDelay < -1)shootDelay = -1;
        if(forward)level.updatePlayer(PlayerMovements.FORWARD, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(backwards)level.updatePlayer(PlayerMovements.BACKWARDS, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(rotLeft)level.updatePlayer(PlayerMovements.ROTATE_LEFT, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(rotRight)level.updatePlayer(PlayerMovements.ROTATE_RIGHT, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(arrowLeft)cannon.rotate(5);
        if(arrowRight)cannon.rotate(-5);
        if(!forward && !backwards)body.setLinearDamping(5);
        if(!rotLeft && !rotRight)body.setTransform(body.getPosition(), 0);
        if(shoot){
            if(shootDelay <= 0 && bullets > 0){
                level.updatePlayer(PlayerMovements.SHOOT, body, (float) Math.toRadians(cannon.getRotation()), speed, rotVelocity);
                shootDelay += 0.8;
                bullets--;
            }
        }
        rotate((float) Math.toDegrees(body.getAngle()));
        body.getTransform().setRotation(getRotation());
        setPosition(body.getPosition().x, body.getPosition().y);
    }

    public void dealDamage(int damage){
        health -= damage;
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition( x - getWidth() / 2, y - getHeight() / 2);
        cannon.setPosition((x - getWidth() / 2), (y - getHeight() / 2) + getHeight() / 2 - cannon.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        cannon.draw(batch);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.D: rotRight = true ;break;
            case Input.Keys.A: rotLeft = true ;break;
            case Input.Keys.W: forward = true ;break;
            case Input.Keys.S: backwards = true;break;
            case Input.Keys.SPACE: shoot = true;break;
            case Input.Keys.RIGHT: arrowRight = true;break;
            case Input.Keys.LEFT: arrowLeft = true;break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.D: rotRight = false ;break;
            case Input.Keys.A: rotLeft = false ;break;
            case Input.Keys.W: forward = false ;break;
            case Input.Keys.S: backwards = false ;break;
            case Input.Keys.SPACE: shoot = false;break;
            case Input.Keys.RIGHT: arrowRight = false;break;
            case Input.Keys.LEFT: arrowLeft = false;break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public int getBullets() {
        return bullets;
    }
}
