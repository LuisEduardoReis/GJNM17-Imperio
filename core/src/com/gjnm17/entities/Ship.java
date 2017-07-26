package com.gjnm17.entities;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gjnm17.Assets;
import com.gjnm17.Good;
import com.gjnm17.Level;
import com.gjnm17.Main;
import com.gjnm17.Player;
import com.gjnm17.Player.Upgrade;
import com.gjnm17.Sprites;
import com.gjnm17.Util;
import com.gjnm17.XBox360Pad;
import com.gjnm17.entities.particles.Message;


public class Ship extends Entity {
	
	public Player owner;
	public Place targetPlace;
	
	public int haul_weight, haul_value;
	
	public float continuous_x;
		
	public Ship(Level level, Player owner) {
		super(level);
		
		this.owner = owner;
		this.blend = owner.color;
		
		//this.entityCollisions = true;
		
		this.sprite = Sprites.playerSprite;
		
		this.health = this.full_health = owner.initial_reach;
		this.aux_color = Color.RED;
		
		this.levelCollisions = true;
		
		this.radius = Main.SIZE/2.5f;
		
		this.haul_weight = 0;
		this.haul_value = 0;		
	}
	
	@Override
	public void preupdate(float delta) {
		continuous_x += Util.clamp(x - px, -Main.SIZE,Main.SIZE);
		
		float d = Math.min(Util.pointDistance(px, py, x, y), owner.ship_speed*delta);		
		if (!Main.DEBUG) damage(0.25f*d);
		
		
		super.preupdate(delta);		
	}
	
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		x %= Main.WIDTH; if (x < 0) x = Main.WIDTH-x;
		y %= Main.HEIGHT; if (y < 0) y = Main.HEIGHT-y;
		
		targetPlace = null; float td = Float.MAX_VALUE;
		for(Place p : level.places.values()) {
			
			float d = Util.pointDistance(x, y, p.x, p.y);
			if (d < td && d < radius+p.radius) {
				targetPlace = p;
				td = d;
			}
		}
		
		if(!dead & owner != null && owner.controller != null) {
			Controller controller = owner.controller;
			
			// Movement
			float lax = controller.getAxis(XBox360Pad.AXIS_LEFT_X),
				lay = controller.getAxis(XBox360Pad.AXIS_LEFT_Y);
			float deadzone = 0.25f;
			float speed = owner.ship_speed;
			boolean moving = false;
			
			float ld = Util.pointDistance(0,0, lax,lay);
			if (ld > deadzone) {
				this.y -= speed*delta * lay;
				this.x += speed*delta * lax;
				
				moving = true;				
			}		
			
			if(lax > 0) scale_x = -1; else scale_x = 1;
			
			// Place interaction
			if (targetPlace != null) {
				targetPlace.renderActive = true;
				
				// Trade message
				if (targetPlace.trade != null && (targetPlace.owner == null || targetPlace.owner == owner)) {
					Good g = targetPlace.trade;
					owner.message = g.name + " " + g.value+"$ "+ g.weight+"P "+(level.t % 1 < 0.5f ? "(A)" : "   ");
					
					if (g.weight > owner.ship_capacity - haul_weight) owner.message_color = Color.RED;					
				}
				
				// Place convertion
				if (targetPlace.owner != owner) {
					if (owner.message == null) owner.message = ""; else owner.message += "\n";
					owner.message += "Colonizar... " + (level.t % 1 < 0.5f ? "(X)" : "   ");
				}				
				if (controller.getButton(XBox360Pad.BUTTON_X) && !moving && !targetPlace.home) {
					
					if ((targetPlace.owner == owner && targetPlace.convertion > 0) || (targetPlace.owner != owner && targetPlace.converter != owner)) {
						
						targetPlace.convertion = Util.stepTo(targetPlace.convertion, 0, delta*owner.convertion_speed);
						if (targetPlace.convertion == 0 && targetPlace.owner != owner) targetPlace.converter = owner;
						
						owner.message = "A remover colonização passada... "+(int)((1f-targetPlace.convertion)*100)+"%";
						owner.message_color = Color.BLACK;
					} else if (targetPlace.owner != owner){
						targetPlace.convertion = Util.stepTo(targetPlace.convertion, 1, delta*owner.convertion_speed*(targetPlace.owner == null ? 1 : 0.5f));
						
						if (targetPlace.convertion == 1) {
							targetPlace.convertion = 0;
							targetPlace.owner = owner;
							
							Main.playSound(Assets.colonize);
						}
						
						owner.message = "A colonizar... "+(int)(targetPlace.convertion*100)+"%";
						owner.message_color = Color.BLACK;
					}
					
					targetPlace.trade_timer = targetPlace.trade_delay;					
				}	
				// Place healing
				else {
					health = Util.stepTo(health, full_health, delta * owner.heal_speed * ((targetPlace.home || targetPlace.owner == owner) ? owner.owner_bonus : 1f));
					
					// Unload haul
					if (targetPlace.home && haul_value > 0) {
						
						owner.getMoney(haul_value, x,y);
						haul_value = 0;
						haul_weight = 0;	
						
						
						Main.playSound(Assets.pickup[0]);
						Main.playSound(Assets.pickup[2]);
					}
				}
				
		
				// At home
				if (targetPlace.home) {
					// Upgrade message
					Upgrade upgrade = Player.upgrades[owner.upgradeIndex];
					owner.message = "Melhorias (setas para alternar):\n";
					owner.message += (owner.upgradeIndex+1)+" - \"" + upgrade.name + "\" - " + upgrade.cost + "$ "+(level.t % 1 < 0.5f ? "(X)" : "   ");
					
					if (upgrade.cost > owner.money) owner.message_color = Color.RED;
					
					
					// Trip around the world
					if (Math.abs(continuous_x) >= Main.WIDTH*0.9f) {
						new Message(level, "Viagem à volta do mundo! +100$").setBlend(owner.color);
						owner.money += 100;
						continuous_x = 0;
					}
				}
				
			}
		
			// Stats
			if (controller.getButton(XBox360Pad.BUTTON_LB) || controller.getButton(XBox360Pad.BUTTON_RB)) {
				owner.message = owner.getStatsString();
				owner.message_color = Color.BLACK;
			}
		}

	} 
	
	@Override
	public void die() {
		super.die();
		
		owner.playerMessage("Caravela perdida!");
		
		Main.playSound(Assets.ship_down);
		
		this.fade_anim_timer = fade_anim_delay;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (owner.ship == this) owner.ship = null;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		// Bar
		float w = 4*radius;
		batch.setColor(blend);
		float by = y+1.5f*radius;
		if (health < 0.2f*full_health && level.t % 0.5f < 0.25f) by += 5;
		Util.drawCentered(batch, Assets.rect, x-w/2, by, w*health/full_health,10, 0, false,false);
	}
	

}
