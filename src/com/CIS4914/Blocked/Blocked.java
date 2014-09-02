package com.CIS4914.Blocked;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.CIS4914.Blocked.Screens.MainMenu;

public class Blocked extends Game {
	
	public static final String version = "0.00.00 Alpha Initial Commit";
	public static final String LOG = "Blocked";

    MainMenu mainMenuScreen;

   @Override
    public void create() {
            mainMenuScreen = new MainMenu(this);
            setScreen(mainMenuScreen);   
   }

}
