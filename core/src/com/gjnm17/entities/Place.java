package com.gjnm17.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gjnm17.Assets;
import com.gjnm17.Good;
import com.gjnm17.Level;
import com.gjnm17.Main;
import com.gjnm17.Player;
import com.gjnm17.Util;

public class Place extends Entity {

	
	
	
	public String name;
	public Player converter, owner;
	public float convertion;
	public boolean home;
	
	public boolean renderActive;
	public Good trade;
	public float trade_timer, trade_delay;
	public float money_timer, money_delay;
	
	public ArrayList<Good> goods = new ArrayList<Good>();
	public ArrayList<Float> goods_weights = new ArrayList<Float>();
	public float goods_weight_sum = 0;
	
	public Place addGood(String good, float weight) {
		if (!Good.allGoods.containsKey(good)) throw new RuntimeException("Unknown good: " + good);
		goods.add(Good.allGoods.get(good));
		goods_weights.add(weight);
		goods_weight_sum += weight;
		return this;
	}
	
	public Place(Level level, String name) {
		super(level);
		
		this.name = name;
		level.places.put(name, this);
		
		owner = null;
		converter = null;
		convertion = 0;
		home = false;
		
		trade = null;
		trade_timer = 0;
		trade_delay = 1f;
		
		money_timer = 0;
		money_delay = 5f;
		
		z = 1;
	}
	
	@Override
	public void preupdate(float delta) {
		super.preupdate(delta);
		
		this.renderActive = false;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (goods.size() > 0 && trade == null && !home) {
			trade_timer = Util.stepTo(trade_timer, 0, delta);
			
			if (trade_timer == 0) {
				trade = Good.getRandomWeighedGood(this);
				trade_timer = trade_delay * (owner == null ? 1 : 0.5f);
			}
		}
		
		if (owner != null) {
			money_timer = Util.stepTo(money_timer, 0, delta);
			if (money_timer == 0) {
				owner.getMoney(1, x,y);
				money_timer = money_delay;
			}
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		if (!visible) return;
		
		if (renderActive) {
			BitmapFont font = Assets.font;
			
			// Name
			if (!home) {
				font.setColor(Color.WHITE);
				font.getData().setScale(1f);
				Util.drawTextCentered(batch, font, name, (int) x, (int) y + 2f*Main.SIZE/3);
			}
			
			// Bar
			if (renderActive && convertion < 1) {
				if (converter != null) 
					batch.setColor(converter.color);
				else
					batch.setColor(Color.WHITE);			
				
				float w = 4*Main.SIZE/3;
				Util.drawCentered(batch, Assets.rect, x-w/2, y-2*Main.SIZE/3, w*convertion,10, 0, false,false);
			}			
		}
		
		// Circle
		if (owner != null) 
			batch.setColor(owner.color);
		else
			batch.setColor(Color.WHITE);
		
		float r = Main.SIZE/3;	
		if (owner != null) r *= 2f;
		if (trade != null && level.t % 1 > 0.5f) r += 5f;
		Util.drawCentered(batch,Assets.circle,x,y, r,r, 0, true,true);
	}

}
