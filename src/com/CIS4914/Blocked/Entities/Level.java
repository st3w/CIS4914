package com.CIS4914.Blocked.Entities;

import com.badlogic.gdx.graphics.Texture;

public class Level {
	
	private String name;
	private int[][] grid;
	private Texture background;
	private float levelWidth;
	
	// constructor
	public Level(String name, int width){
		this.name = name;
		levelWidth = width;
		
		grid = new int[width][12];
		
		for(int i = 0; i < 12; i++){
			for(int j = 0; j < width; j++){
				grid[j][i] = 0;
			}
		}
		
	}
	
	// getName Method: Returns the level name
	public String getName(){
		return name;
	}
	
	// setName Method: Sets the level name
	public void setName(String name){
		this.name = name;
	}
	
	// getWidth Method: returns the level width
	public float getWidth(){
		return levelWidth;
	}
	
	// getGrid Method: Returns the entity in the X,Y coordinate
	public int getGrid(int x, int y){
		return grid[x][y];
	}
	
	// setGrid Method: Sets the entity in the X,Y coordinate
	public void setGrid(int blockType, int x, int y){
		grid[x][y] = blockType;
	}
	
	
	
	
	
}
