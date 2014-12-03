package com.CIS4914.Blocked.Entities;

import java.util.ArrayList;

import com.CIS4914.Blocked.Blocked;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Entity {
	static final int SHEET_COLS = 8;
	static final int SHEET_ROWS = 9;
	
	TextureRegion standLeftFrame;
	TextureRegion standRightFrame;
	TextureRegion[] runLeftFrames; 
	TextureRegion[] runRightFrames;
	TextureRegion[] jumpLeftFrames;
	TextureRegion[] jumpRightFrames;
	TextureRegion[] walkLeftFrames;
	TextureRegion[] walkRightFrames;
	
	Animation runLeftAnimation;
	Animation runRightAnimation;
	Animation walkLeftAnimation;
	Animation walkRightAnimation;
	
	float stateTime;
	
	public boolean isLeftButtonDown;
	public boolean isRightButtonDown;
	public boolean isJumpButtonDown;
	public boolean isFacingRight;
	public boolean isOnGround;
	
	public Player(Rectangle hitBox, Rectangle objBound, Texture playerTex) {
		super(hitBox, objBound, new TextureRegion(playerTex), true);
		TextureRegion[][] frames = TextureRegion.split(playerTex, 
													playerTex.getWidth() / SHEET_COLS,
													playerTex.getHeight() / SHEET_ROWS);
		
		standRightFrame = new TextureRegion(frames[8][0]);
		standLeftFrame = new TextureRegion(frames[8][0]);
		standLeftFrame.flip(true, false);
		
		runRightFrames = new TextureRegion[8];
		for (int i = 0; i < 4; i++)
			runRightFrames[i] = frames[0][i+4];

		for (int i = 0; i < 4; i++)
			runRightFrames[i+4] = frames[1][i];
		
		runLeftFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++) {
			runLeftFrames[i] = new TextureRegion(runRightFrames[i]);
			runLeftFrames[i].flip(true, false);
		}
		
		jumpRightFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++)
			jumpRightFrames[i] = frames[5][i];
		
		jumpLeftFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++) {
			jumpLeftFrames[i] = new TextureRegion(jumpRightFrames[i]);
			jumpLeftFrames[i].flip(true, false);
		}
		
		walkRightFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++)
			walkRightFrames[i] = frames[4][i];
		
		walkLeftFrames = new TextureRegion[8];
		for (int i = 0; i < 8; i++) {
			walkLeftFrames[i] = new TextureRegion(walkRightFrames[i]);
			walkLeftFrames[i].flip(true, false);
		}	
		
		runRightAnimation = new Animation(1/15f, runRightFrames);
		runLeftAnimation = new Animation(1/15f, runLeftFrames);
		walkRightAnimation = new Animation(1/8f, walkRightFrames);
		walkLeftAnimation = new Animation(1/8f, walkLeftFrames);
		
		isLeftButtonDown = false;
		isRightButtonDown = false;
		isJumpButtonDown = false;
		isFacingRight = true;
		isOnGround = false;
		stateTime = 0;
	}
	
	public void draw(Batch batch, float alpha) {
		if (isOnGround) {
			if (vel.x > 0) {
				if (vel.x > 2 * Blocked.maxSpeed / 3)
					batch.draw(runRightAnimation.getKeyFrame(stateTime, true), 
							getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(walkRightAnimation.getKeyFrame(stateTime, true), 
							getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			} else if (vel.x < 0) {
				if (vel.x < -2 * Blocked.maxSpeed / 3) 
					batch.draw(runLeftAnimation.getKeyFrame(stateTime, true), 
						getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else {
					batch.draw(walkLeftAnimation.getKeyFrame(stateTime, true), 
							getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				}
			} else {
				if (isFacingRight)
					batch.draw(standRightFrame, getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else 
					batch.draw(standLeftFrame, getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
		} else { // jumping
			if (vel.y > Blocked.jumpSpeed / 2) {
				if (isFacingRight)
					batch.draw(jumpRightFrames[3], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(jumpLeftFrames[3], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
			if (vel.y > 0 && vel.y <= Blocked.jumpSpeed / 2) {
				if (isFacingRight)
					batch.draw(jumpRightFrames[4], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(jumpLeftFrames[4], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
			if (vel.y <= 0 && vel.y > -Blocked.jumpSpeed / 2) {
				if (isFacingRight)
					batch.draw(jumpRightFrames[5], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(jumpLeftFrames[5], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
			if (vel.y <= -Blocked.jumpSpeed / 2 && vel.y > -Blocked.jumpSpeed) {
				if (isFacingRight)
					batch.draw(jumpRightFrames[6], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(jumpLeftFrames[6], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
			if (vel.y <= -Blocked.jumpSpeed) {
				if (isFacingRight)
					batch.draw(jumpRightFrames[7], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
				else
					batch.draw(jumpLeftFrames[7], getTextureX(), getTextureY(), getTextureWidth(), getTextureHeight());
			}
		}
	}

	@Override
	public void act(float delta) {
		final float runAccel = 2000;
		float friction = 2500 * delta;
		
		stateTime += delta;
		
		if (isRightButtonDown) {
			accel.x = runAccel;
			isFacingRight = true;
		}
		if (isLeftButtonDown) {
			accel.x = -runAccel;
			isFacingRight = false;
		}
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
		if (isJumpButtonDown && isOnGround)
			vel.y = Blocked.jumpSpeed;
	}
}