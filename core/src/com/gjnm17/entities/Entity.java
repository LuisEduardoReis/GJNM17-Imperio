package com.gjnm17.entities;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gjnm17.Level;
import com.gjnm17.Main;
import com.gjnm17.Sprite;
import com.gjnm17.Tile;
import com.gjnm17.Util;

public class Entity {

	public Level level;
	
	public Sprite sprite;
	public boolean visible;
	public Color blend, aux_color;
	public float alpha;
	public float damage_anim_timer, damage_anim_delay;
	public float bar_anim_timer, bar_anim_delay;
	public float anim_timer, anim_speed; 
	public int anim_index;
	
	public boolean dead;
	public boolean remove;
	public boolean entityCollisions, imovable, levelCollisions;
	
	public float t;
	public float x, px;
	public float y, py;
	public float z;
	public float direction, speed, scale_x, scale_y;
	public float radius;
	public float health, full_health;
	public float mass;
	
	public float edx, edy, edf;

	public float fade_anim_timer, fade_anim_delay;


	public Entity(Level level) {
		this.level = level;
		level.addEntity(this);
	
		this.sprite = null;
		this.visible = true;
		this.blend = Color.WHITE;
		this.aux_color = Color.WHITE;
		this.alpha = 1f;
		this.damage_anim_timer = 0;
		this.damage_anim_delay = 1f;
		this.bar_anim_timer = 0;
		this.bar_anim_delay = 3f;
		
		this.anim_index = 0;
		this.anim_timer = 0;
		this.anim_speed = 1;
		this.mass = 1;
		
		this.dead = false;
		this.remove = false;
		this.entityCollisions = false;
		this.levelCollisions = false;
		this.imovable = false;
		
		this.t = 0;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.px = 0;
		this.py = 0;		
		this.direction = 0;
		this.speed = 0;
		this.scale_x = 1;
		this.scale_y = 1;
		this.radius = Main.SIZE/3;	
		this.health = 100;
		this.full_health = this.health;
		
		this.edx = 0;
		this.edy = 0;
		this.edf = 0.90f;
		
		this.fade_anim_timer = -1;
		this.fade_anim_delay = 1f;
	}
	
	public void preupdate(float delta) {
		px = x;
		py = y;
	}

	public void update(float delta) {
		t += delta;
		
		damage_anim_timer = Util.stepTo(damage_anim_timer, 0, delta);
		bar_anim_timer = Util.stepTo(bar_anim_timer, 0, delta);
		
		if (fade_anim_timer > 0) fade_anim_timer = Util.stepTo(fade_anim_timer, 0, delta);
		if (fade_anim_timer == 0) remove = true;
		
		x += (Math.cos(direction)*speed + edx)*delta;
		y += (Math.sin(direction)*speed + edy)*delta;
		
		edx *= edf;
		edy *= edf;
		
		if (health <= 0 && !dead) die();
		
		if (sprite != null) {
			if (sprite.anim_delay > 0) {
				anim_timer += delta * anim_speed;
				while (anim_timer > sprite.anim_delay) {
					anim_index++;			
					anim_timer -= sprite.anim_delay;
				}
				if (!sprite.anim_loop && anim_index >= sprite.frames.size()) anim_index = sprite.frames.size()-1;
			} else {
				anim_index = 0;
			}
			anim_index %= sprite.frames.size();
		}
	}

	public static Color color = new Color();
	public void render(SpriteBatch batch) {	
		if (!visible) return;
		if (sprite != null && alpha > 0)	{
			
			color.set(dead ? Color.GRAY : blend);
			color.mul(1,1,1,alpha);
			if (damage_anim_timer > 0) {
				float s = damage_anim_timer / damage_anim_delay;
				color.mul(1,1-s,1-s,1);
			}
			if (fade_anim_timer >= 0) color.mul(1,1,1,Util.clamp(fade_anim_timer/fade_anim_delay,0,1));
			sprite.render(batch, anim_index, (int) x, (int) y, scale_x, scale_y, direction*Util.radToDeg, color);
		}
	}
	
	public void renderShape(ShapeRenderer renderer) {
		if (!visible) return;
	}
	
	public void renderDebug(ShapeRenderer renderer) {
		renderer.setColor(blend);
		renderer.ellipse(this.x - this.radius, this.y - this.radius, 2*this.radius, 2*this.radius);
	}
	
	public void die() {
		dead = true;
	}
	
	public void revive() {
		health = full_health;
		dead = false;
	}

	public void destroy() {
		
	}

	public void collide(Entity o) {
		if (entityCollisions && o.entityCollisions && !o.imovable && collidesWith(o)) {
			if (imovable) repelRigid(o);
			else repelForce(o,25);
		}		
	}

	public boolean collidesWith(Entity o) {		
		return true; 
	}

	public void damage(float damage) {
		health = Util.stepTo(health, 0, damage);
		
		/*if (!dead) {
			damage_anim_timer = Math.max(damage_anim_timer, damage_anim_delay);
			bar_anim_timer = Math.max(bar_anim_timer, bar_anim_delay);
		}*/		
	}
	
	public void repelRigid(Entity o) {
		float dist = (radius + o.radius - Util.pointDistance(x, y, o.x, o.y));
		float dir = Util.pointDirection(x, y, o.x, o.y);
		
		o.x += dist/2 * Math.cos(dir);
		o.y += dist/2 * Math.sin(dir);
	}
	
	public void repelForce(Entity o, float force) {
		float ratio = (radius + o.radius - Util.pointDistance(x, y, o.x, o.y)) / (radius + o.radius);
		float dir = Util.pointDirection(x, y, o.x, o.y);
		
		o.x += ratio * force * Math.cos(dir);
		o.y += ratio * force * Math.sin(dir);
	}

	public Entity setPosition(float x, float y) {
		this.x = this.px = x; this.y = this.py = y; return this;		
	}
	public Entity addEVel(float x, float y) {
		this.edx += x; this.edy += y; return this;
	}
	public Entity setBlend(Color blend) {
		this.blend = blend; return this;
	}
	
	public boolean tileIsSolid(Tile tile) {
		if (tile == Tile.LAND) return true;
		return false;
	}
	
	public void levelCollision() {
		if (!levelCollisions) return;
		
		int s = Main.SIZE;
		
		// Calculate in which tile the player is in
		int xc = (int) Math.floor(this.x / s);
		int yc = (int) Math.floor(this.y / s);
		
		// Check if inside solid block to fix weird corner cases
		if (tileIsSolid(this.level.getTile(xc,yc))) {
			this.x = px;
			xc = (int) Math.floor(this.x / s);
		}
		
		// Calculate the player's position relative to the cell they're in
		float xr = this.x - (xc * s);
		float yr = this.y - (yc * s);
		
		// Check if bumping left
		if (tileIsSolid(this.level.getTile(xc-1,yc)) && xr <= this.radius) {
			xr = this.radius;
		}
		// Check if bumping right
		if (tileIsSolid(this.level.getTile(xc+1,yc)) && xr >= s - this.radius) {
			xr = s - this.radius;

		}
		// Update x. X-axis is now resolved
		this.x = (xc * s) + xr;		
		xc = (int) Math.floor(this.x / s);
		
		// Check if bumping up
		if (tileIsSolid(this.level.getTile(xc,yc-1)) && yr <= this.radius) {
			yr = this.radius;
		}
		
		// Check if bumping down
		if (tileIsSolid(this.level.getTile(xc,yc+1)) && yr >= s - this.radius) {
			yr = s - this.radius;	
		}		
		
		// Update y. Y-axis is now resolved
		this.y = (yc * s) + yr;
	}
	
	public static Comparator<Entity> zComparator = new Comparator<Entity>(){
		@Override
		public int compare(Entity o1, Entity o2) {
			return Float.compare(o1.z, o2.z);
		}		
	};

}