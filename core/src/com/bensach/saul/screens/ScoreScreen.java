package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mysql.jdbc.PreparedStatement;
import sun.security.provider.SHA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by saul- on 17/05/2016.
 */
public class ScoreScreen implements Screen {
    private boolean DEBUG = false;
    private GameStart gameStart;
    private Stage stage;
    private ArrayList<String> scores;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture scoresText;
    private Texture cajaPuntuacion;
    private boolean touched = false;
    private ShapeRenderer shapeRenderer;
    private Rectangle volver, vida, tiempo, flechaAbajo, flechaArriba;
    private float lastX = 0, lastY = 0, initialX , initialY;
    private Texture cesped;
    private int overflow = 0;
    public ScoreScreen(GameStart gameStart){
        this.gameStart = gameStart;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        scoresText = new Texture("gui/scoresTable.png");
        cajaPuntuacion = new Texture("gui/cajaPuntuacion.png");
        font = new BitmapFont();
        volver = new Rectangle(Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 - 18, Gdx.graphics.getHeight() / 2 - cajaPuntuacion.getHeight() / 2 - 177, 124,30);
        vida = new Rectangle(Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 + 108, Gdx.graphics.getHeight() / 2 - cajaPuntuacion.getHeight() / 2 - 177, 124,30);
        tiempo = new Rectangle(Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 + 232, Gdx.graphics.getHeight() / 2 - cajaPuntuacion.getHeight() / 2 - 177, 124,30);
        flechaAbajo = new Rectangle(Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 + 390, Gdx.graphics.getHeight() / 2 - cajaPuntuacion.getHeight() / 2 - 200, 28,42);
        flechaArriba = new Rectangle(Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 + 390, Gdx.graphics.getHeight() / 2 - cajaPuntuacion.getHeight() / 2 + 224, 28,42);
        initialX = Gdx.graphics.getWidth() / 2 - cajaPuntuacion.getWidth() / 2 - 17;
        initialY = Gdx.graphics.getHeight() / 2 + 140;
        cesped = new Texture("gui/dirt.png");
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage();
        scores = new ArrayList<String>();
        getData();
        scores.add("RUBEN");
        scores.add("SAUL");
        scores.add("ALMELA");
        scores.add("VICENT");
        scores.add("PUTA");
        scores.add("XAVI");
        scores.add("RUBEN");
        scores.add("SAUL");
        scores.add("ALMELA");
        scores.add("VICENT");
        scores.add("PUTA");
        scores.add("XAVI");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        batch.begin();
        actualizarFondo();
        detectButtons();
        for(int y = (int) (lastY - cesped.getHeight()); y < Gdx.graphics.getHeight(); y += cesped.getHeight()){
            for(int x = (int) lastX; x < Gdx.graphics.getWidth(); x += cesped.getWidth()){
                batch.draw(cesped,x,y);
            }
        }
        batch.draw(scoresText,Gdx.graphics.getWidth() / 2 - scoresText.getWidth() / 2, Gdx.graphics.getHeight() / 2 - scoresText.getHeight() / 2);
        int nextPos = 0;
        if(scores.size() > 0){
            int max = (scores.size() < 5) ? scores.size() : 5;
            for(int i = 0; i < max; i++){
                batch.draw(cajaPuntuacion,initialX,initialY + nextPos);
                font.setColor(Color.BROWN);
                font.draw(batch,scores.get(i + overflow), initialX + 10, initialY + 40 + nextPos);
                nextPos -= cajaPuntuacion.getHeight() + 8;
            }
        }

        batch.end();
        if(DEBUG){
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(volver.getX(),volver.getY(),volver.getWidth(),volver.getHeight());
            shapeRenderer.rect(vida.getX(), volver.getY(), volver.getWidth(), volver.getHeight());
            shapeRenderer.rect(tiempo.getX(), tiempo.getY(), tiempo.getWidth(),tiempo.getHeight());
            shapeRenderer.rect(flechaAbajo.getX(),flechaAbajo.getY(),flechaAbajo.getWidth(),flechaAbajo.getHeight());
            shapeRenderer.rect(flechaArriba.getX(), flechaArriba.getY(), flechaArriba.getWidth(),flechaArriba.getHeight());
            shapeRenderer.end();
        }
    }

    private void detectButtons(){
        Vector2 mousePos = new Vector2();
        if(Gdx.input.justTouched()){
            mousePos = new Vector2(Gdx.input.getX(), Math.abs(Gdx.input.getY() - Gdx.graphics.getHeight()));
            touched = true;
        }
        if(touched){
            if(volver.contains(mousePos)){
                gameStart.setScreen(gameStart.mainMenu);
                touched = false;
            }
            if(vida.contains(mousePos)){
                getDataOrderByVida();
                touched = false;
            }
            if(tiempo.contains(mousePos)){
                getDataOrderByTiempo();
                touched = false;
            }
            if(flechaAbajo.contains(mousePos)){
                desplazarAbajo();
                touched = false;
            }
            if(flechaArriba.contains(mousePos)){
                desplazarArriba();
                touched = false;
            }
        }
    }

    private void desplazarArriba(){
        overflow--;
        if(overflow < 0)overflow = 0;
    }

    private void desplazarAbajo(){
        overflow++;
        if(overflow > scores.size() - 5)overflow--;
    }

    private void actualizarFondo(){
        lastY -= -20.0f * Gdx.graphics.getDeltaTime();
        if(lastY > cesped.getWidth()){
            lastY = 0f;
        }
    }

    private void getDataOrderByVida(){

    }

    private void getDataOrderByTiempo(){

    }


    private void getData(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tankmaster", "root", "root");
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
