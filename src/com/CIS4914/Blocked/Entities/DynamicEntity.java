package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class DynamicEntity extends StaticEntity {
	public Vector2 vel;
	public Vector2 accel;
	
	public DynamicEntity(Rectangle bounds, TextureRegion tex) {
		super(bounds, tex);
		vel = new Vector2();
		accel = new Vector2();
	}
	
	public DynamicEntity(TextureRegion tex) {
		super(tex);
		vel = new Vector2();
		accel = new Vector2();
	}

}
