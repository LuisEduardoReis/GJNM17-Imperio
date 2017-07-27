package com.gjnm17;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture grid, spritesheet;
	public static TextureRegion[][] spritesheet40;
	public static BitmapFont font;
	
	public static TextureRegion rect, circle;
	public static Texture fillTexture;
	public static TextureRegion xbox_controller, keyboardmouse_controller;
	
	public static Music music;
	
	public static Sound[] pickup;
	public static Sound good_pickup;
	public static Sound colonize;
	public static Sound ship_down;
	public static Sound upgrade;
	public static Sound ship_full;
	

	public static void createAssets() {
		
		grid = new Texture(Gdx.files.internal("grid.png"));		
		xbox_controller = new TextureRegion(new Texture(Gdx.files.internal("xbox_controller.png")));
		keyboardmouse_controller = new TextureRegion(new Texture(Gdx.files.internal("keyboardmouse_controller.png")));
			
		int s = Main.SIZE;	
		
		spritesheet = new Texture(Gdx.files.internal("spritesheet.png"));
		spritesheet40 = new TextureRegion[spritesheet.getWidth()/s][spritesheet.getHeight()/s];
		for(int i = 0; i < spritesheet40.length; i++) {
		for(int j = 0; j < spritesheet40[i].length; j++) {
			spritesheet40[i][j] = new TextureRegion(spritesheet, j*s, i*s, s,s);
		}}
		
		circle = spritesheet40[0][1];		
		
		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		
		Pixmap p = new Pixmap(Main.WIDTH, Main.HEIGHT, Format.RGBA8888);
		p.setColor(1, 1, 1, 1);
		p.fill();
		fillTexture = new Texture(p);
		p.dispose();
		
		p = new Pixmap(Main.SIZE, Main.SIZE, Format.RGBA8888);
		p.setColor(1, 1, 1, 1);
		p.fill();
		rect = new TextureRegion(new Texture(p));
		p.dispose();
		
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.wav"));
		
		
		pickup = new Sound[4];
		pickup[0] = Gdx.audio.newSound(Gdx.files.internal("sound/pickup1.wav"));
		pickup[1] = Gdx.audio.newSound(Gdx.files.internal("sound/pickup2.wav"));
		pickup[2] = Gdx.audio.newSound(Gdx.files.internal("sound/pickup3.wav"));
		pickup[3] = Gdx.audio.newSound(Gdx.files.internal("sound/pickup4.wav"));
	
		good_pickup = Gdx.audio.newSound(Gdx.files.internal("sound/pickup.wav"));
		colonize = Gdx.audio.newSound(Gdx.files.internal("sound/colonize.wav"));
		ship_down = Gdx.audio.newSound(Gdx.files.internal("sound/ship_down.wav"));
		upgrade = Gdx.audio.newSound(Gdx.files.internal("sound/upgrade.wav"));		
		ship_full = Gdx.audio.newSound(Gdx.files.internal("sound/ship_full.wav"));
	}

}
