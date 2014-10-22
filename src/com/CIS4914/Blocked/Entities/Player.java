package com.CIS4914.Blocked.Entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Entity {
	// Player states
	static final int STAND_RIGHT = 0;
	static final int STAND_LEFT = 1;
	static final int RUN_RIGHT = 2;
	static final int RUN_LEFT = 3;
	static final int JUMP_RIGHT = 4;
	static final int JUMP_LEFT = 5;
	
	static final int SHEET_COLS = 8;
	static final int SHEET_ROWS = 9;
	
	TextureRegion standFrame;
	TextureRegion[] runFrames; // 8
	TextureRegion[] jumpFrames; // 8
	Animation runAnimation;
	Animation jumpAnimation;
	
	float stateTime;
	int state;
	
	public boolean isLeftButtonDown;
	public boolean isRightButtonDown;
	public boolean isJumpButtonDown;
	
	ShapeRenderer shapeRenderer;
	
	public Player(Rectangle hitBox, Rectangle objBound, Texture playerTex) {
		super(hitBox, objBound, new TextureRegion(playerTex), true);
		TextureRegion[][] frames = TextureRegion.split(playerTex, 
													playerTex.getWidth() / SHEET_COLS,
													playerTex.getHeight() / SHEET_ROWS);
		
		runFrames = new TextureRegion[8];
		for (int i = 0; i < 4; i++)
			runFrames[i] = frames[0][i+4];

		for (int i = 0; i < 4; i++)
			runFrames[i+4] = frames[1][i];
		
		jumpFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++)
			jumpFrames[i] = frames[5][i];
		
		standFrame = new TextureRegion(frames[8][0]);
		runAnimation = new Animation(1/15f, runFrames);
		jumpAnimation = new Animation(1/15f, jumpFrames);
		state = STAND_RIGHT;
		
		isLeftButtonDown = false;
		isRightButtonDown = false;
		isJumpButtonDown = false;
		
		shapeRenderer = new ShapeRenderer();
	}
	
	public void draw(Batch batch, float alpha) {
		switch (state) {
		case STAND_RIGHT:
			batch.draw(standFrame, getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			break;
		default:
			batch.draw(standFrame, getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
		}
	}

	@Override
	public void act(float delta) {
		final float runAccel = 2000;
		float friction = 2500 * delta;
		
		stateTime += delta;
		
		if (isRightButtonDown)
			accel.x = runAccel;
		if (isLeftButtonDown)
			accel.x = -runAccel;
		if (!isLeftButtonDown && !isRightButtonDown) {
			accel.x = 0;
			if (vel.x < friction && vel.x > -friction) {
				vel.x = 0;
			} else {
				if (vel.x > 0)
					vel.x = vel.x - friction;
				else
					vel.x = vel.x + friction;
			}
		}
		if (isJumpButtonDown && vel.y == 0)
			vel.y = 2200;
	}
}