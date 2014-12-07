package com.CIS4914.Blocked.Screens;

import com.CIS4914.Blocked.Blocked;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.jndi.ldap.ManageReferralControl;

public class SplashScreen implements Screen{

	private float time;
	private SpriteBatch batch;
	private Sprite splash;
	
	private float introTime;
	private float showTime;
	private float fadeTime;
	
	private Blocked game;
	
	public SplashScreen(Blocked game) {
		this.game = game;
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		time += delta;
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// fade in
		if (time < introTime)
			splash.setAlpha(time / introTime);
		
		// fade out
		else if (time > introTime + showTime && time < introTime + showTime + fadeTime)
			splash.setAlpha(1 - ((time - (introTime + showTime)) / fadeTime));
		
		batch.begin();
		splash.draw(batch);
		batch.end();
		
		if (Blocked.manager.update() && time > introTime + showTime + fadeTime)
			game.setScreen(new MainMenu(game));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Blocked.manager.load("splash.png", Texture.class);
		Blocked.manager.finishLoading();
		
		batch = new SpriteBatch();
		splash = new Sprite(Blocked.manager.get("splash.png", Texture.class));
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		introTime = 1.0f;
		showTime = 2.0f;
		fadeTime = 1.0f;
		
		Blocked.manager.load("bricks/brick.png", Texture.class);
		Blocked.manager.load("bricks/movable_block.png", Texture.class);
	   	Blocked.manager.load("game_background.jpg", Texture.class);
	   	Blocked.manager.load("grid.png", Texture.class);
	   	Blocked.manager.load("main_menu_background.png", Texture.class);
		Blocked.manager.load("menu_background.png", Texture.class);
		Blocked.manager.load("table_background.png", Texture.class);
		Blocked.manager.load("generic.png", Texture.class);
		Blocked.manager.load("black.jpg", Texture.class);
		//Blocked.manager.load("sounds/menuTheme.mp3", Music.class);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
