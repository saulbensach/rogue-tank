package com.bensach.saul.screens;

import com.badlogic.gdx.Game;
import com.bensach.saul.screens.*;

public class GameStart extends Game {

	public SplashScreen splashScreen;
	public LoginScreen loginScreen;
	public RegisterScreen registerScreen;
	public MainMenu mainMenu;
	public SettingsScreen settingsScreen;
	public GameScreen gameScreen;
	public ScoreScreen scoreScreen;

	@Override
	public void create () {

		splashScreen 	= new SplashScreen(this);
		loginScreen 	= new LoginScreen(this);
		registerScreen 	= new RegisterScreen(this);
		mainMenu 		= new MainMenu(this);
		settingsScreen 	= new SettingsScreen(this);
		gameScreen 		= new GameScreen(this);
		scoreScreen		= new ScoreScreen(this);

		setScreen(gameScreen);

	}

}
