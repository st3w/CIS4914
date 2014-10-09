package com.CIS4914.Blocked.Screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

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
	TextButtonStyle buttonStyle, buttonStyleCheckable;
	TextButton2 newMap, mainGameMap, customMap, load, delete, mainMenu;
	Table levelList;

	//Other
	Json json;
	private FileHandle saveFile;
	
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
		
		final Vector<String> newLevels = new Vector();
		final Vector<String> mainLevels = new Vector();
		final Vector<String> customLevels = new Vector();

		newLevels.add("Small");
		newLevels.add("Medium");
		newLevels.add("Large");

		mainLevels.add("Level 1");
		mainLevels.add("Level 2");
		mainLevels.add("Level 3");
		mainLevels.add("Level 4");
		mainLevels.add("Level 5");
		mainLevels.add("Level 6");

		Level[] levelArray = null;
		
		saveFile = Gdx.files.local("customLevels");
		if(saveFile.exists()){
			String customLevelString = saveFile.readString();

			String[] customLevelArray = customLevelString.split("\n");

			levelArray = new Level[customLevelArray.length];

			for(int i = 0; i < customLevelArray.length; i++){			
				levelArray[i] = json.fromJson(Level.class, (String) customLevelArray[i]);
				customLevels.add(levelArray[i].getName());
			}


		}
		
		final Level[] levels = levelArray;
		


		stage = new Stage();
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
		
		defaultFont.setScale(width * 0.00035f);
		
		//Background Textures
		background = Blocked.manager.get("menu_background.png");
		backgroundImage = new Image(background);
		backgroundImage.setWidth(width);
		backgroundImage.setHeight(height);
		stage.addActor(backgroundImage);
		
		tableBackground = Blocked.manager.get("table_background.png");
		tableBackgroundImage = new Image(tableBackground);
		tableBackgroundImage.setWidth(width * 0.52f);
		tableBackgroundImage.setHeight(height * 0.75f);
		tableBackgroundImage.setX(width * 0.989f - tableBackgroundImage.getWidth());
		tableBackgroundImage.setY(height * 0.98f - tableBackgroundImage.getHeight());
		stage.addActor(tableBackgroundImage);
		
		
		//Button instantiation and adding to stage
        buttonStyle = new TextButtonStyle();
		buttonStyle.up = skin.getDrawable("button_up");
		buttonStyle.down = skin.getDrawable("button_down");
		buttonStyle.font = defaultFont;
		
		buttonStyleCheckable = new TextButtonStyle();
		buttonStyleCheckable.up = skin.getDrawable("button_up");
		buttonStyleCheckable.down = skin.getDrawable("button_down");
		buttonStyleCheckable.checked = skin.getDrawable("button_down");
		buttonStyleCheckable.font = defaultFont;
		
		float buttonWidth = width * 0.3f;
		float buttonHeight = width * 0.1f;
		
		newMap = new TextButton2("New Map", buttonStyleCheckable, width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		mainGameMap = new TextButton2("Main Game Maps", buttonStyleCheckable, width * 0.015f, height - buttonHeight * 2 - width * 0.04f, buttonWidth, buttonHeight);
		customMap = new TextButton2("Custom Maps", buttonStyleCheckable, width * 0.015f, height - buttonHeight * 3 - width * 0.06f, buttonWidth, buttonHeight);
		load = new TextButton2("Load", buttonStyle, /*INSERT*/ width * 0.989f - tableBackgroundImage.getWidth(), width * 0.015f, buttonWidth * 0.8f, buttonHeight);
		delete = new TextButton2("Delete", buttonStyle, /*INSERT*/ width * 0.989f - buttonWidth * 0.8f , width * 0.015f, buttonWidth *0.8f, buttonHeight);
		mainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.015f, width * 0.015f, buttonWidth, buttonHeight);
		
		stage.addActor(newMap);
		stage.addActor(mainGameMap);
		stage.addActor(customMap);
		stage.addActor(load);
		stage.addActor(delete);
		stage.addActor(mainMenu);
		
		//Scrollable List
		levelList = new Table(skin);	

	    ScrollPane scroller = new ScrollPane(levelList);

	    Table table = new Table();
	    table.setFillParent(true);
	    table.add(scroller).height(height * 0.724f);;
	    
	    table.setWidth(width * 0.501f);
	    table.setHeight(height * 0.7f);
	    table.setX(width * 0.98f - table.getWidth());
	    table.setY(-height * 0.034f);
	    
	    table.left();
	    table.top();

	    stage.addActor(table);
		
		//Button Listeners//
		//New Map Button Listeners
		newMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				mainGameMap.setChecked(false);
				customMap.setChecked(false);
				levelList.clear();
				for(int i = 0; i < newLevels.size(); i++){
					final TextButton temp2 = new TextButton(newLevels.get(i), buttonStyleCheckable);
					
					//button from list
					temp2.addListener(new InputListener(){
						public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
							return true;
						}
						public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
							updateList(temp2.getText().toString());
						}
					});
					
					levelList.add(temp2).left().width(screenWidth * 0.5f).height(screenHeight * 0.15f);
					levelList.row();
				}
				levelList.top();
			}
		});
		
		//Main Game Maps Listener
		mainGameMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				newMap.setChecked(false);
				customMap.setChecked(false);
				levelList.clear();
				for(int i = 0; i < mainLevels.size(); i++){
					final TextButton temp2 = new TextButton(mainLevels.get(i), buttonStyleCheckable);
					
					//button from list
					temp2.addListener(new InputListener(){
						public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
							return true;
						}
						public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
							updateList(temp2.getText().toString());
						}
					});
					
					levelList.add(temp2).left().width(screenWidth * 0.5f).height(screenHeight * 0.15f);
					levelList.row();
				}
				levelList.top();
				
			}
		});
		
		//Custom Maps Button Listener
		customMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				newMap.setChecked(false);
				mainGameMap.setChecked(false);
				levelList.clear();
				for(int i = 0; i < customLevels.size(); i++){
					final TextButton temp2 = new TextButton(customLevels.get(i), buttonStyleCheckable);
					
					//button from list
					temp2.addListener(new InputListener(){
						public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
							return true;
						}
						public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
							updateList(temp2.getText().toString());
						}
					});
					
					levelList.add(temp2).left().width(screenWidth * 0.5f).height(screenHeight * 0.15f);
					levelList.row();
				}
				levelList.top();
			}
		});
		
		//Load Button Listener
		load.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Boolean mapSelected = false;
				Level selectedLevel = null;
				
				
				if(newMap.isChecked()){
					Array<Cell> list = levelList.getCells();
					for(int i = 0; i < newLevels.size(); i++){
						TextButton currentButton = ((TextButton) list.get(i).getActor());
						String buttonText = currentButton.getText().toString();
						if(currentButton.isChecked()){
							mapSelected = true;

							if(buttonText.equals("Small")){
								selectedLevel = new Level("New Small Level", 100);
							} else if(buttonText.equals("Medium")){
								selectedLevel = new Level("New Medium Level", 200);
							} else if(buttonText.equals("Large")){
								selectedLevel = new Level("New Large Level", 300);
							} else{
								System.out.println("no map selected");
							}
						}	
					}
				} else if(mainGameMap.isChecked()){
					
				} else if(customMap.isChecked()){
					Array<Cell> list = levelList.getCells();
					for(int i = 0; i < customLevels.size(); i++){
						TextButton currentButton = ((TextButton) list.get(i).getActor());
						String buttonText = currentButton.getText().toString();
						if(currentButton.isChecked()){
							if(buttonText.equals(levels[i].getName())){
								mapSelected = true;
								selectedLevel = levels[i];
							} else{
								System.out.println("no map selected");
							}
						}
						
					}
				} else{
					System.out.println("no category selected");
				}
				
				
				if(mapSelected){
					((Blocked) Gdx.app.getApplicationListener()).setScreen(new LevelEditor(selectedLevel, game)); 
				}
			}
		});
		
		//Main Menu Button Listener
		delete.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				if(customMap.isChecked()){
					
					String customLevelString = saveFile.readString();
					String[] customLevelArray = customLevelString.split("\n");
					Level[] levelArray = new Level[customLevelArray.length];

					for(int i = 0; i < customLevelArray.length; i++){			
						levelArray[i] = json.fromJson(Level.class, (String) customLevelArray[i]);
					}
					
					Array<Cell> list = levelList.getCells();
					int offset = 0; // if the first file is deleted we still want an overwrite instead of append
					String buttonText = "";
					
					for(int i = 0; i < levelArray.length; i++){
						TextButton currentButton = ((TextButton) list.get(i).getActor());
						if(currentButton.isChecked()){
							buttonText = currentButton.getText().toString();
						}
						
						
						String levelSave = json.toJson(levelArray[i]);
						if(!buttonText.equals(levelArray[i].getName())){
							if(i - offset == 0){ // in the first iteration need to overwrite
								saveFile.writeString(levelSave + "\n", false);
							} else{
								saveFile.writeString(levelSave + "\n", true);
							}
						} else{
							offset++;
						}
					}
				}
				((Blocked) Gdx.app.getApplicationListener()).setScreen(new EditorMenu(game)); 
			}
		});

		//Main Menu Button Listener
		mainMenu.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((Blocked) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game)); 

			}
		});
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		defaultFont = new BitmapFont(Gdx.files.internal("arial_black_72pt.fnt"), false);
		
		json = new Json();
		
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
	
	/*	updateList Method: Takes the table and updates it by getting the cells, 
	*						putting them in an array, making sure the only checked 
	*						button is the selected one, then re-populates the levelList table */
	void updateList(String selectedName){
		Array<Cell> list = levelList.getCells();
		for(int i = 0; i < list.size; i++){
			if(((TextButton) list.get(i).getActor()).isChecked() && !((TextButton) list.get(i).getActor()).getText().toString().equals(selectedName)){
				((TextButton) list.get(i).getActor()).setChecked(false);
			}
		}
	}
	
	
}
