package com.bensach.saul.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bensach.saul.map.generator.LevelBuilder;

/**
 * Created by saul- on 18/04/2016.
 */
public class Level {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public Level(){
        LevelBuilder builder = new LevelBuilder(95,40,10,10,5,10,5);
        map = builder.getMap();
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void draw(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

}
