package com.comfyfutons.Blocked.Screens;

import Controllers.TextButton2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.comfyfutons.Blocked.Blocked;

public class MainMenu implements Screen{
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
	int screenHeight, screenWidth;
	
	//Constructor
	public MainMenu(Blocked game){
		this.game = game;
	}
	
	//Render Method: called continuously
	@Override
	public void render(float delta) {
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
		stage.draw();
		batch.end();
	}

	//Resize Method: called once when the stage is switched to and again every time it is resized
	@Override
	public void resize(int width, int height) {
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		
		playGameButton = new TextButton2("Play Game", buttonStyle, screenWidth/2, screenHeight/2, screenWidth/5, screenHeight/5);
		levelEditor = new TextButton2("Level Editor", buttonStyle, screenWidth/3, screenHeight/3, screenWidth/5, screenHeight/5);
		options = new TextButton2("Options", buttonStyle, screenWidth/4, screenHeight/4, screenWidth/5, screenHeight/5);
		stage.addActor(playGameButton);
	}

	//Show Method: called when this stage is switched to
	@Override
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin();
		textures = new TextureAtlas("textures.atlas");
		skin.addRegions(textures);
		defaultFont = new BitmapFont();
		
        buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("buttonUp");
		buttonStyle.down = skin.getDrawable("buttonDown");
		buttonStyle.font = defaultFont;
	}

	//Hide Method: ...
	@Override
	public void hide() {
		
	}

	//Pause Method: Called whenever the game is paused or exited temporarily
	@Override
	public void pause() {
		
	}

	//Resume Method: Called when the game is resumed or reopened
	@Override
	public void resume() {
		
	}

	//Dispose Method: called when exiting this stage completely
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
