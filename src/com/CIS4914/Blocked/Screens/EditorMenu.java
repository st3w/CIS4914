package com.CIS4914.Blocked.Screens;

import java.util.Vector;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class EditorMenu implements Screen{
	//Base Variables
	Blocked game;
	SpriteBatch batch;
	Stage stage;
	
	//Texture Variables
	Skin skin;
	BitmapFont defaultFont;
	TextureAtlas textures;
	Texture background, tableBackground;
	Image backgroundImage, tableBackgroundImage;
	
	//Button Variables
	TextButtonStyle buttonStyle;
	TextButton2 newMap, mainGameMap, customMap, load;
	Table levelList;
	
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	//Constructor
	public EditorMenu(Blocked game){
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
		screenHeight = height;
		screenWidth = width;
		
		//TEMPORARY
		final Vector<String> mainLevels = new Vector();
		final Vector<String> customLevels = new Vector();
		
		mainLevels.add("Level 1");
		mainLevels.add("Level 2");
		mainLevels.add("Level 3");
		mainLevels.add("Level 4");
		mainLevels.add("Level 5");
		mainLevels.add("Level 6");
		
		customLevels.add("Custom Level 1");
		customLevels.add("Custom Level 2");
		customLevels.add("Custom Level 3");
		customLevels.add("Custom Level 4");	
		//TEMPORARY
		
		stage = new Stage();
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		defaultFont.setScale(width * 0.002f);
		
		//Background Textures
		background = new Texture("menu_background.png");
		backgroundImage = new Image(background);
		backgroundImage.setWidth(width);
		backgroundImage.setHeight(height);
		stage.addActor(backgroundImage);
		
		tableBackground = new Texture("table_background.png");
		tableBackgroundImage = new Image(tableBackground);
		tableBackgroundImage.setWidth(width * 0.4f);
		tableBackgroundImage.setHeight(height * 0.8f);
		tableBackgroundImage.setX(width - tableBackgroundImage.getWidth());
		tableBackgroundImage.setY(height - tableBackgroundImage.getHeight());
		stage.addActor(tableBackgroundImage);
		
		
		//Button instantiation and adding to stage
        buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("buttonUp");
		buttonStyle.down = skin.getDrawable("buttonDown");
		buttonStyle.font = defaultFont;
		
		float buttonWidth = width * 0.3f;
		float buttonHeight = width * 0.1f;
		
		newMap = new TextButton2("New Map", buttonStyle, width * 0.15f - buttonWidth * 0.5f, height * 0.9f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		mainGameMap = new TextButton2("Main Game Map", buttonStyle, width * 0.15f - buttonWidth * 0.5f, height * 0.7f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		customMap = new TextButton2("Custom Map", buttonStyle, width * 0.15f - buttonWidth * 0.5f, height * 0.5f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		load = new TextButton2("Load", buttonStyle, width * 0.8f - buttonWidth * 0.5f, height * 0.1f - buttonHeight * 0.5f, buttonWidth, buttonHeight);
		
		stage.addActor(newMap);
		stage.addActor(mainGameMap);
		stage.addActor(customMap);
		stage.addActor(load);
		
		//Button Listeners//
		//New Map Button Listeners
		newMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//TO-DO FILL
			}
		});
		
		//Main Game Maps Listener
		mainGameMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				levelList.clear();
				for(int i = 0; i < mainLevels.size(); i++){
					LabelStyle testStyle = new LabelStyle(defaultFont, Color.ORANGE);
					Label temp = new Label(mainLevels.get(i), testStyle);
					levelList.add(temp);
					levelList.row();
				}
			}
		});
		
		//Custom Maps Button Listener
		customMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				levelList.clear();
				for(int i = 0; i < customLevels.size(); i++){
					LabelStyle testStyle = new LabelStyle(defaultFont, Color.ORANGE);
					Label temp = new Label(customLevels.get(i), testStyle);
					levelList.add(temp);
					levelList.row();
				}
			}
		});
		
		//Load Button Listener
		load.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

				stage.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.run(new Runnable() {
					@Override
					public void run() {
					}
				})));
			}
		});
		
		//Scrollable List
		levelList = new Table(skin);	

	    ScrollPane scroller = new ScrollPane(levelList);

	    Table table = new Table();
	    table.setFillParent(true);
	    table.add(scroller);
	    
	    table.setWidth(width * 0.38f);
	    table.setHeight(height * 0.75f);
	    table.setX(width - table.getWidth());
	    table.setY(-height * 0.01f);

	    table.left();
	    table.top();
	    
	    stage.addActor(table);
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
