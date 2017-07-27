package com.gjnm17.controllers;

import java.util.HashMap;
import java.util.HashSet;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;

public class Xbox360Controller extends ControllerAdapter implements GameController {

	Controller controller;
	HashMap<Object, Boolean> pressed = new HashMap<Object, Boolean>();
	HashMap<Object, Boolean> down = new HashMap<Object, Boolean>();
	HashSet<Integer> keys = new HashSet<Integer>();
	PovDirection pov = PovDirection.center;
	
	public Xbox360Controller(Controller c) {
		this.controller = c;
		
		c.addListener(this);
		
		keys.add(XBox360Pad.BUTTON_A);
		keys.add(XBox360Pad.BUTTON_X);
		keys.add(XBox360Pad.BUTTON_START);
		keys.add(XBox360Pad.BUTTON_LB);
		keys.add(XBox360Pad.BUTTON_RB);
		
		for(Object o : keys) {
			pressed.put(o, false);
			down.put(o, false);
		}
		
		for(PovDirection p : PovDirection.values()) {
			pressed.put(p, false);
			down.put(p, false);
		}
	}
	
	@Override
	public void update() {	
		for(Object o : keys) {
			boolean v = controller.getButton((Integer) o);
			pressed.put(o, v && !down.get(o));
			down.put(o, v);
		}
		for(PovDirection p : PovDirection.values()) {
			pressed.put(p, p.equals(pov) && !down.get(p));
			down.put(p, p.equals(pov));
		}
	}
	
	@Override
	public float getMoveAxisX() { return controller.getAxis(XBox360Pad.AXIS_LEFT_X); }

	@Override
	public float getMoveAxisY() { return controller.getAxis(XBox360Pad.AXIS_LEFT_Y); }
	
	@Override
	public boolean getKeyDown(Key key) {
		switch(key) {
		case A: return down.get(XBox360Pad.BUTTON_A);
		case INFO: return down.get(XBox360Pad.BUTTON_LB) || down.get(XBox360Pad.BUTTON_RB);
		case START: return down.get(XBox360Pad.BUTTON_START);
		case X: return down.get(XBox360Pad.BUTTON_X);
		case UP: return down.get(PovDirection.north);
		case DOWN: return down.get(PovDirection.south);
		}
		return false;
	}

	@Override
	public boolean getKeyPressed(Key key) {
		switch(key) {
		case A: return pressed.get(XBox360Pad.BUTTON_A);
		case INFO: return pressed.get(XBox360Pad.BUTTON_LB) || pressed.get(XBox360Pad.BUTTON_RB);
		case START: return pressed.get(XBox360Pad.BUTTON_START);
		case X: return pressed.get(XBox360Pad.BUTTON_X);
		case UP: return pressed.get(PovDirection.north);
		case DOWN: return pressed.get(PovDirection.south);
		}
		return false;
	}

	@Override
	public String getKeyName(Key key) {
		switch(key) {
		case A: return "A";
		case INFO: return "LB ou RB";
		case START: return "Start";
		case X: return "X";
		case UP: case DOWN: return "D-Pad para alternar";
		}
		return "<undefined>";		
	}
	
	
	@Override
	public String toString() {
		return controller.toString();
	}

	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		pov = value;		
		return super.povMoved(controller, povIndex, value);
	}




}
