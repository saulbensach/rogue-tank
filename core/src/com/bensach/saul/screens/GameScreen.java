package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bensach.saul.bullets.BulletHandler;
import com.bensach.saul.enemies.EnemiesHandler;
import com.bensach.saul.enemies.Enemy;
import com.bensach.saul.map.Level;
import com.bensach.saul.player.Player;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

public class GameScreen implements Screen {

    private GameStart gameStart;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private Stage stage;
    private BitmapFont bitmapFont;
    private SpriteBatch batch;
    private Level level;
    private Player player;
    private EnemiesHandler enemiesHandler;
    private BulletHandler bulletHandler;
    private float counter, timeCounter;
    private float timerDeath;
    private boolean muerto = false, winner = false, pause = false;
    private Texture mensajeMuerte = new Texture("gui/mensajeMuerte.png");
    private Texture mensajeVictoria = new Texture("gui/mensajeVictoria.png");

    public GameScreen(GameStart gameStart){
        this.gameStart = gameStart;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom -= 0.301f;
    }

    @Override
    public void show() {
        stage = new Stage();
        pause = false;
        muerto = false;
        createStage();
        bulletHandler = new BulletHandler();
        counter = 0f;timeCounter = 0f;timerDeath = 0f;
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
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //update
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pause = !pause;
        if(!pause){
            if(!muerto){
                timeCounter += delta;
                if(timeCounter >= 1.0f){
                    timeCounter = 0f;
                    counter++;
                }
                bulletHandler.update(delta);
                player.update(delta);
                level.updateEnemies(enemiesHandler);
                enemiesHandler.updateEnemies(delta);
            }
        }
        updateCamera();

        //render
        batch.setProjectionMatrix(camera.combined);
        level.draw(camera);
        batch.begin();
        player.draw(batch);
        enemiesHandler.drawEnemies(batch);
        bulletHandler.draw(batch);
        batch.end();
        victoria();
        mensajeMuerte();
        drawUI();
        if(pause){
            Gdx.input.setInputProcessor(stage);
            menuPausa();
        }else{
            Gdx.input.setInputProcessor(player);
        }
    }

    private void menuPausa(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void drawUI(){
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        bitmapFont.draw(batch, "Tiempo: "+counter, -Gdx.graphics.getWidth() / 2 + 10, (-Gdx.graphics.getHeight() / 2)+30);
        bitmapFont.draw(batch, "Enemigos Restantes: "+enemiesHandler.getEnemies().size(), -Gdx.graphics.getWidth() / 2 + 120, (-Gdx.graphics.getHeight() / 2)+30);
        bitmapFont.draw(batch, ""+player.getHealth(), -10,0);
        bitmapFont.draw(batch, ""+player.getBullets()+"/"+player.getMaxBullets(),20, 0);
        if(player.getHealth() <= 0)
            batch.draw(mensajeMuerte,0 - mensajeMuerte.getWidth() / 2,0);
        if(winner){
            batch.draw(mensajeVictoria,0 - mensajeVictoria.getWidth() / 2, 0);
        }
        batch.end();
    }

    private void createStage(){
        TextureAtlas textureAtlas = new TextureAtlas("gui/gui.pack");
        Skin skin = new Skin();
        skin.addRegions(textureAtlas);
        TextButton continueButton = new TextButton("Continuar",new TextButton.TextButtonStyle(skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),new BitmapFont()));
        TextButton exitButton = new TextButton("Salir",new TextButton.TextButtonStyle(skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),new BitmapFont()));

        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause = false;
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                gameStart.setScreen(gameStart.mainMenu);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(continueButton).width(200).pad(5);
        table.row();
        table.add(exitButton).width(200).pad(5);
        stage.addActor(table);
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

    private void victoria(){
        if(enemiesHandler.getEnemies().size() <= 0){
            timerDeath += Gdx.graphics.getDeltaTime();
            winner = true;
        }
        if(timerDeath % 60 >= 3f){
            gameStart.setScreen(gameStart.mainMenu);
            sendData();
        }
    }

    private void mensajeMuerte(){
        if(player.getHealth() <= 0){
            timerDeath += Gdx.graphics.getDeltaTime();
            muerto = true;
        }
        if(timerDeath % 60 >= 3f){
            gameStart.setScreen(gameStart.mainMenu);
        }
    }

    private void sendData(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tankmaster", "root", "root");
            String query = "INSERT INTO users VALUES(?,?)";
            PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query);
            statement.setInt(2, (int) counter);
            statement.setInt(3, player.getHealth());
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
