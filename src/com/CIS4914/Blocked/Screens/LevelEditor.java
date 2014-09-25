package com.CIS4914.Blocked.Screens;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class LevelEditor implements Screen 
{
	//Base Variables
	private Blocked game;
	private Level currentLevel;
	private SpriteBatch batch;
	private Stage stage;
	//Texture Variables
	private Skin skin;
	private BitmapFont defaultFont;
	private TextureAtlas textures;
	//Button Variables
	private TextButtonStyle buttonStyle;
	private TextButton2 pause, save;
	//Reference Resolution Identifier Variables
	float screenHeight, screenWidth;
	
	static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    OrthographicCamera cam;

    Sprite mapSprite;
    private float rotationSpeed;
	
	public LevelEditor(Level selectedLevel, Blocked game)
	{
		currentLevel = selectedLevel;
		this.game = game;
	}

	@Override
	public void render(float delta) {
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
	
		batch.begin();
        mapSprite.draw(batch);
		batch.end();
		
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
		buttonStyle.up = skin.getDrawable("button_up");
		buttonStyle.down = skin.getDrawable("button_down");
		buttonStyle.font = defaultFont;
		
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		
		float buttonWidth = screenWidth/4;
		float buttonHeight = screenHeight/4;
		
		//String test = currentLevel.getName();
		
		pause = new TextButton2("Pause", buttonStyle, screenWidth*0.15f - buttonWidth/2, screenHeight/2 + screenHeight/4 + 60, buttonWidth/2, buttonHeight/2);
		save = new TextButton2("Save", buttonStyle, screenWidth*0.29f - buttonWidth/2, screenHeight/2 + screenHeight/4 + 60, buttonWidth/2, buttonHeight/2);
		stage.addActor(pause);
		stage.addActor(save);
		
	}

	@Override
	public void show() {
		
		//batch = new SpriteBatch();
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		defaultFont = new BitmapFont();	
		
		rotationSpeed = 0.5f;

        mapSprite = new Sprite(new Texture(Gdx.files.internal("main_menu_background.png")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        
        //float width = Gdx.graphics.getWidth();
        //float height = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(100, 100);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
		
        batch = new SpriteBatch();
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
