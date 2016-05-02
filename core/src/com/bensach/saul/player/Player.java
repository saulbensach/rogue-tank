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
    private float speed, rotVelocity;
    private boolean forward,backwards,rotLeft,rotRight, shoot;

    public Player (Vector2 playerPos, Level level){
        super(new Texture("player/cuerpoTanque.png"));
        speed = 5000 ;rotVelocity = 0.05f;
        PlayerBuilder builder = new PlayerBuilder((int)playerPos.x, (int)playerPos.y, (int)getWidth() / 2, (int)getHeight() / 2, level.getWorld(), 0.0001f,0.4f,0.01f);
        body = builder.getBody();
        this.playerPos = playerPos;
        this.level = level;
        setPosition(playerPos.x , playerPos.y);
        System.out.println(playerPos);
    }

    public void update(float delta){
        if(forward)level.updatePlayer(PlayerMovements.FORWARD, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(backwards)level.updatePlayer(PlayerMovements.BACKWARDS, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(rotLeft)level.updatePlayer(PlayerMovements.ROTATE_LEFT, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(rotRight)level.updatePlayer(PlayerMovements.ROTATE_RIGHT, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        if(!forward && !backwards)body.setLinearDamping(5);
        if(!rotLeft && !rotRight)body.setTransform(body.getPosition(), 0);
        if(shoot)level.updatePlayer(PlayerMovements.SHOOT, body, (float) Math.toRadians(getRotation()), speed, rotVelocity);
        rotate((float) Math.toDegrees(body.getAngle()));
        body.getTransform().setRotation(getRotation());
        setPosition(body.getPosition().x, body.getPosition().y);
    }

    @Override
    public void setPosition(float x, float y){ super.setPosition( x - getWidth() / 2, y - getHeight() / 2); }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.D: rotRight = true ;break;
            case Input.Keys.A: rotLeft = true ;break;
            case Input.Keys.W: forward = true ;break;
            case Input.Keys.S: backwards = true;break;
            case Input.Keys.SPACE: shoot = true;break;
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
}
