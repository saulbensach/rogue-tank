package com.bensach.saul.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by saul- on 05/05/2016.
 */
public class Bullet extends Sprite {

    private Vector2 startPos;
    private Vector2 endPos;
    private Vector2 position;
    private Vector2 velocity;
    public boolean toRemove = false;
    private float speed = 50;
    private float angle = 0;
    float distance = 0f;

    public Bullet(Vector2 startPos, Vector2 endPos, float angle){
        super(new Texture("player/cannon.png"));
        rotate((float) Math.toDegrees(angle));
        this.angle = (float) Math.toDegrees(angle);
        this.startPos = startPos;
        position = startPos;
        this.endPos = endPos;
        velocity = new Vector2();
        velocity.set(endPos.x - position.x, endPos.y - position.y);
        velocity.nor();
        velocity.x *= speed;
        velocity.y *= speed;
        setPosition(position.x, position.y);
    }

    public void update(float delta){
        position.add(velocity);
        setPosition(position.x, position.y);
        distance = new Vector2(getX(), getY()).dst(endPos);
        if(distance > 0 && distance < 20)toRemove = true;
        if(distance > 3000)toRemove = true;
    }

    @Override
    public void draw(Batch batch){
        super.draw(batch);
    }

}
