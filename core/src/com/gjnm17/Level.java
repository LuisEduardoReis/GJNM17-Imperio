package com.gjnm17;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gjnm17.entities.Entity;
import com.gjnm17.entities.Place;

public class Level {	
	public GameScreen game;

	public TiledMap map;
	public OrthogonalTiledMapRenderer tileRenderer;
	public int map_width, map_height;
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Entity> newEntities = new ArrayList<Entity>();
	
	public ArrayList<Player> players = new ArrayList<Player>();
	public HashMap<String, Place> places = new HashMap<String, Place>();
	public Place europa;
	
	
	public Tile[] tiles;
	
	public float t;

	public Color[] playerColors = {Color.RED, Color.YELLOW, Color.CYAN, Color.PURPLE};
	public String[] playerHomes = {"Lisboa","Madrid","Paris","Londres"};
	public String[] playerNames = {"Império Português","Império Espanhol","Império Francês","Império Britânico"};


	
	
	public Level(GameScreen game) {
		this.game = game;
			
		this.map = new TmxMapLoader(new InternalFileHandleResolver()).load("worldmap.tmx");
		this.tileRenderer = new OrthogonalTiledMapRenderer(map);
		
		
		map_width = (Integer) map.getProperties().get("width");
		map_height = (Integer) map.getProperties().get("height");
		tiles = new Tile[map_width*map_height];
		
		for(int i = 0; i < tiles.length; i++) tiles[i] = Tile.SEA;
		
		TiledMapTileLayer tiled_tiles = (TiledMapTileLayer) map.getLayers().get("main"); 
		for(int yy = 0; yy<map_height; yy++) {
			for(int xx = 0; xx<map_width; xx++) {
				TiledMapTileLayer.Cell cell = tiled_tiles.getCell(xx, yy);
				
				/*System.out.print("\t"+((cell == null) ? " " : 
					"("+cell.getTile().getProperties().get("solid")+")" 
						));*/
				if (cell != null) {
					//int cid = cell.getTile().getId()-1;
					tiles[yy*map_width+xx] = Tile.LAND;
				}
			}
			//System.out.println(); System.out.println();
		}
		
		for(MapObject o : map.getLayers().get("objects").getObjects()) {
			String type = (String) o.getProperties().get("type"); 
			Vector2 p = Util.getMapObjectPosition(o);
			
			if (type != null) {
				type = type.toLowerCase();
				
				if(type.equals("place")) {
					Place place = new Place(this, o.getName());
					place.setPosition(p.x, p.y);
					place.radius = (Float) o.getProperties().get("width")/2;
				}
			}
		}
		europa = new Place(this, "Europa");
		europa.visible = false;
		europa.radius = 80 - 2*Main.SIZE/3;
		europa.home = true;
		europa.setPosition(900, Main.HEIGHT-280);
		

		for(int i = 0; i < playerHomes.length; i++) {
			places.get(playerHomes[i]).home = true;
		}
		Array<Controller> controllers = Controllers.getControllers();
		for(int i = 0; i < 4; i++) {
			Controller c = (i > controllers.size-1) ? null : controllers.get(i);
			Player p = new Player(this,c);
			p.id = i;
			p.color = playerColors[i];
			p.name = playerNames[i];
			Place home = places.get(playerHomes[i]);
			home.owner = p;
			p.home = home;
			home.converter = p;
			home.convertion = 1;
		}
		
		System.out.println();
		for(Place p : places.values()) {
			if (p.home) continue;
			
			System.out.println(p.name);
		}
		
		Good.addGoods(this);
	}

	public Tile getTile(int x, int y) { return (x < 0 || y < 0 || x >= map_width || y >= map_height) ? Tile.SEA : tiles[y*map_width+x];	}
	public void setTile(int x, int y, Tile tile) { if (x >= 0 && y >= 0 && x < map_width && y < map_height) tiles[y*map_width+x] = tile; }
	
	
	public void update(float delta) {
		
		t+=delta;
	
		for(Player p : players) p.update(delta);
		
		for(Entity e : entities) e.preupdate(delta);		
		for(Entity e : entities) e.update(delta);
		
		entities.addAll(newEntities);
		newEntities.clear();
		
		// Entity Collisions
		for(Entity e : entities) {
		for(Entity o : entities) {
			if (e == o) continue;
			float sqrDist = Util.pointDistanceSqr(e.x, e.y, o.x, o.y);
			float radii = (e.radius + o.radius);
			
			// Collisions
			if (sqrDist <= radii*radii) e.collide(o);			
		}}
		
		for(Entity e : entities) e.levelCollision();
		
		for(int i = 0; i < entities.size(); i++) 
			if (entities.get(i).remove)
				entities.remove(i).destroy();
		
		
		entities.sort(Entity.zComparator);
	}
	
	public void renderEntities(SpriteBatch batch) {
		for(Entity e : entities) e.render(batch);
	}
	
	public void renderEntitiesShape(ShapeRenderer batch) {
		for(Entity e : entities) e.renderShape(batch);
	}
	
	public void renderDebug(ShapeRenderer renderer) {
		for(Entity e : entities) e.renderDebug(renderer);
		
		renderer.setColor(Color.WHITE);
		for(int y = 0; y < map_height; y++) {
		for(int x = 0; x < map_width; x++) {
			if (getTile(x,y) == Tile.LAND) renderer.rect(x*40,y*40,40,40);			
		}}
	}

	
	public void addEntity(Entity entity) { newEntities.add(entity); }
	
}
