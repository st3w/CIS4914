package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//Entity class for objects in game
public class Entity extends Actor
{
	Rectangle bounds; // Bounding rectangle, relative to the position
	Vector2 position; // Absolute position of bottom left in world coordinates
	TextureRegion texReg;
	
	// Default Constructor for Entity class
	public Entity(Rectangle objBound, TextureRegion objTex)
	{
		this.bounds = objBound;
		this.texReg = objTex;
		this.position = new Vector2();
	}
	
	// get x position of obj 
	public float getX()
	{
		return bounds.getX();
	}
	
	// get y position of obj
	public float getY()
	{
		return bounds.getY();
	}
	
	// sets position of grid x & y for Rectangle obj
	public void setPos(int x, int y)
	{
		this.bounds.setPosition(x, y);
	}
	
	public void setBounds(Rectangle obj)
	{
		this.bounds = obj;
	}

}
