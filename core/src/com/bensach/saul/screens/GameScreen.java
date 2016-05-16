package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private OrthographicCamera uiCamera;
    private BitmapFont bitmapFont;
    private SpriteBatch batch;
    private Level level;
    private Player player;
    private EnemiesHandler enemiesHandler;
    private BulletHandler bulletHandler;
    private float counter, timeCounter;

    public GameScreen(GameStart gameStart){
        this.gameStart = gameStart;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom -= 0.301f;
    }

    @Override
    public void show() {
        bulletHandler = new BulletHandler();
        counter = 0f;timeCounter = 0f;
        enemiesHandler = new EnemiesHandler();
        bitmapFont = new BitmapFont();
        level = new Level(bulletHandler);
        int num = 0;
        for(Vector2 position : level.getEnemiesPositions()){
            enemiesHandler.addEnemy(new Enemy(position, level, Integer.toString(num)));
            num++;
        }
        player = new Player(level.getPlayerStart(), level);
        level.setPlayer(player);
        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render(float delta) {
        timeCounter += delta;
        if(timeCounter >= 1.0f){
            timeCounter = 0f;
            counter++;
        }
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update
        bulletHandler.update(delta);
        player.update(delta);
        level.updateEnemies(enemiesHandler);
        enemiesHandler.updateEnemies(delta);
        updateCamera();

        if(player.getHealth() < 0){
            gameStart.setScreen(gameStart.mainMenu);
        }

        //render
        batch.setProjectionMatrix(camera.combined);
        level.draw(camera);
        batch.begin();
        player.draw(batch);
        enemiesHandler.drawEnemies(batch);
        bulletHandler.draw(batch);
        batch.end();
        drawUI();
        //Testing
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
           //level = new Level(bulletHandler);
        }
    }

    private void drawUI(){
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        bitmapFont.draw(batch, "Tiempo: "+counter, -Gdx.graphics.getWidth() / 2 + 10, (-Gdx.graphics.getHeight() / 2)+30);
        bitmapFont.draw(batch, "Enemigos Restantes: "+enemiesHandler.getEnemies().size(), -Gdx.graphics.getWidth() / 2 + 120, (-Gdx.graphics.getHeight() / 2)+30);
        bitmapFont.draw(batch, ""+player.getHealth(), -10,0);
        bitmapFont.draw(batch, ""+player.getBullets()+"/"+player.getMaxBullets(),20, 0);
        batch.end();
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
        uiCamera.update();
        camera.position.set(
                player.getX(),
                player.getY(),
                0
        );
        camera.update();
    }
}
