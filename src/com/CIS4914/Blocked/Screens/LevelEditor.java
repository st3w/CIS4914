package com.CIS4914.Blocked.Screens;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
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
	SpriteBatch batch;
	Stage stage;
	//Texture Variables
	Skin skin;
	BitmapFont defaultFont;
	TextureAtlas textures;
	//Button Variables
	TextButtonStyle buttonStyle;
	TextButton2 playGameButton, levelEditor, options;
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	public LevelEditor(Blocked game)
	{
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
		
		
		playGameButton = new TextButton2("Play Game", buttonStyle, screenWidth*0.2f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		levelEditor = new TextButton2("Level Editor", buttonStyle, screenWidth*0.5f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		options = new TextButton2("Options", buttonStyle, screenWidth*0.8f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		stage.addActor(playGameButton);
		stage.addActor(levelEditor);
		stage.addActor(options);	
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
