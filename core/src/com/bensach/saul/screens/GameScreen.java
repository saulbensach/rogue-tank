package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bensach.saul.bullets.BulletHandler;
import com.bensach.saul.enemies.EnemiesHandler;
import com.bensach.saul.enemies.Enemy;
import com.bensach.saul.map.Level;
import com.bensach.saul.player.Player;

public class GameScreen implements Screen {

    private GameStart gameStart;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Level level;
    private Player player;
    private EnemiesHandler enemiesHandler;
    private BulletHandler bulletHandler;

    public GameScreen(GameStart gameStart){
        this.gameStart = gameStart;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom -= 0.501f;
    }

    @Override
    public void show() {
        bulletHandler = new BulletHandler();
        level = new Level(bulletHandler);
        player = new Player(level.getPlayerStart(), level);
        enemiesHandler = new EnemiesHandler();
        enemiesHandler.addEnemy(new Enemy(new Vector2(player.getX() + 90, player.getY()),level));
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update
        bulletHandler.update(delta);
        player.update(delta);
        level.updateEnemies(enemiesHandler);
        enemiesHandler.updateEnemies(delta);
        updateCamera();

        //render
        batch.setProjectionMatrix(camera.combined);
        level.draw(camera);
        batch.begin();
        player.draw(batch);
        enemiesHandler.drawEnemies(batch);
        bulletHandler.draw(batch);
        batch.end();
        //Testing
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            level = new Level(bulletHandler);
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
                player.getX(),
                player.getY(),
                0
        );
        camera.update();
    }
}
