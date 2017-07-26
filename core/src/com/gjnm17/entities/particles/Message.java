package com.gjnm17.entities.particles;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gjnm17.Assets;
import com.gjnm17.Level;
import com.gjnm17.Main;
import com.gjnm17.Util;

public class Message extends Particle {

	public String message;

	public Message(Level level, String message) {
		super(level);
		
		this.sprite = null;
		this.message = message;
		
		this.lifetime = 4f;
		this.speed = Main.SIZE;
		this.direction = (float) (Math.PI/2);
		this.scale_x = 2f;
		
		this.x = Main.WIDTH/2;
		this.y = Main.HEIGHT/2;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		BitmapFont font = Assets.font;
		
		font.setColor(color.set(blend).mul(1, 1, 1, alpha));
		font.getData().setScale(scale_x);
		Util.drawTextCentered(batch, font, message, x,y);
	}

}
