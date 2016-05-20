package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by saul- on 06/04/2016.
 */
public class MainMenu implements Screen {

    private GameStart gameStart;
    private Stage stage;
    private Texture cesped;
    private Texture contenedor;
    private SpriteBatch batch;
    private float lastX = 0, lastY = 0;

    public MainMenu(GameStart gameStart) {
        this.gameStart = gameStart;
        stage = new Stage();
        createUI();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        contenedor = new Texture("gui/contenedorPeque.png");
        cesped = new Texture("gui/dirt.png");
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        batch.begin();
        actualizarFondo();
        for(int y = (int) (lastY - cesped.getHeight()); y < Gdx.graphics.getHeight(); y += cesped.getHeight()){
            for(int x = (int) lastX; x < Gdx.graphics.getWidth(); x += cesped.getWidth()){
                batch.draw(cesped,x,y);
            }
        }
        batch.draw(contenedor,Gdx.graphics.getWidth() / 2 - contenedor.getWidth() / 2, Gdx.graphics.getHeight() / 2 - contenedor.getHeight() / 2);
        batch.end();
        stage.draw();

    }

    private void actualizarFondo(){
        lastY -= -20.0f * Gdx.graphics.getDeltaTime();
        if(lastY > cesped.getWidth()){
            lastY = 0f;
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

    private void createUI(){

        TextureAtlas textureAtlas = new TextureAtlas("gui/gui.pack");
        Skin skin = new Skin();
        skin.addRegions(textureAtlas);

        TextButton startButton = new TextButton("Start Game",new TextButton.TextButtonStyle(skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),new BitmapFont()));
        TextButton scoresButton = new TextButton("Scores",new TextButton.TextButtonStyle(skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),new BitmapFont()));
        TextButton exitButton = new TextButton("Exit Game",new TextButton.TextButtonStyle(skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),new BitmapFont()));

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStart.setScreen(gameStart.gameScreen);
            }
        });

        scoresButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStart.setScreen(gameStart.scoreScreen);
            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(0);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(startButton).width(200).padBottom(5);
        table.row();
        table.add(scoresButton).width(200).padBottom(5);
        table.row();
        table.add(exitButton).width(200);
        stage.addActor(table);
    }
}
