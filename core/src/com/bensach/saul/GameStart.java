package com.bensach.saul;

import com.badlogic.gdx.Game;
import com.bensach.saul.screens.*;

public class GameStart extends Game {

	public SplashScreen splashScreen;
	public LoginScreen loginScreen;
	public RegisterScreen registerScreen;
	public MainMenu mainMenu;
	public SettingsScreen settingsScreen;

	@Override
	public void create () {

		splashScreen 	= new SplashScreen(this);
		loginScreen 	= new LoginScreen(this);
		registerScreen 	= new RegisterScreen(this);
		mainMenu 		= new MainMenu(this);
		settingsScreen 	= new SettingsScreen(this);

	}

}
