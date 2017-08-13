package com.gjnm17.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyBoardMouseController implements GameController {

	
	@Override
	public void update() {}
	
	
	@Override
	public float getMoveAxisX() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) return -1;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) return 1;
		return 0;
	}
	
	@Override
	public float getMoveAxisY() {
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) return 1;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) return -1;
		return 0;
	}
	
	@Override
	public boolean getKeyDown(Key key) {
		switch(key) {
		case A: return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
		case INFO: return Gdx.input.isKeyPressed(Input.Keys.TAB);
		case START: return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER);
		case X: return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
		case UP: return Gdx.input.isKeyPressed(Input.Keys.PAGE_UP);
		case DOWN: return Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN) || Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT);
		}
		return false;
	}

	@Override
	public boolean getKeyPressed(Key key) {
		switch(key) {
		case A: return Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT);
		case INFO: return Gdx.input.isKeyJustPressed(Input.Keys.TAB);
		case START: return Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
		case X: return Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT);
		case UP: return Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP);
		case DOWN: return Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT);
		}
		return false;
	}

	@Override
	public String getKeyName(Key key) {
		switch(key) {
		case A: return "CTRL";
		case INFO: return "TAB";
		case START: return "Espaço";
		case X: return "SHIFT";
		case UP: case DOWN: return "ALT para alternar";
		}
		return "<undefined>";
	}

	@Override
	public String toString() {
		return "Keyboard Controller";
	}





	

}
