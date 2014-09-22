package com.CIS4914.Blocked.Screens;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class LevelEditor implements Screen 
{
	//Base Variables
	Blocked game;
	Level currentLevel;
	SpriteBatch batch;
	Stage stage;
	//Texture Variables
	Skin skin;
	BitmapFont defaultFont;
	TextureAtlas textures;
	//Button Variables
	TextButtonStyle buttonStyle;
	TextButton2 testButton;
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	public LevelEditor(Level selectedLevel, Blocked game)
	{
		currentLevel = selectedLevel;
		this.game = game;
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
		stage.draw();
		batch.end();	
	}

	@Override
	public void resize(int width, int height) {
		
		stage = new Stage();
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		defaultFont.setScale(width*0.002f);
		
        buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("buttonUp");
		buttonStyle.down = skin.getDrawable("buttonDown");
		buttonStyle.font = defaultFont;
		
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		
		float buttonWidth = screenWidth/4;
		float buttonHeight = screenHeight/4;
		
		String test = currentLevel.getName();
		
		testButton = new TextButton2(test, buttonStyle, screenWidth*0.5f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		stage.addActor(testButton);
	}

	@Override
	public void show() {
		
		batch = new SpriteBatch();
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		defaultFont = new BitmapFont();	
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
		
		batch.dispose();
		game.dispose();
		skin.dispose();
		defaultFont.dispose();
		textures.dispose();
		stage.dispose();	
	}

}
