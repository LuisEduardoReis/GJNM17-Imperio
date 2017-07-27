package com.gjnm17.controllers;

import com.badlogic.gdx.controllers.Controller;

public class Xbox360Controller implements GameController {

	Controller controller;
	
	public Xbox360Controller(Controller c) {
		this.controller = c;
	}


	@Override
	public float getMoveAxisX() { return controller.getAxis(XBox360Pad.AXIS_LEFT_X); }

	@Override
	public float getMoveAxisY() { return controller.getAxis(XBox360Pad.AXIS_LEFT_Y); }
	
	@Override
	public boolean getAButtonDown() { return controller.getButton(XBox360Pad.BUTTON_A); }
	@Override
	public boolean getXButtonDown() { return controller.getButton(XBox360Pad.BUTTON_X); }
	@Override
	public boolean getStartButtonDown() { return controller.getButton(XBox360Pad.BUTTON_START); }
	
	@Override
	public String toString() {
		return controller.toString();
	}

}
