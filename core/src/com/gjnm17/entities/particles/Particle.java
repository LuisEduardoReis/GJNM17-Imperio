package com.gjnm17.entities.particles;

import com.gjnm17.Level;
import com.gjnm17.Sprites;
import com.gjnm17.Util;
import com.gjnm17.entities.Entity;

public class Particle extends Entity {

	public float lifetime;
	public float dscale;
	
	public Particle(Level level) {
		super(level);
		
		this.sprite = Sprites.circleSprite;
				
		this.lifetime = 1f;
		
		this.dscale = 0f;
	}
	
	public Particle setDscale(float dscale) {
		this.dscale = dscale; return this;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (lifetime > 0 && t > lifetime) remove = true;
		
		scale_x += dscale*delta;
		scale_y += dscale*delta;
		
		alpha = Util.clamp(1f - (t / lifetime),0,1);
	}

}
