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

/**
 * Created by saul- on 06/04/2016.
 */
public class RegisterScreen implements Screen {

    private GameStart gameStart;

    private Stage stage;

    public RegisterScreen(GameStart gameStart) {
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

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(new BitmapFont(), Color.BLACK,null,null,skin.getDrawable("textField"));
        TextField nameTextField = new TextField("", textFieldStyle);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMessageText("Username");
        TextField passwordTextField = new TextField("", textFieldStyle);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        passwordTextField.setAlignment(Align.center);
        TextField checkPasswordTextField = new TextField("", textFieldStyle);
        checkPasswordTextField.setMessageText("Password");
        checkPasswordTextField.setPasswordCharacter('*');
        checkPasswordTextField.setPasswordMode(true);
        checkPasswordTextField.setAlignment(Align.center);

        TextButton signupButton = new TextButton("Sign up",new TextButton.TextButtonStyle(skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),skin.getDrawable("greenButton"),new BitmapFont()));
        signupButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStart.setScreen(gameStart.mainMenu);
            }
        });


        Button cancelButton = new Button(skin.getDrawable("orangeSquare"));
        //TODO fix button overlap
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
        table.add(checkPasswordTextField).width(200);
        table.row();
        HorizontalGroup group = new HorizontalGroup();
        group.addActor(cancelButton);
        group.addActor(signupButton);
        table.add(group).width(200).padTop(5);


        table.setDebug(true);
        stage.addActor(table);

    }
}
