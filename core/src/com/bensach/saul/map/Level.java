package com.bensach.saul.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.bensach.saul.map.generator.LevelBuilder;
import com.bensach.saul.player.PlayerMovements;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by saul- on 18/04/2016.
 */
public class Level {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private float timeStep;
    int velocityItearation, positionIteration;
    private Box2DDebugRenderer debugRenderer;
    private LevelBuilder builder;
    private Vector2 lookAt;
    private Vector2 startPos;
    private Vector2 endPos;
    private Vector2 normal;
    private Vector2 collisionPoint;
    private ShapeRenderer shapeRenderer;

    public Level(){
        timeStep = 1/60f;velocityItearation = 6; positionIteration = 2;
        builder = new LevelBuilder(400,400,25,80,50,80,50);
        lookAt = new Vector2(1.000f,1.000f);
        map = builder.getMap();
        world = builder.getWorld();
        debugRenderer = new Box2DDebugRenderer(true, false, true, true, false, false);
        renderer = new OrthogonalTiledMapRenderer(map);
        startPos = new Vector2();
        endPos = new Vector2();
        normal = new Vector2();
        collisionPoint = new Vector2();
        shapeRenderer = new ShapeRenderer();
    }

    public void updatePlayer(PlayerMovements movements, Body body, float rot, float speed, float rotVelocity){
        lookAt.x = (float) Math.cos(rot);
        lookAt.y = (float) Math.sin(rot);
        if(lookAt.len() > 0)lookAt.nor();
        if(lookAt.len2() > 0)lookAt.nor();
        startPos = new Vector2(body.getPosition().x, body.getPosition().y);
        float[] pt = {startPos.x * 0,startPos.y * 1};
        AffineTransform.getRotateInstance(rot, startPos.x, startPos.y).transform(pt,0,pt,0,1);
        endPos.x = pt[0];
        endPos.y = pt[1];
        switch (movements){
            case BACKWARDS:
                body.applyForce(lookAt.x * speed, lookAt.y * speed, body.getPosition().x, body.getPosition().y, true);
                break;
            case FORWARD:
                body.applyForce(-lookAt.x * speed, -lookAt.y * speed, body.getPosition().x, body.getPosition().y, true);
                break;
            case ROTATE_RIGHT:
                body.setTransform(body.getWorldCenter(), -rotVelocity);
                body.getPosition().rotate(-rotVelocity);
                break;
            case ROTATE_LEFT:
                body.setTransform(body.getWorldCenter(), rotVelocity);
                body.getPosition().rotate(rotVelocity);
                break;
            case SHOOT:
                final RayCastCallback callback = new RayCastCallback() {
                    @Override
                    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                        if(fixture.getBody().getUserData().equals("wall"))return -1;
                        Level.this.collisionPoint.set(point);
                        Level.this.normal.set(normal);
                        return 0;
                    }
                };
                world.rayCast(callback, startPos, endPos);
                break;
        }
    }

    public void draw(OrthographicCamera camera){
        world.step(timeStep,velocityItearation,positionIteration);
        renderer.setView(camera);
        renderer.render();
        debugRenderer.render(world, camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.line(startPos, collisionPoint);
        shapeRenderer.end();
    }

    public World getWorld() {
        return world;
    }

    public Vector2 getPlayerStart(){
        return builder.getPlayerStart();
    }

    public ArrayList<Rectangle> getWalls(){
        return builder.getWalls();
    }

}
