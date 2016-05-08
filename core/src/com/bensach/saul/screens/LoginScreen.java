package com.bensach.saul.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by saul- on 06/04/2016.
 */

public class LoginScreen implements Screen {

    private GameStart gameStart;

    /*UI*/
    private Stage stage;

    public LoginScreen(GameStart gameStart) {
        this.gameStart = gameStart;
        stage = new Stage();
        createUI();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

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

        //TODO fix text offset
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(new BitmapFont(),Color.BLACK,null,null,skin.getDrawable("textField"));
        final TextField nameTextField = new TextField("", textFieldStyle);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMessageText("Username");
        final TextField passwordTextField = new TextField("", textFieldStyle);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        passwordTextField.setAlignment(Align.center);


        TextButton sendButton = new TextButton("Sign in",new TextButton.TextButtonStyle(skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),new BitmapFont()));
        sendButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                login(nameTextField.getText(), passwordTextField.getText());
            }
        });

        TextButton registerButton = new TextButton("Sign up",new TextButton.TextButtonStyle(skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),skin.getDrawable("orangeButton"),new BitmapFont()));
        registerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               gameStart.setScreen(gameStart.registerScreen);
            }
        });


        Table table = new Table();
        //table.setBackground(skin.getDrawable("panel"));
        table.setFillParent(true);
        table.add(nameTextField).width(200).padBottom(5);
        table.row();
        table.add(passwordTextField).width(200);
        table.row();
        table.add(sendButton).width(200).padTop(20);
        table.row();
        table.add(registerButton).width(200).padTop(5);


        table.setDebug(true);
        stage.addActor(table);

    }

    private void login(String user, String password){
        /*try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tankmaster", "root", "");
            String query = "SELECT password FROM users WHERE name LIKE ? AND password LIKE ?;";
            PreparedStatement statement = (PreparedStatement) conn.prepareStatement(query);
            statement.setString(1, user+"%");
            statement.setString(2, password+"%");
            ResultSet resultSet = statement.executeQuery();
            String dbPassword = "";
            while(resultSet.next()){
                dbPassword = resultSet.getString("password");
            }
            resultSet.close();
            statement.close();
            conn.close();
            if(dbPassword.equals(password)){
                gameStart.setScreen(gameStart.mainMenu);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
*/
    }
}
