package com.CIS4914.Blocked.Screens;

import java.util.ArrayList;

import com.CIS4914.Blocked.Blocked;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
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
	
	public GameScreen(Level selectedLevel, Blocked game) {
		this.game = game;
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
		}
		
		for (Entity ent1 : entities) {
			for (Entity ent2 : entities) {
				
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
		float newX = ent.getX() + ent.vel.x * delta;
		float newY = ent.getY() + ent.vel.y * delta;
		newX = MathUtils.clamp(newX, 0 - ent.getHitBox().getX(), 9000);
		newY = MathUtils.clamp(newY, 0, 1080);
		ent.setX(newX);
		ent.setY(newY);
		ent.vel.x = ent.vel.x + ent.accel.x * delta / 2f;
		ent.vel.y = ent.vel.y + ent.accel.y * delta / 2f;
		ent.vel.x = MathUtils.clamp(ent.vel.x, -maxSpeed, maxSpeed);
		if (ent.getY() == 0)
			ent.vel.y = 0;
	}
	
	public void updateCamera() {
		if (player.getY() > stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = player.getY();
		if (player.getY() <= stage.getCamera().viewportHeight / 2)
			stage.getCamera().position.y = stage.getCamera().viewportHeight / 2;
		stage.getCamera().position.x = MathUtils.clamp(stage.getCamera().position.x, 
				stage.getCamera().viewportWidth / 2, 
				9000 - stage.getCamera().position.x);
		
		stage.getCamera().position.y = MathUtils.clamp(stage.getCamera().position.y,
														0, 540);
		stage.getCamera().update();
		// ((OrthographicCamera) stage.getCamera()).translate(5,0);
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

		player = new Player(new Rectangle(160, 0, 180, 196), new Rectangle(0, 700, 196, 196), Blocked.manager.get("generic.png", Texture.class));
		
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