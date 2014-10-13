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
	boolean isMovable;
	
	public Entity(Rectangle bounds, TextureRegion tex, boolean isMovable) {
		this.tex = tex;
		setWidth(bounds.width);
		setHeight(bounds.height);
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		this.isMovable = isMovable;
		vel = new Vector2();
		accel = new Vector2();
	}

	public Entity(Rectangle bounds, TextureRegion tex) {
		this.tex = tex;
		setWidth(bounds.width);
		setHeight(bounds.height);
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		isMovable = false;
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public Entity(TextureRegion tex) {
		this.tex = tex;
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
}