package com.CIS4914.Blocked;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.CIS4914.Blocked.Screens.MainMenu;

public class Blocked extends Game {
	
	public static final String version = "V0.00.08 Pre-Alpha (custom levels populated in game menu and also load blocks into game)";
	public static final String LOG = "Blocked";
	public static AssetManager manager;
	public static final int blockSize = 90;
	public static final int worldHeight = 1080;
	
    private MainMenu mainMenuScreen;

   @Override
    public void create() {
	   	manager = new AssetManager();
	   	manager.load("bricks/brick.png", Texture.class);
	   	manager.load("game_background.jpg", Texture.class);
	   	manager.load("grid.png", Texture.class);
	   	manager.load("main_menu_background.png", Texture.class);
		manager.load("menu_background.png", Texture.class);
		manager.load("table_background.png", Texture.class);
		manager.load("generic.png", Texture.class);
		manager.finishLoading();
		
        mainMenuScreen = new MainMenu(this);
        setScreen(mainMenuScreen);   
   }
   
   @Override
   public void render() {
	   super.render();
   }
}
