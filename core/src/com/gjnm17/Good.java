package com.gjnm17;

import java.util.HashMap;

import com.gjnm17.entities.Place;

public class Good {
	public int value;
	public int weight;
	public String name;
	
	public Good(String n, int v, int w) {this.name = n; this.value = v; this.weight = w;}
	
	public static Good getRandomWeighedGood(Place p) {
		float r  = Util.randomRangef(0, p.goods_weight_sum);
		for(int i = 0; i < p.goods.size(); i++) {
			r -= p.goods_weights.get(i);
			if (r < 0) return p.goods.get(i);
		}
		return null;
	}	
	
	public static HashMap<String, Good> allGoods = new HashMap<String, Good>();
	public static void createGood(String n, int v, int w) {Good good = new Good(n,v,w); allGoods.put(good.name, good);}
	static {
		createGood("Fruta",1,1);
		createGood("Cereais",1,1);
		createGood("Açucar",1,1);
		createGood("Madeiras",2,2);
		createGood("Animais",1,2);
		
		createGood("Escravos",2,1);
		createGood("Fruta Tropical",2,1);
		createGood("Madeiras Exoticas",3,2);
		createGood("Pedras Preciosas",12,2);
		createGood("Ouro",20,3);
		createGood("Marfim",5,2);
		createGood("Malagueta",4,1);
		createGood("Animais Exoticos",7,3);
		createGood("Aves Raras",4,2);
		
		createGood("Porcelana",2,2);
		createGood("Seda",3,2);
		createGood("Especiarias",15,2);
		createGood("Perfumes",8,1);
		
		createGood("Elefante Branco",20,10);
	}
	
	public static void addGoods(Level level) {
		// Ilhas
		level.places.get("Madeira")
			.addGood("Fruta", 1).addGood("Cereais", 1).addGood("Açucar", 1).addGood("Madeiras", 1).addGood("Animais", 1);
		level.places.get("Açores")
			.addGood("Fruta", 1).addGood("Cereais", 1).addGood("Açucar", 1).addGood("Madeiras", 1).addGood("Animais", 1);
				
		level.places.get("Cabo Verde")
			.addGood("Fruta", 1).addGood("Madeiras", 3).addGood("Animais", 1).addGood("Escravos", 5);
		
		// Africa
		level.places.get("Cabo da Boa Esperança")
			.addGood("Escravos", 10).addGood("Malagueta",5).addGood("Marfim", 2).addGood("Ouro", 1);
		level.places.get("Melinde")
			.addGood("Escravos", 10).addGood("Malagueta",10).addGood("Marfim", 5).addGood("Ouro", 3);
		
		level.places.get("Madagascar")
			.addGood("Fruta Tropical", 10).addGood("Animais Exoticos", 10).addGood("Ouro", 1);
		
		// America do Sul
		level.places.get("Porto Seguro")
			.addGood("Fruta Tropical", 10).addGood("Madeiras Exoticas",5).addGood("Pedras Preciosas", 3).addGood("Ouro", 5);
		
		// India/Asia
		level.places.get("Goa")
			.addGood("Especiarias", 1).addGood("Seda", 1).addGood("Perfumes", 1).addGood("Porcelana", 1).addGood("Pedras Preciosas", 1);
		level.places.get("Calcutá")
			.addGood("Especiarias", 1).addGood("Seda", 1).addGood("Perfumes", 1).addGood("Porcelana", 1).addGood("Pedras Preciosas", 1);
		
		level.places.get("Malaca")
			.addGood("Especiarias", 1).addGood("Seda", 1).addGood("Perfumes", 1).addGood("Porcelana", 1).addGood("Pedras Preciosas", 1);
		level.places.get("Sumatra")
			.addGood("Especiarias", 1).addGood("Seda", 1).addGood("Perfumes", 1).addGood("Porcelana", 1).addGood("Pedras Preciosas", 1);
		
		level.places.get("Timor")
			.addGood("Fruta Tropical", 10).addGood("Animais Exoticos", 10).addGood("Ouro", 5);
			
	}

}