package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by saul- on 17/05/2016.
 */
public class ScoreScreen implements Screen {

    private GameStart gameStart;
    private Stage stage;
    private ArrayList<String> scores;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture scoresText;
    private Texture cajaPuntuacion;
    private ShapeRenderer shapeRenderer;
    private Rectangle volver, vida, salud;
    private float lastX = 0, lastY = 0, initialX , initialY;
    private Texture cesped;

    public ScoreScreen(GameStart gameStart){
        this.gameStart = gameStart;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        scoresText = new Texture("gui/scoresTable.png");
        cajaPuntuacion = new Texture("gui/cajaPuntuacion.png");
        font = new BitmapFont();
        initialX = Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 - 17;
        initialY = Gdx.graphics.getHeight() / 2 + 140;
        cesped = new Texture("gui/dirt.png");
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage();
        scores = new ArrayList<String>();
        getData();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        actualizarFondo();
        for(int y = (int) (lastY - cesped.getHeight()); y < Gdx.graphics.getHeight(); y += cesped.getHeight()){
            for(int x = (int) lastX; x < Gdx.graphics.getWidth(); x += cesped.getWidth()){
                batch.draw(cesped,x,y);
            }
        }
        batch.draw(scoresText,Gdx.graphics.getWidth() / 2 - scoresText.getWidth() / 2, Gdx.graphics.getHeight() / 2 - scoresText.getHeight() / 2);
        int nextPos = 0;

        for(int i = 0; i < 5; i++){
            batch.draw(cajaPuntuacion,initialX,initialY + nextPos);
            font.draw(batch,scores.get(i), initialX + 10, initialY + 40 + nextPos);
            nextPos -= cajaPuntuacion.getHeight() + 8;
        }

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(volver.getX(),volver.getY(),volver.getWidth(),volver.getHeight());
        shapeRenderer.end();
    }

    private void actualizarFondo(){
        lastY -= -20.0f * Gdx.graphics.getDeltaTime();
        if(lastY > cesped.getWidth()){
            lastY = 0f;
        }
    }


    private void getData(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tankmaster", "root", "");
            String query = "SELECT * FROM users";
            PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String text = "";
                text += resultSet.getString("name");
                text += " Tiempo: "+resultSet.getString("segundos");
                text += " Vida: "+resultSet.getString("vida");
                scores.add(text);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
}
