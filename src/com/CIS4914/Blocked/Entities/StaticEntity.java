package com.CIS4914.Blocked.Entities;

import com.CIS4914.Blocked.Blocked;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

// StaticEntity is just an Actor with a TextureRegions
public class StaticEntity extends Actor {

	TextureRegion tex;
	
	public StaticEntity(Rectangle bounds, TextureRegion tex) {
		this.tex = tex;
		setWidth(bounds.width);
		setHeight(bounds.height);
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	public StaticEntity(TextureRegion tex) {
		this.tex = tex;
		setWidth(64);
		setHeight(64);
		setBounds(0, 0, 64, 64);
	}
	
	public StaticEntity() {
		tex = new TextureRegion(Blocked.manager.get("generic.png", Texture.class));
		setWidth(64);
		setHeight(64);
		setBounds(0, 0, 64, 64);
	}
}
