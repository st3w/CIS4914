package com.CIS4914.Blocked.Entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Level {
	
	private String name;
	private Entity[][] grid;
	private Texture background;
	
	// constructor
	public Level(String name, int width){
		this.name = name;
		
		// TEMP VALUES
		Block air = null;
		Block immovableBrick;
		Block movableBrick;
		
		grid = new Entity[20][width];
		
		//background = Gdx.files.internal(FILE_PATH)
		
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < width; j++){
				grid[i][j] = air;
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
	
	// getGrid Method: Returns the entity in the X,Y coordinate
	public Entity getGrid(int x, int y){
		return grid[x][y];
	}
	
	// setGrid Method: Sets the entity in the X,Y coordinate
	public void setGrid(Entity ent, int x, int y){
		grid[x][y] = ent;
	}
	
	
	
	
	
}
