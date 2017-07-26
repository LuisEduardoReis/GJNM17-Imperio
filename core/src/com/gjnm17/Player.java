package com.gjnm17;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.gjnm17.GameScreen.State;
import com.gjnm17.entities.Place;
import com.gjnm17.entities.Ship;
import com.gjnm17.entities.particles.Coin;
import com.gjnm17.entities.particles.Message;

public class Player extends ControllerAdapter {
	
	public static abstract class Upgrade {
		public String name;
		public int cost;
		public Upgrade(String name, int cost) {
			this.name = name;
			this.cost = cost;
		}
		
		public abstract void get(Player p);
	}
	public static Upgrade[] upgrades = {
		new Upgrade("+5 Capacidade", 10) {			
			@Override
			public void get(Player p) {		
				p.ship_capacity+=5;
			}
		},
		new Upgrade("+50 Alcance", 20) {			
			@Override
			public void get(Player p) {		
				p.initial_reach += 50;
				if (p.ship != null) {
					p.ship.full_health = p.initial_reach;
				}
			}
		},
		new Upgrade("+25% Recuperação", 10) {
			@Override
			public void get(Player p) {
				p.heal_speed *= 1.25f;
			}
		},		
		new Upgrade("-25% Tempo de Colonização", 25) {
			@Override
			public void get(Player p) {
				p.convertion_speed /= 0.75f;
			}			
		},
		new Upgrade("+15% Velocidade da Caravela", 15) {
			@Override
			public void get(Player p) {
				p.ship_speed *= 1.15f;
			}			
		}
	};
	
	
	public int id;
	public Controller controller;
	public Color color;
	public Level level;
	public String name;
	public Ship ship;
	public boolean ad,bd,xd,yd,sd,ud,dd;
	
	
	// Stats
	public float heal_speed;
	public float owner_bonus;
	public float initial_reach;
	public float convertion_speed;
	public int ship_capacity;
	public float ship_speed;
	
	public float ship_timer, ship_delay;
	
	public int money;
	public float money_timer, money_delay;
	public Place home;
	
	public int technology_investment;
	
	public String message;
	public Color message_color;
	
	public int upgradeIndex;

	public Player(Level level, Controller controller) {
		this.level = level;
		this.controller = controller;
		
		level.players.add(this);
		if (controller != null) controller.addListener(this);
		
		money = 50;
		money_delay = 2f;
		money_timer = money_delay;
		
		technology_investment = 0;
		
		heal_speed = 10;
		owner_bonus = 3;
		initial_reach = 50;
		convertion_speed = 1f / 30f;
		ship_capacity = 5;
		ship_timer = 0;
		ship_delay = 10;
		ship_speed = 2.5f*Main.SIZE;
	}
	
	public void update(float delta) {
					
		message = null;
		message_color = Color.BLACK;
		
		if (ship == null && controller != null) {
			ship_timer = Util.stepTo(ship_timer, 0, delta);
			
			if (ship_timer == 0) {
				ship = (Ship) new Ship(level,this).setPosition(867 + Util.randomRangef(-4, 4), 764 + Util.randomRangef(-4, 4));
				ship_timer = ship_delay;
			}
		}
		
		if (controller != null) {

			if (controller.getButton(XBox360Pad.BUTTON_A)) {
				// Press A
				if (ad == false) {
					if (ship != null && !ship.dead) {
						// Make Trade
						if (ship.targetPlace != null && ship.targetPlace.trade != null && (ship.targetPlace.owner == null || ship.targetPlace.owner == this)) {
							Good good = ship.targetPlace.trade;
							
							if (good.weight <= ship_capacity - ship.haul_weight) {
								playerMessage("+ "+ship.targetPlace.trade.name);
								
								ship.haul_value += good.value;
								ship.haul_weight += good.weight;
								ship.targetPlace.trade = null;	
								
								Main.playSound(Assets.pickup);
							} else {
								playerMessage("Caravela cheia!");
								
								Main.playSound(Assets.ship_full);
							}							
						}
					}
				}
				ad = true;
			} else
				ad = false;
			
			if (controller.getButton(XBox360Pad.BUTTON_X)) {
				// Press X
				if (xd == false) {
					if (ship != null && !ship.dead) {					
						// Do upgrade
						if (ship.targetPlace != null && ship.targetPlace.home) {
							Upgrade upgrade = Player.upgrades[upgradeIndex];
							if (upgrade.cost <= money) {
								
								playerMessage(upgrade.name);								
								
								upgrade.get(this);
								money -= upgrade.cost;
								
								technology_investment += upgrade.cost;
								
								Main.playSound(Assets.upgrade);
							}
						}
					}
				}
				xd = true;
			} else
				xd = false;
			
		}
	}

	public void playerMessage(String message) {
		Rectangle r = GameScreen.playerHudSpaces[id];
		Message m = new Message(level, message);
		m.setBlend(level.playerColors[id]);
		
		Assets.font.getData().setScale(m.scale_x);
		Util.layout.setText(Assets.font, message);
		
		float w = Util.layout.width;
		float x = id % 2 == 0 ? r.x + w/2 + 20 : r.x + r.width - w/2 - 20;
		float y = id < 2 ? 		(r.y - 250) : (r.y + r.height + 50);
		m.setPosition(x,y);
	}

	public void getMoney(int v, float x, float y) {
		for(int i = 0; i < v; i++) new Coin(level,this).setPosition(x,y);		
	}
	
	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		if (level.game.state == State.MENU) {
			
			if (value == XBox360Pad.BUTTON_DPAD_UP) level.game.game_delay += 60;
			if (value == XBox360Pad.BUTTON_DPAD_DOWN) level.game.game_delay -= 60;
			
			level.game.game_delay = Math.max(level.game.game_delay, 60);
		}	
		
		if (level.game.state == State.PLAY && ship != null && ship.targetPlace != null && ship.targetPlace.home) {
			if (value == XBox360Pad.BUTTON_DPAD_UP) upgradeIndex--;
			if (value == XBox360Pad.BUTTON_DPAD_DOWN) upgradeIndex++;
			
			if (upgradeIndex < 0) upgradeIndex = Player.upgrades.length - 1;
			upgradeIndex %= Player.upgrades.length;
		}
		return super.povMoved(controller, povIndex, value);
	}

	public String getStatsString() {
		/*
		public float heal_speed;
		public float owner_bonus;
		public float initial_health;
		public float convertion_speed;
		public int ship_capacity;
		*/
		return
			"Alcance da Caravela - " + (int)initial_reach + " u.d.\n"+
			"Capacidade da Caravela - " + ship_capacity + " u.p.\n"+
			"Velocidade da Caravela - " + (int)(10*ship_speed*0.25f)/10f + " u.d./s\n"+
			"Tempo para construir nova Caravela - " + (int) ship_delay + "s\n"+
			"Recuperação de alcance - " + (int)heal_speed + " u.d./s\n"+
			"Recuperação de alcance no próprio territorio - " + (int)(heal_speed*owner_bonus) + " u.d./s\n" + 
			"Tempo de colonização - " + ((int)Math.round(10f/convertion_speed))/10f + " s\n" +
			"Investimento em Tecnologia - " + technology_investment + "$"
			;
	}
}
