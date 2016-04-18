package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bensach.saul.map.Level;
import com.bensach.saul.player.Player;

/**
 * Created by saul- on 06/04/2016.
 */
public class GameScreen implements Screen {

    private GameStart gameStart;
    private OrthographicCamera camera;
    private Level level;
    private Player player;

    public GameScreen(GameStart gameStart){
        this.gameStart = gameStart;
        player = new Player();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,0);
        camera.zoom += 10;
    }

    @Override
    public void show() {
        level = new Level();
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f,0.5f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update
        camera.update();

        //render
        level.draw(camera);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
