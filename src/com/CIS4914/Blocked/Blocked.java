package com.CIS4914.Blocked;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.CIS4914.Blocked.Screens.MainMenu;

public class Blocked extends Game {
	
	public static final String version = "0.00.00 Alpha Initial Commit";
	public static final String LOG = "Blocked";
	
	private AssetManager manager;
    private MainMenu mainMenuScreen;

   @Override
    public void create() {
	   	manager = new AssetManager();
		manager.load("smiley.png", Texture.class);
		manager.finishLoading();
		
        mainMenuScreen = new MainMenu(this);
        setScreen(mainMenuScreen);   
   }
   
   @Override
   public void render() {
	   super.render();
   }
}
