package com.gjnm17.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyBoardMouseController implements GameController {

	@Override
	public float getMoveAxisX() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) return -1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) return 1;
		return 0;
	}


	@Override
	public float getMoveAxisY() {
		if (Gdx.input.isKeyPressed(Input.Keys.S)) return 1;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) return -1;
		return 0;
	}
	


	@Override
	public boolean getAButtonDown() {
		return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
	}
	@Override
	public boolean getXButtonDown() {
		return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
	}


	@Override
	public boolean getStartButtonDown() {
		return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER);
	}

	
	@Override
	public String toString() {
		return "Keyboard Controller";
	}

}
