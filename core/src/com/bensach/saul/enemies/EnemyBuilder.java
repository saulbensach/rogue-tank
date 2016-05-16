package com.bensach.saul.enemies;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by saul- on 03/05/2016.
 */
public class EnemyBuilder {

    private Body body;
    public String name;

    public EnemyBuilder(int x, int y, int width, int height, World world, float density, float friction, float restitution, Enemy enemy, String name){
        body = createBody(x,y,width,height,world,density,friction,restitution, enemy);
        this.name = name;
    }

    private Body createBody(int x, int y, int width, int height, World world, float density, float friction, float restitution, Enemy enemy){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        body.createFixture(fixtureDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(300f);
        FixtureDef sensor = new FixtureDef();
        sensor.shape = circleShape;
        sensor.isSensor = true;
        body.createFixture(sensor);
        body.setUserData(enemy);
        polygonShape.dispose();
        return body;
    }

    public Body getBody() {
        return body;
    }
}
