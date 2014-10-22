package com.CIS4914.Blocked.Screens;

import java.util.ArrayList;

import com.CIS4914.Blocked.Blocked;
import com.CIS4914.Blocked.Entities.Block;
import com.CIS4914.Blocked.Entities.Entity;
import com.CIS4914.Blocked.Entities.Level;
import com.CIS4914.Blocked.Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
	private static final int GAME_PLAYING = 0;
	private static final int GAME_PAUSED = 1;
	private static final int GAME_OVER = 2;
	
	private int gameState;
	private Stage stage;
	private Blocked game;
	private Image background;
	private ArrayList<Entity> entities;
	private Player player;
	
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
			updateCamera();
			stage.draw();
			
			shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 1, 0, 1);
			shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
			for (Entity entity : entities)
			{
				if(entity instanceof Player)
					continue;
				shapeRenderer.setColor(0, 0, 1, 1);
				shapeRenderer.rect(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
			}
			shapeRenderer.end();
			
		}
	}

	public void updateWorld(float delta)
	{
		// Update each DynamicEntity's position
		// If the math doesn't make sense, see 
		// http://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html
		for (Entity ent : entities) {
			if (!ent.isMovable()) 
				continue;
			moveEntity(ent, delta);
			borderCollide(ent);
		}
		
		for (Entity entity : entities) 
		{
			if(collides(player, entity) && !(entity instanceof Player) )
			{
				stop(player);
			}
		}
	}
	
	public void moveEntity(Entity ent, float delta) {
		final float gravity = 4000;
		final float maxSpeed = 1200;
		
		ent.accel.y = -gravity;
		ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
		ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		ent.vel.x = MathUtils.clamp(ent.vel.x, -maxSpeed, maxSpeed);
		ent.setX(ent.getX() + ent.vel.x * delta);
		ent.setY(ent.getY() + ent.vel.y * delta);
		ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
		ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		ent.vel.x = MathUtils.clamp(ent.vel.x, -maxSpeed, maxSpeed);
	}
	
	public void borderCollide(Entity ent) {
		// We only care about
		if (!ent.isMovable())
			return;
		
		// Off the left end of the screen
		if (ent.getX() < 0) {
			ent.setX(0);
			ent.vel.x = 0;
			ent.accel.x = 0;
		}
		
		if (ent.getY() < 0) {
			ent.setY(0);
			ent.vel.y = 0;
			ent.accel.y = 0;
		}
		
		// newX = MathUtils.clamp(newX, 0 - ent.getHitBox().getX(), selectedLevel.getWidth() * 90);
		// newY = MathUtils.clamp(newY, 0, 1080 - ent.getHitBox().height);
		
		
		//if (ent.getHitBox().x + ent.getHitBox().getWidth() > selectedLevel.getWidth() * 90) {
		//	ent.setX(ent.getX() - )
	}
	
	public void updateCamera() {
		if (player.getY() > stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = player.getY();
		if (player.getY() <= stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = stage.getCamera().viewportHeight / 2;
		
		stage.getCamera().position.x = (2 * player.getHitBox().x - player.getHitBox().width) / 2;
		stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, 
													stage.getCamera().viewportWidth / 2, 
													selectedLevel.getWidth() * 90 - stage.getCamera().viewportWidth / 2);
		
		stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y, 0, 1080-stage.getCamera().viewportHeight/2);
		stage.getCamera().update();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage = new Stage(new ExtendViewport(1080, 880, 2560, 880));
		
		Texture backgroundTex = Blocked.manager.get("game_background.jpg", Texture.class);
		backgroundTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		TextureRegion backgroundRegion = new TextureRegion(backgroundTex, 0, 0, 9000, 1080);
		background = new Image(backgroundRegion);
		stage.addActor(background);

		player = new Player(new Rectangle(113, 0, 30, 186), new Rectangle(0, 700, 196, 196), Blocked.manager.get("generic.png", Texture.class));
		
		entities = new ArrayList<Entity>();
		entities.add(player);
		stage.addActor(player);
		Gdx.input.setInputProcessor(stage);
		
		stage.setKeyboardFocus(player);
		player.addListener(new InputListener() {
			public boolean keyDown(InputEvent input, int keycode) {
				if (keycode == Input.Keys.LEFT)
					player.isLeftButtonDown = true;
				else if (keycode == Input.Keys.RIGHT)
					player.isRightButtonDown = true;
				else if (keycode == Input.Keys.UP)
					player.isJumpButtonDown = true;
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
		
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < selectedLevel.getWidth(); j++){
				if(selectedLevel.getGrid(j,i) == 1){
					Block immovableBlock = new Block(new Rectangle(90 * j, 1080 - (90 * (i + 1)), 90, 90), Blocked.manager.get("bricks/brick.png", Texture.class), "Immovable Block");
					stage.addActor(immovableBlock);
					entities.add(immovableBlock);
				}
			}
		}
		gameState = GAME_PLAYING;
	}
	
	public boolean collides(Player player,  Entity entity)
	{
		if(entity.getHitBox().getX() < player.getHitBox().getX() + player.getHitBox().getWidth())
		{
			return (Intersector.overlaps(player.getHitBox(), entity.getHitBox() ));
		}
		
		return false;
	}
	
	public void stop(Entity entity)
	{
		entity.vel.x=0;
		entity.vel.y=0;
		entity.accel.x=0;
		entity.accel.y=0;
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