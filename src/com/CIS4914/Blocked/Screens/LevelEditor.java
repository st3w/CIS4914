package com.CIS4914.Blocked.Screens;

import java.util.Vector;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class LevelEditor implements Screen, GestureListener {
	
	//Base Variables
	Blocked game;
	SpriteBatch batch;
	Stage stage;
	Stage saveStage;
	Level selectedLevel;
	InputMultiplexer input = new InputMultiplexer();
	
	//Texture Variables
	Skin skin;
	BitmapFont defaultFont, invisibleFont;
	TextureAtlas textures;
	Texture tableBackground, gameBackground, gridBackground, immovableBlock,saveBackground;
	Image backgroundImage, tableBackgroundImage, saveBackgroundImage;
	Vector<Sprite> background = new Vector<Sprite>();
	int backgroundTiles, gridTiles, blockTiles;
	
	//Button Variables
	TextButtonStyle buttonStyle, buttonStyleCheckable, blockButtonStyle;
	TextButton2 mainMenu, pause, save;
	Table levelList;
	
	//TextField
	TextField textBox;
	TextFieldStyle textStyle;
	
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	//Camera Variables
	private OrthographicCamera camera;
	
	// Game Pause State
	int state;
	public static final int GAME_PAUSED = 2; 
	
	//Constructor
	public LevelEditor(Level selectedLevel, Blocked game){
		this.game = game;
		this.selectedLevel = selectedLevel;
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    batch.setProjectionMatrix(camera.combined);
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
		for(int i = 0; i < backgroundTiles + gridTiles + blockTiles; i++)
		{
			background.get(i).draw(batch);
		}
		batch.end();
		
		
		// For Save menu
		if(state == GAME_PAUSED)
		{
			batch.begin();
			stage.draw();
			saveStage.draw();
			batch.end();
		}
		else
		{
			batch.begin();
			stage.draw();
			batch.end();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
		screenHeight = height;
		screenWidth = width;
		
		float cameraWidth = 1080 * width/height;
		camera = new OrthographicCamera(cameraWidth, 1080);
		
		if(camera.viewportWidth < 1920){
			camera.translate((camera.viewportWidth - 1920) * 0.5f, 0);
			camera.update();
		}
		if(camera.viewportWidth > 1920){
			camera.translate((camera.viewportWidth - 1920) * 0.5f, 0);
			camera.update();
		}
		
		//TEMPORARY
		final Vector<String> blocks = new Vector();
		
		blocks.add("A");
		blocks.add("B");
		blocks.add("C");
		blocks.add("D");
		blocks.add("E");
		blocks.add("F");
		blocks.add("G");
		blocks.add("H");
		blocks.add("I");
		blocks.add("J");
		blocks.add("K");
		blocks.add("L");
		//TEMPORARY
		
		stage = new Stage();
		stage.clear();
		saveStage = new Stage();
		saveStage.clear();
		
		input.addProcessor(stage);
		input.addProcessor(saveStage);
		input.addProcessor(new GestureDetector(this));
		Gdx.input.setInputProcessor(input);
		
		defaultFont.setScale(width * 0.00035f);
		
		//Background Textures
		tableBackground = Blocked.manager.get("table_background.png");
		tableBackgroundImage = new Image(tableBackground);
		tableBackgroundImage.setWidth(width * 0.12f);
		tableBackgroundImage.setHeight(height * 0.96f);
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
		
		blockButtonStyle = new TextButtonStyle();
		blockButtonStyle.up = skin.getDrawable("brick_button_up");
		blockButtonStyle.down = skin.getDrawable("brick_button_down");
		blockButtonStyle.checked = skin.getDrawable("brick_button_down");
		blockButtonStyle.font = invisibleFont;
		
		float buttonWidth = width * 0.18f;
		float buttonHeight = width * 0.06f;
		
		mainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		pause = new TextButton2("Pause", buttonStyle, screenWidth * 0.015f + buttonWidth + width * 0.02f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		save = new TextButton2("Save", buttonStyle, screenWidth * 0.015f + (buttonWidth + width * 0.02f) * 2f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		
		stage.addActor(mainMenu);		
		stage.addActor(pause);
		stage.addActor(save);

		//Scrollable List
		levelList = new Table(skin);	

	    ScrollPane scroller = new ScrollPane(levelList);

	    Table table = new Table();
	    table.setFillParent(true);
	    table.add(scroller).height(height * 0.93f);;
	    
	    table.setWidth(width * 0.1f);
	    table.setHeight(height * 0.7f);
	    table.setX(width * 0.98f - table.getWidth());
	    table.setY(-height * 0.034f);
	    
	    table.left();
	    table.top();

	    stage.addActor(table);
		
	    levelList.clear();
		for(int i = 0; i < blocks.size(); i++){
			final TextButton temp2;
			
			if(i == 0){
				temp2 = new TextButton(blocks.get(i), blockButtonStyle);
			} else{
				temp2 = new TextButton(blocks.get(i), buttonStyleCheckable);
			}
			
			//button from list
			temp2.addListener(new InputListener(){
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					updateList(temp2.getText().toString());
				}
			});
			
			levelList.add(temp2).left().width(screenWidth * 0.1f).height(screenWidth * 0.1f);
			levelList.row();
		}
		levelList.top();
	    
		// Main Menu Button Listener
		mainMenu.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				((Blocked) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game)); 

			}
		});
		
		// For saving Files 
		saveBackground = Blocked.manager.get("table_background.png");
		saveBackgroundImage = new Image(saveBackground);
		saveBackgroundImage.setWidth(width * 0.4f);
		saveBackgroundImage.setHeight(height * 0.25f);
		saveBackgroundImage.setX(width * 0.5f - saveBackgroundImage.getWidth() * 0.5f);
		saveBackgroundImage.setY(height * 0.5f - saveBackgroundImage.getHeight() * 0.5f);
		saveStage.addActor(saveBackgroundImage);
		
		
		textStyle = new TextFieldStyle(defaultFont, Color.RED, null, skin.getDrawable("button_down"), null);
		textBox = new TextField("File Name", textStyle);
		textBox.setWidth(width * 0.33f);
		textBox.setHeight(height * 0.11f);
		textBox.setX(width * 0.5f - textBox.getWidth() * 0.5f);
		textBox.setY(height * 0.5f - textBox.getHeight() * 0.5f);
		saveStage.addActor(textBox);

		
		
		/*textBox.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped (TextField textBox, char key) {
				if (key == '\n') textBox.getOnscreenKeyboard().show(false);
			}
		});
		*/
		
		// Save button Listener
		save.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				state = 2; 
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
		invisibleFont = new BitmapFont();
		invisibleFont.setScale(0.00000001f);
		
		updateMap();
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
	
	void updateMap() {
		background.clear();
		// Add background image to background vector
		gameBackground = Blocked.manager.get("game_background.jpg");
		backgroundTiles = (int)Math.ceil(selectedLevel.getWidth() / 21);
		for(int i = 0; i < backgroundTiles; i++){
			Sprite backgroundSprite = new Sprite(gameBackground);
			backgroundSprite.setOrigin(0,0);
			backgroundSprite.setPosition((1920 * i) + -backgroundSprite.getWidth()/2,-backgroundSprite.getHeight()/2);
			background.add(backgroundSprite);
		}
		
		// Add blocks to background vector
		immovableBlock = Blocked.manager.get("bricks/brick.png");
		blockTiles = 0;
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < selectedLevel.getWidth(); j++){
				if(selectedLevel.getGrid(j, i) == 1){
					blockTiles++;
					Sprite blockSprite = new Sprite(immovableBlock);
					blockSprite.setOrigin(0, 0);
					blockSprite.setPosition((90 * j) + -1920/2,-(90 * i) + 450);
					background.add(blockSprite);
				}
			}
		}
		
		
		// Add grid to background vector
		gridBackground = Blocked.manager.get("grid.png");
		gridTiles = (int)Math.ceil(selectedLevel.getWidth() / 12);
		for(int i=0; i < gridTiles; i++)
		{
			Sprite gridSprite = new Sprite(gridBackground);
			gridSprite.setOrigin(0, 0);
			gridSprite.setPosition((1080 * i) + -1920/2,-gridSprite.getHeight()/2);
			background.add(gridSprite);
		}
	}

	/*******************
	 * GESTURE DETECTION
	 *******************/
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Array<Cell> list = levelList.getCells();
		
		int xPos = (int) Math.floor(x * (1920 / screenWidth) + camera.position.x)/90;
		int yPos = (int) Math.floor(y * (1080 / screenHeight))/90;
		if(selectedLevel.getGrid(xPos, yPos) == 0 && ((TextButton) list.get(0).getActor()).isChecked()){
			selectedLevel.setGrid(1, xPos, yPos);
		}else if(selectedLevel.getGrid(xPos, yPos) == 0 && ((TextButton) list.get(1).getActor()).isChecked()){
			// Set to second block
		}else if(selectedLevel.getGrid(xPos, yPos) == 0 && ((TextButton) list.get(2).getActor()).isChecked()){
			// Set to third block
		}else if(selectedLevel.getGrid(xPos, yPos) == 0 && ((TextButton) list.get(3).getActor()).isChecked()){
			// Set to fourth block
		}else if(selectedLevel.getGrid(xPos, yPos) == 1){
			selectedLevel.setGrid(0, xPos, yPos);
		}
		updateMap();
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if(camera.position.x - deltaX * 2 > 0 && camera.position.x - deltaX * 2 < 1920 * (backgroundTiles - 1)){
			camera.translate(-deltaX * 2,0);
			camera.update();
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}