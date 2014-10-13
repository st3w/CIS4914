package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

// Character class for character entities in game
public class Character extends Entity {

	// The constructor (name, Rectangle obj, Texture obj)
	public Character(Rectangle objBound, TextureRegion objTex) {
		super(objBound, objTex);
	}	
}
