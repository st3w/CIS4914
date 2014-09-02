package com.comfyfutons.Blocked.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.comfyfutons.Blocked.Blocked;
import com.comfyfutons.Blocked.Controllers.TextButton2;

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
	float screenHeight, screenWidth;
	
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
		
		float buttonWidth = screenWidth/4;
		float buttonHeight = screenHeight/4;
		
		
		playGameButton = new TextButton2("Play Game", buttonStyle, screenWidth*0.2f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		levelEditor = new TextButton2("Level Editor", buttonStyle, screenWidth*0.5f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		options = new TextButton2("Options", buttonStyle, screenWidth*0.8f - buttonWidth/2, screenHeight/2, buttonWidth, buttonHeight);
		stage.addActor(playGameButton);
		stage.addActor(levelEditor);
		stage.addActor(options);
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
