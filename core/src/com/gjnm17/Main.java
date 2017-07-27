package com.gjnm17;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.gjnm17.GameScreen.State;

public class Main extends Game {
	
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int SIZE = 40;
	
	public static boolean DEBUG = false;
	public static boolean MUSIC = false;
	public static boolean SOUND = true;
	
	@Override
	public void create() {
		Assets.createAssets();
		
		if (MUSIC) Assets.music.play();
		
		start();		
	}

	public void start() {
				
		Sprites.createSprites();
		
		setScreen(new GameScreen(this));		
	}
	
	
	
	@Override
	public void render() {
		super.render();	
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
			if (!Gdx.graphics.isFullscreen()) 
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			else
				Gdx.graphics.setWindowedMode(Main.WIDTH/2,Main.HEIGHT/2);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {start(); ((GameScreen) screen).state = State.PLAY; return;}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {start(); return;}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) ((GameScreen) screen).t =  ((GameScreen) screen).game_delay;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) DEBUG ^= true;
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) { 
			MUSIC ^= true;
			if (!MUSIC && Assets.music.isPlaying()) Assets.music.pause();
			if (MUSIC && !Assets.music.isPlaying()) Assets.music.play();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) SOUND ^= true;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.O)) ((GameScreen) screen).level.players.get(0).getMoney(100, Main.WIDTH/2, Main.HEIGHT/2);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
			for(int i = 0; i < ((GameScreen) screen).level.players.size(); i++) {
				((GameScreen) screen).level.players.get(i).playerMessage("teste teste teste");
			}
		}
	}

	public static void playSound(Sound[] sounds) {
		if (SOUND) Util.randomSound(sounds);
	}
	public static void playSound(Sound sound) {
		if (SOUND) sound.play();
	}


}
