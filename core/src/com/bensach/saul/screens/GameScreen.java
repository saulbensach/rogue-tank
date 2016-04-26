package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.bensach.saul.map.Level;
import com.bensach.saul.player.Player;

public class GameScreen implements Screen {

    private GameStart gameStart;
    private OrthographicCamera camera;
    private Level level;
    private Player player;

    public GameScreen(GameStart gameStart){
        this.gameStart = gameStart;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,0);
        camera.zoom += 6;
    }

    @Override
    public void show() {
        level = new Level();
        System.out.println(level.getPlayerStart());
        player = new Player(level.getPlayerStart(), level.getWalls());
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f,0.5f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update
        updateCamera();

        //render
        level.draw(camera);

        //Testing
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            level = new Level();
        }
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

    private void updateCamera(){
        camera.position.set(
                player.getPlayerPos().x,
                player.getPlayerPos().y,
                0
        );
        camera.update();
    }
}
