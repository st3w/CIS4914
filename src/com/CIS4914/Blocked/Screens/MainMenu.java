package com.CIS4914.Blocked.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;

public class MainMenu implements Screen{
	//Base Variables
	Blocked game;
	SpriteBatch batch;
	Stage stage;
	//Texture Variables
	Skin skin;
	BitmapFont defaultFont, buttonFont;
	TextureAtlas textures;
	Image backgroundImage;
	LabelStyle versionText;
	Label gameVersion;
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
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
		stage.draw();
		batch.end();
	}

	//Resize Method: called once when the stage is switched to and again every time it is resized
	@Override
	public void resize(int width, int height) {
		stage = new Stage();
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		buttonFont.setScale(width * 0.00035f);

		backgroundImage = new Image(Blocked.manager.get("main_menu_background.png", Texture.class));
		backgroundImage.setWidth(width);
		backgroundImage.setHeight(height);
		stage.addActor(backgroundImage);
		
		versionText = new LabelStyle(defaultFont, Color.WHITE);
		gameVersion = new Label("Version: " + Blocked.version, versionText);
		stage.addActor(gameVersion);
		
        buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("button_up");
		buttonStyle.down = skin.getDrawable("button_down");
		buttonStyle.font = buttonFont;
		
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		
		float buttonWidth = width * 0.23f;
		float buttonHeight = height * 0.16f;
		
		
		playGameButton = new TextButton2("Play Game", buttonStyle, width * 0.2f - buttonWidth * 0.5f, height * 0.45f, buttonWidth, buttonHeight);
		levelEditor = new TextButton2("Level Editor", buttonStyle, width * 0.5f - buttonWidth * 0.5f, height * 0.45f, buttonWidth, buttonHeight);
		options = new TextButton2("Options", buttonStyle, width * 0.8f - buttonWidth * 0.5f, height * 0.45f, buttonWidth, buttonHeight);
		stage.addActor(playGameButton);
		stage.addActor(levelEditor);
		stage.addActor(options);
		
		
		playGameButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//TO-DO FILL
			}
		});
		
		levelEditor.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((Blocked) Gdx.app.getApplicationListener()).setScreen(new EditorMenu(game)); 
			}
		});
		
		options.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//TO-DO FILL
			}
		});
	}

	//Show Method: called when this stage is switched to
	@Override
	public void show() {
		batch = new SpriteBatch();
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		defaultFont = new BitmapFont();
		buttonFont = new BitmapFont(Gdx.files.internal("Arial_Black_72pt.fnt"), false);

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
		game.dispose();
		batch.dispose();
		stage.dispose();
		skin.dispose();
		defaultFont.dispose();
		buttonFont.dispose();
		textures.dispose();
	}

}
