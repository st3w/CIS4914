package com.CIS4914.Blocked.Screens;

import java.util.Vector;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Level;
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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;

public class GameMenu implements Screen{
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
	TextButton2 mainGameMap, customMap, load, mainMenu;
	Table levelList;
	
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	//Constructor
	public GameMenu(Blocked game){
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
		
		customLevels.add("Pit's fallow");
		customLevels.add("Fire city");
		customLevels.add("Custom Level 3");
		customLevels.add("Custom level 1");	
		customLevels.add("Blue terror");
		customLevels.add("Satin's dungeon");
		customLevels.add("the keymaster's lair");
		customLevels.add("Happy rainbow fairy land");	
		customLevels.add("Garden of eatin'");
		customLevels.add("super fun time level");
		customLevels.add("Calipsow's end");
		customLevels.add("the furrow");	
		customLevels.add("Temple of undending wruin");	
		customLevels.add("Dark's den");	
		customLevels.add("white shoes are cool");	
		//TEMPORARY
		
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
		buttonStyle.checked = skin.getDrawable("button_down");
		buttonStyle.font = defaultFont;
		
		float buttonWidth = width * 0.3f;
		float buttonHeight = width * 0.1f;
		
		mainGameMap = new TextButton2("Main Game Maps", buttonStyle, width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		customMap = new TextButton2("Custom Maps", 	buttonStyle, width * 0.015f, height - buttonHeight * 2 - width * 0.04f, buttonWidth, buttonHeight);
		load = new TextButton2("Load", buttonStyle, width * 0.729f - buttonWidth * 0.5f, width * 0.015f, buttonWidth, buttonHeight);
		mainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.015f, width * 0.015f, buttonWidth, buttonHeight);
		
		stage.addActor(mainGameMap);
		stage.addActor(customMap);
		stage.addActor(load);
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
		//Main Game Maps Listener
		mainGameMap.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				customMap.setChecked(false);
				levelList.clear();
				for(int i = 0; i < mainLevels.size(); i++){
					final TextButton temp2 = new TextButton(mainLevels.get(i), buttonStyle);
					
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
				mainGameMap.setChecked(false);
				levelList.clear();
				for(int i = 0; i < customLevels.size(); i++){
					final TextButton temp2 = new TextButton(customLevels.get(i), buttonStyle);
					
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
				
				if(mainGameMap.isChecked()){
					
				} else if(customMap.isChecked()){
					
				} else{
					System.out.println("no category selected");
				}
				
				
				if(mapSelected){
					((Blocked) Gdx.app.getApplicationListener()).setScreen(new LevelEditor(selectedLevel, game)); 
				}
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