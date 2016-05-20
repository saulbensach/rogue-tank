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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by saul- on 06/04/2016.
 */
public class RegisterScreen implements Screen {

    private GameStart gameStart;
    private SpriteBatch batch;
    private Texture contenedor;
    private BitmapFont font;
    private Texture cesped;
    private boolean correctPass = true;
    private Stage stage;
    private float lastX = 0, lastY = 0;

    public RegisterScreen(GameStart gameStart) {
        this.gameStart = gameStart;
        stage = new Stage();
        createUI();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        contenedor = new Texture("gui/contenedorGui.png");
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
        batch.draw(contenedor,Gdx.graphics.getWidth() / 2 - contenedor.getWidth() / 2,Gdx.graphics.getHeight() / 2 - contenedor.getHeight() / 2);
        if(!correctPass){
            font.draw(batch, "Las contraseñas no conciden", Gdx.graphics.getWidth() / 2 - 90, Gdx.graphics.getHeight() / 2 + 185);
        }
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
        stage.getViewport().update(width, height);
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

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(new BitmapFont(), Color.BLACK,null,null,skin.getDrawable("textField"));
        final TextField nameTextField = new TextField("", textFieldStyle);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMessageText("Username");
        final TextField passwordTextField = new TextField("", textFieldStyle);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        passwordTextField.setAlignment(Align.center);
        final TextField checkPasswordTextField = new TextField("", textFieldStyle);
        checkPasswordTextField.setMessageText("Password");
        checkPasswordTextField.setPasswordCharacter('*');
        checkPasswordTextField.setPasswordMode(true);
        checkPasswordTextField.setAlignment(Align.center);

        TextButton signupButton = new TextButton("Sign up",new TextButton.TextButtonStyle(skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),new BitmapFont()));
        signupButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(passwordTextField.getText().equals(checkPasswordTextField.getText())){
                    registerUser(nameTextField.getText(), passwordTextField.getText());
                    gameStart.setScreen(gameStart.mainMenu);
                }else{
                    correctPass = false;
                    nameTextField.setText("");
                    passwordTextField.setText("");
                    checkPasswordTextField.setText("");
                }
            }
        });


        TextButton cancelButton = new TextButton("Cancel",new TextButton.TextButtonStyle(skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),new BitmapFont()));
        cancelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStart.setScreen(gameStart.loginScreen);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(nameTextField).width(200).padBottom(5);
        table.row();
        table.add(passwordTextField).width(200).padBottom(5);
        table.row();
        table.add(checkPasswordTextField).width(200).padBottom(5);
        table.row();
        table.add(signupButton).width(200).padBottom(5);
        table.row();
        table.add(cancelButton).width(200);

        stage.addActor(table);

    }

    private void registerUser(String name, String password){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tankmaster", "root", "root");
            String query = "INSERT INTO users VALUES(?,?,?,?,?)";
            PreparedStatement statement = (PreparedStatement) connection.prepareStatement(query);
            statement.setNull(1, Types.INTEGER);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
