package com.CIS4914.Blocked.Screens;

import java.util.ArrayList;
import java.util.HashMap;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Block;
import com.CIS4914.Blocked.Entities.Entity;
import com.CIS4914.Blocked.Entities.Level;
import com.CIS4914.Blocked.Entities.Player;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen implements Screen {
	private static final int GAME_PLAYING = 0;
	private static final int GAME_PAUSED = 1;
	private static final int GAME_OVER = 2;
	
	private int gameState;
	private Stage stage, UIStage, fadeStage;
	private Blocked game;
	private Image background;
	private ArrayList<Entity> entities;
	private Player player;
	
	private TextButtonStyle buttonStyle;
	private TextButton2 mainMenu, moveLeft, moveRight, moveJump;
	private ImageButton leftButton, rightButton;
	private BitmapFont defaultFont;
	Skin skin;
	TextureAtlas textures;
	
	InputMultiplexer input = new InputMultiplexer();
	SpriteBatch batch;
	
	private Level selectedLevel;
	
	private ShapeRenderer shapeRenderer;
	
	public GameScreen(Level selectedLevel, Blocked game) {
		this.game = game;
		this.selectedLevel = selectedLevel;
		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (gameState) {
		case GAME_PLAYING:
			stage.act(delta);
			updateWorld2(delta);

			fadeStage.act(Gdx.graphics.getDeltaTime());
			updateCamera();
			
			batch.begin();
			stage.draw();
			UIStage.draw();
			fadeStage.draw();
			batch.end();

			// Developer Collision Edges
			/*
			shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 0, 1, 1);
			shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
			for (Entity entity : entities)
			{
				if (entity instanceof Player)
					continue;
				shapeRenderer.setColor(1, 1, 0, 1);
				shapeRenderer.rect(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
			}
			shapeRenderer.end();
			*/
		}
	}

	// Update each dynamic entity's position
	public void updateWorld(float delta)
	{
		Rectangle collisionRectangle = new Rectangle();
		
		for (Entity ent1 : entities) {
			if (!ent1.isMovable()) 
				continue;
			moveEntity(ent1, delta);
			
			for (Entity ent2 : entities) {
				if (ent1.equals(ent2))
					continue;
				
				if ((!ent2.isMovable()) && ent1.collides(ent2, collisionRectangle)) {
					if (collisionRectangle.width < collisionRectangle.height) {
						ent1.resolveX(ent2, collisionRectangle);
					} else {
						if (ent1 instanceof Player)
							player.isJumpButtonDown = false;
						ent1.resolveY(ent2, collisionRectangle);
					}
				}
			}
			
			borderCollide(ent1);
		}
	}
	
	// Need to resolve jitter jatter 
	public void updateWorld2(float delta) {
		Rectangle[] collisionRectangles = new Rectangle[5];
		for (int i = 0; i < 5; i++) {
			collisionRectangles[i] = new Rectangle();
		}
		HashMap<Rectangle, Entity> map = new HashMap<Rectangle, Entity>();
		
		for (Entity ent1 : entities) {
			if (!ent1.isMovable()) 
				continue;
			moveEntity(ent1,  delta);
			
			int i = 0;
			for (Entity ent2 : entities) {
				if (ent1.equals(ent2))
					continue;
				
				if ((!ent2.isMovable()) && ent1.collides(ent2, collisionRectangles[i])) {
					map.put(collisionRectangles[i], ent2);
					i++;
				}
			}
			
			for (int j = 0; j < i; j++) {
				Rectangle maxRect = getMaxRect(collisionRectangles);
				Entity entity = map.get(maxRect);
				
				if (maxRect == null)
					continue;
				
				if (maxRect.width < maxRect.height) {
					ent1.resolveX(entity, maxRect);
				} else {
					if (ent1 instanceof Player)
						player.isJumpButtonDown = false;
					ent1.resolveY(entity, maxRect);
				}
				
				for (Rectangle rectangle : collisionRectangles) {
					if (rectangle == null)
						continue;
					if (rectangle.width == 0 && rectangle.height == 0)
						continue;
					if (rectangle.equals(maxRect)) {
						rectangle = null;
						continue;
					}
					if (map.get(rectangle) == null) 
						continue;
					
					// Error lies here
					Entity tempEntity = map.get(rectangle);
					if (!ent1.collides(tempEntity, rectangle)) {
						rectangle.set(0, 0, 0, 0);
					} else {
						map.put(rectangle, tempEntity);
					}
				}
			}
			
			map.clear();
		}
	}
	
	public Rectangle getMaxRect(Rectangle[] rectangles) {
		Rectangle max = null;
		
		for (Rectangle rectangle : rectangles) {
			if (rectangle == null)
				continue;
			if (max == null) {
				max = rectangle;
				continue;
			}
			
			if (rectangle.area() > max.area())
				max = rectangle;
		}
		
		return max;
	}
	
	public void moveEntity(Entity ent, float delta) {
		// If the math doesn't make sense, see 
		// http://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
		ent.accel.y = -Blocked.gravity;
		ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
		ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		ent.vel.x = MathUtils.clamp(ent.vel.x, -Blocked.maxSpeed, Blocked.maxSpeed);
		ent.setX(ent.getX() + ent.vel.x * delta);
		ent.setY(ent.getY() + ent.vel.y * delta);
		ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
		ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		ent.vel.x = MathUtils.clamp(ent.vel.x, -Blocked.maxSpeed, Blocked.maxSpeed);
	}
	
	public void borderCollide(Entity ent) {
		// Off the left end of the screen
		if (ent.getX() < 0) {
			ent.setX(0);
			ent.vel.x = 0;
			ent.accel.x = 0;
		}
		
		// Right edge
		if (ent.getX() + ent.getWidth() > selectedLevel.getWidth() * Blocked.blockSize) {
			ent.setX(selectedLevel.getWidth() * Blocked.blockSize - ent.getWidth());
			ent.vel.x = 0;
			ent.accel.x = 0;
		}
				
		// Off the bottom
		if (ent.getY() < 0) {
			ent.setY(0);
			ent.vel.y = 0;
			ent.accel.y = 0;
		}
		
		// Top
		if (ent.getY() + ent.getHeight() > Blocked.worldHeight) {
			ent.setY(Blocked.worldHeight - ent.getHeight());
		}
	}
	
	public void updateCamera() {
		if (player.getY() > stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = player.getY();
		if (player.getY() <= stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = stage.getCamera().viewportHeight / 2;
		
		stage.getCamera().position.x = (2 * player.getHitBox().x + player.getHitBox().width) / 2;
		stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, 
													stage.getCamera().viewportWidth / 2, 
													selectedLevel.getWidth() * Blocked.blockSize - stage.getCamera().viewportWidth / 2);
		
		stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, 0, Blocked.worldHeight-stage.getCamera().viewportHeight/2);
		stage.getCamera().update();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
		Texture blackTexture = Blocked.manager.get("black.jpg");
		Image blackBackground = new Image(blackTexture);
		blackBackground.setWidth(width);
		blackBackground.setHeight(height);
		fadeStage.addActor(blackBackground);
		fadeStage.addAction(Actions.alpha(0, .5f));
		
		float buttonWidth = width * 0.18f;
		float buttonHeight = width * 0.06f;
		
		defaultFont.setScale(width * 0.00035f);
		
		if (mainMenu != null && mainMenu.hasParent())
			mainMenu.remove();
		
		mainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		moveLeft = new TextButton2("Left", buttonStyle, width * 0.015f, width * 0.015f, buttonWidth, buttonHeight);
		moveRight = new TextButton2("Right", buttonStyle, width - buttonWidth - width * 0.015f, width * 0.015f, buttonWidth, buttonHeight);
		moveJump = new TextButton2("Jump", buttonStyle, width - buttonWidth - width * 0.015f, width * 0.03f + buttonHeight, buttonWidth, buttonHeight);
		
		UIStage.addActor(mainMenu);
		UIStage.addActor(moveLeft);
		UIStage.addActor(moveRight);
		UIStage.addActor(moveJump);
		
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
		stage = new Stage(new ExtendViewport(1080, 880, 2560, 880));
		UIStage = new Stage();
		fadeStage = new Stage();
		
		batch = new SpriteBatch();
		skin = new Skin();
		
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		
		Texture backgroundTex = Blocked.manager.get("game_background.jpg", Texture.class);
		backgroundTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		TextureRegion backgroundRegion = new TextureRegion(backgroundTex, 0, 0, selectedLevel.getWidth() * Blocked.blockSize / 1920, 1);
		background = new Image(backgroundRegion);
		stage.addActor(background);
		
		defaultFont = new BitmapFont(Gdx.files.internal("arial_black_72pt.fnt"), false);
		buttonStyle = new TextButtonStyle(skin.getDrawable("button_up"), skin.getDrawable("button_down"), null, defaultFont);

		player = new Player(new Rectangle(113, 0, 30, 179), new Rectangle(0, 300, 196, 196), Blocked.manager.get("generic.png", Texture.class));
		
		entities = new ArrayList<Entity>();
		entities.add(player);
		stage.addActor(player);
		
		if (Gdx.app.getType() == ApplicationType.Android) {
			
		}
		
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < selectedLevel.getWidth(); j++){
				if(selectedLevel.getGrid(j,i) == 1){
					Block immovableBlock = new Block(new Rectangle(Blocked.blockSize * j, Blocked.worldHeight - (Blocked.blockSize * (i + 1)), Blocked.blockSize, Blocked.blockSize), Blocked.manager.get("bricks/brick.png", Texture.class), "Immovable Block");
					stage.addActor(immovableBlock);
					entities.add(immovableBlock);
				}
			}
		}
		
		input.addProcessor(stage);
		input.addProcessor(UIStage);
		Gdx.input.setInputProcessor(input);
		
		stage.setKeyboardFocus(player);
		
		player.addListener(new InputListener() {
			public boolean keyDown(InputEvent input, int keycode) {
				if (keycode == Input.Keys.LEFT)
					player.isLeftButtonDown = true;
				else if (keycode == Input.Keys.RIGHT)
					player.isRightButtonDown = true;
				else if (keycode == Input.Keys.UP)
					player.isJumpButtonDown = true;
				else if (keycode == Input.Keys.ESCAPE)
					game.setScreen(new GameMenu(game));
				return true;
			}
			public boolean keyUp(InputEvent input, int keycode) {
				if (keycode == Input.Keys.LEFT)
					player.isLeftButtonDown = false;
				else if (keycode == Input.Keys.RIGHT)
					player.isRightButtonDown = false;
				else if (keycode == Input.Keys.UP)
					player.isJumpButtonDown = false;
				return true;
			}
		});
		
		gameState = GAME_PLAYING;
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
		//gameState = GAME_PAUSED;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}
}