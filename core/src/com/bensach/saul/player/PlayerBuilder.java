package com.bensach.saul.player;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by saul- on 02/05/2016.
 */
public class PlayerBuilder {

    private Body body;

    public PlayerBuilder(int x, int y, int width, int height, World world, float density, float friction, float restitution){
        body = createBody(x,y,width,height,world,density,friction,restitution);
    }

    private Body createBody(int x, int y, int width, int height, World world, float density, float friction, float restitution){
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
        body.setUserData("player");
        polygonShape.dispose();
        return body;
    }

    public Body getBody() {
        return body;
    }
}
