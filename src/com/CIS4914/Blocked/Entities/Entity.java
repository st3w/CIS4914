package com.CIS4914.Blocked.Entities;

import com.CIS4914.Blocked.Blocked;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

// StaticEntity is just an Actor with a TextureRegions
public class Entity extends Actor {

	TextureRegion tex;
	public Vector2 vel;
	public Vector2 accel;
	Rectangle hitBox; 
	boolean isMovable;
	
	public Entity(Rectangle hitBox, Rectangle texBounds, TextureRegion tex, boolean isMovable) {
		this.tex = tex;
		
		setWidth(texBounds.width);
		setHeight(texBounds.height);
		super.setX(texBounds.x + hitBox.width - hitBox.x);
		super.setY(texBounds.y + hitBox.y);
		
		// Fix the hitbox coordinates
		this.hitBox = hitBox;
		this.hitBox.setX(texBounds.x);
		this.hitBox.setY(texBounds.y);
		
		this.isMovable = isMovable;
		
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public Entity(Rectangle texBounds, TextureRegion tex, boolean isMovable) {
		this.tex = tex;
		this.hitBox = texBounds;
		setWidth(texBounds.width);
		setHeight(texBounds.height);
		setBounds(texBounds.x, texBounds.y, texBounds.width, texBounds.height);
		this.isMovable = isMovable;
		vel = new Vector2();
		accel = new Vector2();
	}

	public Entity(Rectangle texBounds, TextureRegion tex) {
		this.tex = tex;
		this.hitBox = texBounds;
		setWidth(texBounds.width);
		setHeight(texBounds.height);
		//setBounds(texBounds.x, texBounds.y, texBounds.width, texBounds.height);
		isMovable = false;
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public Entity(TextureRegion tex) {
		this.tex = tex;
		this.hitBox = new Rectangle(0, 0, 64, 64);
		setWidth(64);
		setHeight(64);
		setBounds(0, 0, 64, 64);
		isMovable = false;
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public Entity() {
		tex = new TextureRegion(Blocked.manager.get("generic.png", Texture.class));
		setWidth(64);
		setHeight(64);
		setBounds(0, 0, 64, 64);
		isMovable = false;
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public boolean collides(Entity ent2, Rectangle collisionRectangle) {
		if (ent2.getX() < getX() + getWidth()) {
			return (Intersector.intersectRectangles(getHitBox(), ent2.getHitBox(), collisionRectangle));
		}
		
		return false;
	}
	
	public void resolveX(Entity other, Rectangle collisionRectangle) {
		if (getX() + getWidth() < other.getX() + other.getWidth()) {
			setX(getX() - collisionRectangle.width - .01f);
			vel.x = 0;
			accel.x = 0;
		} else {
			setX(other.getX() + other.getWidth() + .01f);
			vel.x = 0;
			accel.x = 0;
		}
	}
	
	public void resolveY(Entity other, Rectangle collisionRectangle) {
		if (getY() > other.getY()) {
			setY(getY() + collisionRectangle.height + .01f);
			vel.y = 0;
			accel.y = 0;
		} else {
			setY(other.getY() - getHeight() - .01f);
			vel.y = 0;
			accel.y = 0;
		}
	}
	
	public boolean isMovable() { return this.isMovable; }
	
	public void setIsMovable(boolean value) {
		isMovable = value;
	}
	
	public Rectangle getHitBox() { 
		return hitBox;
	}
	
	@Override
	public void setX(float x) {
		float tempX = getX();
		hitBox.setX(x);
		super.setX(x - (tempX - super.getX()));
	}
	
	@Override
	public void setY(float y) {
		float tempY = getY();
		hitBox.setY(y);
		super.setY(y - (tempY - super.getY()));
	}
	
	@Override
	public float getX() { 
		return hitBox.getX();
	}
	
	@Override
	public float getY() {
		return hitBox.getY();
	}
	
	@Override
	public float getWidth() {
		return hitBox.width;
	}
	
	@Override
	public float getHeight() {
		return hitBox.height;
	}
	
	public float getTextureWidth() {
		return super.getWidth();
	}
	
	public float getTextureHeight() {
		return super.getHeight();
	}
	
	public float getTextureX() {
		return super.getX();
	}
	
	public float getTextureY() {
		return super.getY();
	}
}