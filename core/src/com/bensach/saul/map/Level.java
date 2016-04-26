package com.bensach.saul.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bensach.saul.map.generator.LevelBuilder;

import java.util.ArrayList;

/**
 * Created by saul- on 18/04/2016.
 */
public class Level {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private LevelBuilder builder;

    public Level(){
        builder = new LevelBuilder(400,400,10,80,50,80,50);
        map = builder.getMap();
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void draw(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public Vector2 getPlayerStart(){
        return builder.getPlayerStart();
    }

    public ArrayList<Rectangle> getWalls(){
        return builder.getWalls();
    }

}
