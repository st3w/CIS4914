package com.CIS4914.Blocked.Entities;

import com.CIS4914.Blocked.Blocked;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
		this.hitBox = hitBox;
		this.hitBox.setX(hitBox.getX() + getX());
		this.hitBox.setY(hitBox.getY() + getY());
		setWidth(texBounds.width);
		setHeight(texBounds.height);
		setBounds(texBounds.x, texBounds.y, texBounds.width, texBounds.height);
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
		setBounds(texBounds.x, texBounds.y, texBounds.width, texBounds.height);
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
	
	public boolean isMovable() { return this.isMovable; }
	
	public Rectangle getHitBox() { 
		return hitBox;
	}
	
	@Override
	public void setX(float x) {
		hitBox.setX(x + hitBox.getX() - getX());
		super.setX(x);
	}
	
	@Override
	public void setY(float y) {
		hitBox.setY(y + hitBox.getY() - getY());
		super.setY(y);
	}
}