package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;

public class Entity 
{
	// Parameters
	private String name;
	Rectangle bounds;
	Texture tex;
	
	// Default Constructor for Entity class
	public Entity(String name, Rectangle objBound, Texture objTex)
	{
		this.name = name;
		this.bounds = objBound;
		this.tex = objTex;	
	}
	
	/* Get & Set methods for class parameters */
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String newName)
	{
		this.name = newName;
	}
	
	// get x position of obj 
	public int getX()
	{
		return (int) bounds.getX();
	}
	
	// get y position of obj
	public int getY()
	{
		return (int) bounds.getY();
	}
	
	// sets position of grid x & y for Rectangle obj
	public void setPos(int x, int y)
	{
		this.bounds.setPosition(x, y);
	}
	
	public Rectangle getBounds()
	{
		return this.bounds;
	}
	
	public void setBounds(Rectangle obj)
	{
		this.bounds = obj;
	}

}
