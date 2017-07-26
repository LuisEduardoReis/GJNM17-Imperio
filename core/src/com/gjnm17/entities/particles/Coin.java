package com.gjnm17.entities.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.gjnm17.GameScreen;
import com.gjnm17.Level;
import com.gjnm17.Main;
import com.gjnm17.Player;
import com.gjnm17.Util;

public class Coin extends Particle {

	Player owner;
	int value;
	
	public Coin(Level level, Player owner) {
		super(level);
		
		this.owner = owner;
		this.scale_x = this.scale_y = 0.25f;
		this.radius *= this.scale_x;
		this.blend = Color.GOLD;
		this.value = 1;
		
		this.dscale = 0;
		this.lifetime = -1;
		
		float dir = Util.randomRangef(0, (float) (2*Math.PI));
		float p = Util.randomRangef(100, 1000);
		this.addEVel(p * (float) Math.cos(dir), p * (float) Math.sin(dir));
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		Rectangle r = GameScreen.playerHudSpaces[owner.id];
		float tx = r.x + r.width/2, ty = r.y + r.height/2;
				
				
		this.direction = Util.pointDirection(x, y, tx, ty);
		this.speed = 500;
		
		if (Util.pointDistance(x, y, tx, ty) < Main.SIZE) remove = true;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		owner.money += value;
	}
	
	public Coin setValue(int value) {
		this.value = value;
		return this;
	}

}
