package com.gjnm17.controllers;

public interface GameController {
	
	public enum Key {
		A,X,START,INFO, UP,DOWN
	}
	
	void update();
	
	float getMoveAxisX();
	float getMoveAxisY();

	boolean getKeyDown(Key key);
	boolean getKeyPressed(Key key);
	String getKeyName(Key key);	

}
