package com.gjnm17;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gjnm17.controllers.GameController;
import com.gjnm17.controllers.KeyBoardMouseController;
import com.gjnm17.controllers.Xbox360Controller;
import com.gjnm17.controllers.GameController.Key;
import com.gjnm17.entities.Entity;
import com.gjnm17.entities.Place;
import com.gjnm17.entities.Ship;

public class GameScreen extends ScreenAdapter {
	Main main;
	
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public OrthographicCamera camera;
	public Viewport viewport;	
	
	public float t, game_delay;
	public Level level;
	
	public ArrayList<GameController> controllers = new ArrayList<GameController>();
	
	public static final int hw = 400,hh = 200;
	public static final Rectangle[] playerHudSpaces = {
			new Rectangle(10,				Main.HEIGHT-10-hh,	hw,hh),
			new Rectangle(Main.WIDTH-10-hw,	Main.HEIGHT-10-hh,	hw,hh),
			new Rectangle(10,				10,					hw,hh),
			new Rectangle(Main.WIDTH-10-hw,	10,					hw,hh)
	};
	public static final Rectangle[] playerMessageSpaces = {
			new Rectangle(10,				Main.HEIGHT-10-hh-10-hh/3,	hw,hh/3),
			new Rectangle(Main.WIDTH-10-hw,	Main.HEIGHT-10-hh-10-hh/3,	hw,hh/3),
			new Rectangle(10,				10+hh+10,					hw,hh/3),
			new Rectangle(Main.WIDTH-10-hw,	10+hh+10,					hw,hh/3)
	};
	 
	public enum State {	MENU, PLAY, PAUSE };
	public State state;
	
	public GameScreen(Main main) {
		this.main = main;
	}
	
	@Override
	public void show() {		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Main.WIDTH/2, Main.HEIGHT/2);
		viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
		
		t = 0;
		game_delay = 5*60f;
		level = new Level(this);
		
		state = State.MENU;
		
		controllers.add(new KeyBoardMouseController());
		for(Controller c : Controllers.getControllers()) 
			controllers.add(new Xbox360Controller(c));
	}
	
	@Override
	public void render(float delta) {
		
		// Update
		for(GameController c : controllers) c.update();		
		
		if (state != State.PAUSE) t += delta;
		float progress = Util.clamp(t/game_delay,0,1);
		
		switch(state) {
		case MENU: break;
		case PLAY: if (progress < 1f) level.update(delta); break;
		case PAUSE:	break;	
		}
		
		for(GameController c : controllers) {
			if (c == null) continue;
			
			switch(state) {
			case MENU:
				if (c.getKeyPressed(Key.START)) {
					state = State.PLAY;
					t = 0;
					for(int i = 0; i < 4; i++)
						level.createPlayer((i > controllers.size()-1) ? null : controllers.get(i));
				}
				
						
				if (c.getKeyPressed(Key.UP)) level.game.game_delay += 60;
				if (c.getKeyPressed(Key.DOWN)) level.game.game_delay -= 60;
					
				level.game.game_delay = Math.max(level.game.game_delay, 60);
				
				break;
			case PLAY:
				if (c.getKeyPressed(Key.START)) {
					if (t < game_delay)	state = State.PAUSE;
					else main.start();
				}
				break;		
			case PAUSE:
				if (c.getKeyPressed(Key.START)) state = State.PLAY;
				break;
			}				
		}
		
		// Render
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
			
		// Background
		batch.begin();
			batch.setColor(0.125f,0.125f,1,1);
			batch.draw(Assets.fillTexture,0,0);
		batch.end();
		
		// Tiles
		level.tileRenderer.setView(camera);
		level.tileRenderer.render();
		
		// Grid
		batch.begin();
			batch.setColor(1,1,1,0.3f);
			batch.draw(Assets.grid,0,0);
		batch.end();
		
		// Entities
		batch.begin();		
			level.renderEntities(batch);
		batch.end();		
		
		//Hud		
		BitmapFont font = Assets.font;
		switch(state) {
		case MENU:
			batch.begin();
				font.getData().setScale(8f);
				Util.drawTitle(batch, font, "Império", Main.WIDTH/2, Main.HEIGHT*2/3, Util.clamp(t, 0, 1));
				
				font.getData().setScale(2f);
				font.setColor(1,1,1,Util.clamp(t-2, 0, 1));
				if (t % 1 < 0.5f) 
					Util.drawTextCentered(batch, font, "Prima Start/Enter para jogar!", Main.WIDTH/2, 350);
				
				font.getData().setScale(1.5f);
				font.setColor(1,1,1,Util.clamp(t-3, 0, 1));
				Util.drawTextCentered(batch, font, "Tempo de jogo - " + (int)(game_delay/60) + " min", Main.WIDTH/2, 260);
				Util.drawTextCentered(batch, font, "(Use D-Pad/Page Up-Down para mudar)", Main.WIDTH/2, 200);
				
			batch.end();
			break;
		case PLAY:
		case PAUSE:
			float alpha = Util.clamp(t/3, 0, 1);
			if (t > game_delay) alpha = Util.clamp(1f-(t-game_delay)/3f, 0, 1);
			
			renderer.begin(ShapeType.Line);
				renderer.setColor(1, 1, 1, alpha);
				renderer.circle(900, Main.HEIGHT-280, 80);
			renderer.end();
			
			batch.begin();
					
				font.setColor(1, 1, 1, alpha);
				font.getData().setScale(1);
				Util.drawTextCentered(batch, font, "Europa", 900, Main.HEIGHT-280+100);
			
				
				// Timer
				batch.setColor(0, 0, 0, 0.6f*alpha);
				Util.drawCentered(batch, Assets.rect, Main.WIDTH/2, Main.HEIGHT-55, 300, 100, 0,true,true);
				Util.drawCentered(batch, Assets.rect, Main.WIDTH/2, Main.HEIGHT-130, 110, 50, 0,true,true);
				
				font.getData().setScale(3.5f);
				int year_start = 1499, year_end = 1600;
				int year = (int) Math.floor(year_start + (year_end-year_start) * progress);
				Util.drawTitle(batch, font, year+"", Main.WIDTH/2, Main.HEIGHT-50, alpha);
				
				font.getData().setScale(1f);
				font.setColor(1,1,1,alpha);
				int time = (int) ((1f-progress)*game_delay);
				if (1f-progress > 0.1f || time % 1 < 0.5f || progress == 1f) 
				Util.drawTextCentered(batch, font, String.format("%dm%02ds", time / 60, time % 60), Main.WIDTH/2, Main.HEIGHT-125);
				
				// Player Huds
				for(int i = 0; i < level.players.size(); i++) {
					Player p = level.players.get(i);
					
					Rectangle r = playerHudSpaces[i];
					batch.setColor(0, 0, 0, 0.75f*alpha);
					Util.drawCentered(batch, Assets.rect, r.x, r.y, r.width, r.height, 0,false,false);
					
					
							
					font.getData().setScale(1f);
					font.setColor(Entity.color.set(p.color).mul(1, 1, 1, alpha));
					
					font.draw(batch, p.name, r.x+15, r.y+r.height-15);
					font.draw(batch, p.money+" $", r.x+15, r.y+r.height-55);
					// Ship
					Ship s = p.ship;
					if (s != null && !s.dead) {
						font.draw(batch, "Carga: " + s.haul_value + " $ ("+s.haul_weight+"/"+p.ship_capacity+")", r.x+15, r.y+r.height-90);
						font.draw(batch, "T.Colonização - " + ((int)Math.round(10f/p.convertion_speed))/10f + "s", r.x+15, r.y+r.height-125);
						font.draw(batch, "Alc. - " + (int)s.health+"/"+(int)p.initial_reach  + " u.d.", r.x+15, r.y+r.height-160);
					} else {
						if (p.controller != null) font.draw(batch, "Nova caravela em " + (int)Math.ceil(p.ship_timer) + "s", r.x+15, r.y+r.height-125);
					}
					
					// Message
					if (p.message != null) {
						Rectangle mr = playerMessageSpaces[i];
						
						Util.layout.setText(font, p.message);
						float w = Util.layout.width+20;
						float h = Util.layout.height+30;
						float x = (p.id % 2 == 0) ? mr.x : mr.x + mr.width - w;
						float y = (p.id >= 2) 	  ? mr.y : mr.y + mr.height - h;
						
						batch.setColor(1, 1, 1, 0.75f*alpha);
						Util.drawCentered(batch, Assets.rect, x, y, w, h, 0,false,false);				
						
						font.setColor(Entity.color.set(p.message_color).mul(1, 1, 1, alpha));
						font.draw(batch, p.message, x+10, y+h-20);
					}
				}
				
				// Pause
				if (state == State.PAUSE) {
					batch.setColor(0, 0, 0, 0.5f);
					batch.draw(Assets.fillTexture,0,0);
					
					font.getData().setScale(4f);
					Util.drawTitle(batch, font, "Pausa", Main.WIDTH/2, Main.HEIGHT*2/3f, 1);
					
					font.getData().setScale(2f);
					font.setColor(Color.WHITE);
					
					Util.drawTextCentered(batch, font, "Prima Start/Espaço para recomeçar", Main.WIDTH/2, 300);
				}
				
				// Score Board
				if (t > game_delay) {
					float salpha = Util.clamp((t-2-game_delay)/5,0,1);
					
					ArrayList<Player> result = new ArrayList<Player>();
					result.addAll(level.players);
					final int[] colony_count = new int[result.size()];
					Arrays.fill(colony_count, 0);
					for(Place p : level.places.values()) if (!p.home && p.owner != null) colony_count[p.owner.id]++;
					
					final int colony_value = 25;
					final float technology_value = 0.5f;
					result.sort(new Comparator<Player>() {
						@Override
						public int compare(Player p1, Player p2) {
							int s1 = p1.money + colony_value*colony_count[p1.id] + (int)(technology_value * p1.technology_investment);
							int s2 = p2.money + colony_value*colony_count[p2.id] + (int)(technology_value * p2.technology_investment);
							return Integer.compare(s2, s1);
						}});
									
					batch.setColor(0, 0, 0, 0.75f*salpha);
					Util.drawCentered(batch, Assets.rect, Main.WIDTH/2, Main.HEIGHT/2, 1280, 600, 0,true,true);
					
					font.setColor(1, 1, 1, salpha);
					font.getData().setScale(2.5f);
					Util.drawTitle(batch, font, "Resultados", Main.WIDTH/2, Main.HEIGHT-300, salpha);
					font.setColor(1, 1, 1, salpha*0.5f);
					font.getData().setScale(0.85f);
					Util.drawTextCentered(batch, font, "(Total = Dinheiro + "+colony_value+"$xNº de Colonias + Investimento em Tecnologiax"+technology_value+")", Main.WIDTH/2, 375);
					
					
					for(int i = 0; i < result.size(); i++) {
						Player p = result.get(i);
						font.getData().setScale(1.25f * (i == 0 ? 1.1f : 1f));
						float g = (i == 0 ? 1 : 0.65f);
						font.setColor(Entity.color.set(p.color).mul(g,g,g,salpha));						
						Util.drawTextCentered(batch, font, 
								p.name + " - " + 
								p.money + "$ + " + 
								colony_value + "$x" + colony_count[p.id] + " + " +
								p.technology_investment + "$x" + technology_value + " = " +
								(p.money + colony_value*colony_count[p.id] + (int)(technology_value * p.technology_investment)) + "$", 
								
								Main.WIDTH/2, Main.HEIGHT-400 - 75*i + (i == 0 && t % 1 < 0.5f ? 10 : 0));
					}
					
					if (t - game_delay > 10 && t % 2 < 1.5f) {
						font.setColor(Color.WHITE);
						font.getData().setScale(1.5f);
						Util.drawTextCentered(batch, font, "Carregar Start/Enter para jogar de novo", Main.WIDTH/2, 300);
					} 
				}
			batch.end();
			break;
		}		
		
		if (Main.DEBUG) {
			renderer.begin(ShapeType.Line);
				level.renderDebug(renderer);
			renderer.end();
			
			batch.begin();
				Assets.font.setColor(Color.BLACK);
				Assets.font.draw(batch,level.entities.size()+"",100,300);
				Ship s = level.players.get(0).ship;
				if (s != null) Assets.font.draw(batch,(int)s.continuous_x+"",100,330);
				Assets.font.draw(batch,(1f-(t-game_delay)/3f)+"",100,360);
			batch.end();
		}
	}
	

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		viewport.update(width, height);
	}

}
