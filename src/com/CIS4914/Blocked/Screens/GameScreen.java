package com.CIS4914.Blocked.Screens;

import java.util.ArrayList;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Controllers.Button2;
import com.CIS4914.Blocked.Controllers.TextButton2;
import com.CIS4914.Blocked.Entities.Block;
import com.CIS4914.Blocked.Entities.Entity;
import com.CIS4914.Blocked.Entities.Level;
import com.CIS4914.Blocked.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class GameScreen implements Screen {
	private static final int GAME_PLAYING = 0;
	private static final int GAME_OVER = 1;
	private static final int GAME_WON = 2;
	
	private int gameState;
	private Stage stage, UIStage, fadeStage, winStage, loseStage;
	private Blocked game;
	private Image background;
	private ArrayList<Entity> entities;
	private Player player;
	
	Image blackBackground;
	private TextButtonStyle buttonStyle;
	private TextButton2 mainMenu, winMainMenu, loseMainMenu;
	private LabelStyle labelStyle;
	private Label winLabel, loseLabel;
	Button moveLeft, moveRight, moveJump;
	
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
			updateWorld(delta);

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
			break;
		case GAME_OVER:
			stage.act(delta);

			fadeStage.act(Gdx.graphics.getDeltaTime());
			updateCamera();
			
			batch.begin();
			stage.draw();
			UIStage.draw();
			fadeStage.draw();
			loseStage.draw();
			batch.end();
			break;
		case GAME_WON:
			stage.act(delta);

			fadeStage.act(Gdx.graphics.getDeltaTime());
			updateCamera();
			
			batch.begin();
			stage.draw();
			UIStage.draw();
			fadeStage.draw();
			winStage.draw();
			batch.end();
			break;
		}
	}
	
	public void updateWorld(float delta) {
		delta = MathUtils.clamp(delta, 1/60f, 1/10f);
		Rectangle collisionRectangle = new Rectangle();
		player.isOnGround = false;
		
		for (Entity ent1 : entities) {
			if (!ent1.isMovable()) 
				continue;
			
			// Move entity in the y-axis first to avoid sticking when moving
			// across adjacent tiles
			moveEntity(ent1, delta, false, true);
			
			for (Entity ent2 : entities) {
				if (ent1.equals(ent2))
					continue;
				
				// If ent1 collides with a static object
				if (ent1.collides(ent2, collisionRectangle)) {
					if (ent1 instanceof Player) {
						player.isJumpButtonDown = false;
						if (ent1.getY() > ent2.getY())
							player.isOnGround = true;
					}
					ent1.resolveY(ent2, collisionRectangle);
				}
			}
			
			// Move entity in the x-axis and resolve collisions
			moveEntity(ent1, delta, true, false);
			
			for (Entity ent2 : entities) {
				if (ent1.equals(ent2))
					continue;
				
				// If ent1 collides with a static object
				if (ent1.collides(ent2, collisionRectangle)) {
						ent1.resolveX(ent2, collisionRectangle);
				} 
			}
			
			borderCollide(ent1);
		}
	}
	
	// If the math doesn't make sense, see 
	// http://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
	public void moveEntity(Entity ent, float delta, boolean moveX, boolean moveY) {
		if (moveY) {
			ent.accel.y = -Blocked.gravity;
			ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
			ent.setY(ent.getY() + ent.vel.y * delta);
			ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		}
		if (moveX) {
			ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
			ent.vel.x = MathUtils.clamp(ent.vel.x, -Blocked.maxSpeed, Blocked.maxSpeed);
			ent.setX(ent.getX() + ent.vel.x * delta);
			ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
			ent.vel.x = MathUtils.clamp(ent.vel.x, -Blocked.maxSpeed, Blocked.maxSpeed);
		}
	}
	
	public void borderCollide(Entity ent) {
		// Off Bottom (DEATH)
		if(ent.getY() <= 0 && ent instanceof Player){
			gameState = GAME_OVER;
			Gdx.input.setInputProcessor(loseStage);
			fadeStage.addAction(Actions.alpha(.7f, .5f));
			System.out.println("Lose");
		}
		
		// To 2nd to last block (Win State)
		if(ent.getX() + ent.getWidth() > (selectedLevel.getWidth() - 2) * Blocked.blockSize && ent instanceof Player){
			gameState = GAME_WON;
			Gdx.input.setInputProcessor(winStage);
			fadeStage.addAction(Actions.alpha(.7f, .5f));
			System.out.println("Win");
		}
		
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
		
		blackBackground.setWidth(width);
		blackBackground.setHeight(height);
		
		float buttonWidth = width * 0.18f;
		float buttonHeight = width * 0.06f;
		
		defaultFont.setScale(width * 0.00035f);
		
		mainMenu.setBounds(width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		moveLeft.setBounds(width * 0.015f, width * 0.015f, buttonWidth * 1.2f, buttonHeight * 1.7f);
		moveRight.setBounds(width - buttonWidth * 1.2f - width * 0.015f, width * 0.015f, buttonWidth * 1.2f, buttonHeight * 1.7f);
		moveJump.setBounds(width - buttonWidth - width * 0.015f, width * 0.07f + buttonHeight, buttonWidth, buttonWidth * 0.6f);
	}

	@Override
	public void show() {
		System.out.println(Gdx.files.getLocalStoragePath());
		stage = new Stage(new ExtendViewport(1080, 880, 2560, 880));
		UIStage = new Stage();
		fadeStage = new Stage();
		winStage = new Stage();
		loseStage = new Stage();
		
		batch = new SpriteBatch();
		skin = new Skin();
		
		textures = new TextureAtlas("textures.atlas");
		skin = new Skin();
		skin.addRegions(textures);
		
		Texture backgroundTex = Blocked.manager.get("game_background.jpg", Texture.class);
		backgroundTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		TextureRegion backgroundRegion = new TextureRegion(backgroundTex, 0, 0, selectedLevel.getWidth() * Blocked.blockSize, Blocked.worldHeight);
		background = new Image(backgroundRegion);
		stage.addActor(background);
		
		defaultFont = new BitmapFont(Gdx.files.internal("arial_black_72pt.fnt"), false);
		buttonStyle = new TextButtonStyle(skin.getDrawable("button_up"), skin.getDrawable("button_down"), null, defaultFont);

		player = new Player(new Rectangle(113, 0, 30, 179), new Rectangle(0, 300, 196, 196), Blocked.manager.get("generic.png", Texture.class));
		
		entities = new ArrayList<Entity>();
		entities.add(player);
		stage.addActor(player);
		
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		blackBackground = new Image((Texture) (Blocked.manager.get("black.jpg")));
		blackBackground.setWidth(width);
		blackBackground.setHeight(height);
		fadeStage.addActor(blackBackground);
		fadeStage.addAction(Actions.alpha(0, .5f));
		
		float buttonWidth = width * 0.18f;
		float buttonHeight = width * 0.06f; // wtf?
		
		mainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.015f, height - buttonHeight - width * 0.015f, buttonWidth, buttonHeight);
		moveLeft = new Button2(skin.getDrawable("left_arrow"), skin.getDrawable("left_arrow_down"), width * 0.015f, width * 0.015f, buttonWidth * 1.2f, buttonHeight * 1.7f);
		moveRight = new Button2(skin.getDrawable("right_arrow"), skin.getDrawable("right_arrow_down"), width - buttonWidth * 1.2f - width * 0.015f, width * 0.015f, buttonWidth * 1.2f, buttonHeight * 1.7f);
		moveJump = new Button2(skin.getDrawable("jump_button"), skin.getDrawable("jump_button_down"), width - buttonWidth - width * 0.015f, width * 0.07f + buttonHeight, buttonWidth, buttonWidth * 0.6f);
		
		UIStage.addActor(mainMenu);
		UIStage.addActor(moveLeft);
		UIStage.addActor(moveRight);
		UIStage.addActor(moveJump);
		
		labelStyle = new LabelStyle(defaultFont, Color.WHITE);
		
		winMainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.5f - buttonWidth * 0.5f, height * 0.4f, buttonWidth, buttonHeight);
		winLabel = new Label("Stage Complete", labelStyle);
		winLabel.setAlignment(Align.center);
		winLabel.setX(width * 0.5f - winLabel.getWidth() * 0.5f);
		winLabel.setY(height * 0.55f);
		
		winStage.addActor(winMainMenu);
		winStage.addActor(winLabel);
		
		loseMainMenu = new TextButton2("Main Menu", buttonStyle, width * 0.5f - buttonWidth * 0.5f, height * 0.4f, buttonWidth, buttonHeight);
		loseLabel = new Label("Game Over", labelStyle);
		loseLabel.setAlignment(Align.center);
		loseLabel.setX(width * 0.5f - loseLabel.getWidth() * 0.5f);
		loseLabel.setY(height * 0.55f);
		
		loseStage.addActor(loseMainMenu);
		loseStage.addActor(loseLabel);
		
		
		mainMenu.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenu(game));
			}
		});
		
		winMainMenu.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenu(game));
			}
		});
		
		loseMainMenu.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenu(game));
			}
		});
		
		moveLeft.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.isLeftButtonDown = true;
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.isLeftButtonDown = false;
			}
		});
		
		moveRight.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.isRightButtonDown = true;
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.isRightButtonDown = false;
			}
		});
		
		moveJump.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				player.isJumpButtonDown = true;
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				player.isJumpButtonDown = false;
			}
		});
		
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < selectedLevel.getWidth(); j++){
				if(selectedLevel.getGrid(j,i) == 1){
					Block immovableBlock = new Block(new Rectangle(Blocked.blockSize * j, Blocked.worldHeight - (Blocked.blockSize * (i + 1)), Blocked.blockSize, Blocked.blockSize), Blocked.manager.get("bricks/brick.png", Texture.class), "Immovable Block");
					stage.addActor(immovableBlock);
					entities.add(immovableBlock);
				}
				if (selectedLevel.getGrid(j, i) == 2) {
					Block movableBlock = new Block(new Rectangle(Blocked.blockSize * j, Blocked.worldHeight - (Blocked.blockSize * (i + 1)), Blocked.blockSize, Blocked.blockSize), Blocked.manager.get("bricks/brick.png", Texture.class), "Movable Block");
					movableBlock.setIsMovable(true);
					stage.addActor(movableBlock);
					entities.add(movableBlock);
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