package com.gjnm17;

public class Sprites {
	
	public static Sprite playerSprite;
	
	public static Sprite circleSprite;
	
	
	public static void createSprites() {
		playerSprite = new Sprite();
		playerSprite.addFrame(Assets.spritesheet40[1][0]);
	
		
		circleSprite = new Sprite();
		circleSprite.addFrame(Assets.circle);
	}
}
