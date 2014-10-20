package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

// Block class for block entities in game
public class Block extends Entity
{
	// Parameters
	private String type;
	
	// The constructor (name, Rectangle obj, Texture obj, type)
	public Block(Rectangle objBound, Texture objTex, String type) 
	{
		super(objBound, new TextureRegion(objTex));
		this.type = type;
	}
	
	public void draw(Batch batch, float alpha){
		batch.draw(tex, getX(), getY(),  getWidth(), getHeight());
	}
	
	// Gets type
	public String getType()
	{
		return this.type;
	}
	
	// Sets type
	public void setType(String newType)
	{
		this.type = newType;
	}

}
