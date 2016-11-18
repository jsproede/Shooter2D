package de.jenssproede.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jenssproede.entities.Crate;
import de.jenssproede.entities.Entity;
import de.jenssproede.entities.NPC;
import de.jenssproede.entities.WeaponRefillPack;
import de.jenssproede.main.Game;

public class EntityHelper {

	public static void generateCrateLevel() {
		if (Game.getLevel() != null) {
			// Spawn some random boxes in the world
			
			Random rnd = new Random();
			
			int amount = rnd.nextInt(20) + 50;
			
			List<Crate> crates = new ArrayList<Crate>();
			
			for (Entity e : Game.getLevel().getEntities()) {
				if (e instanceof Crate) {
					crates.add((Crate)e);
				}
			}
			
			Crate c;
			
			for (int i = 0; i < amount; i++) {				
				c = new Crate(rnd.nextInt(Game.getLevel().getWidth()), rnd.nextInt(Game.getLevel().getHeight()));
				
				boolean intersects = false;
				
				for (int j = 0; j < crates.size(); j++) {
					Crate cr = crates.get(j);
					
					if (c.intersectsWithCrate(cr)) {
						intersects = true;
						break;
					}
				}
											
				if (!intersects) {
					Game.getLevel().getEntities().add(c);
					crates.add(c);
				}
			}
			
			spawnRefillPacks(rnd.nextInt(50) + 20);
		}
	}	
	
	public static void spawnRefillPacks(int amount) {
		Random rnd = new Random();
		
		for (int i = 0; i < amount; i++) {
			WeaponRefillPack w = new WeaponRefillPack(	rnd.nextInt(Game.getLevel().getWidth()), 
														rnd.nextInt(Game.getLevel().getHeight()), 
														rnd.nextInt(40) + 20);
			
			boolean intersects = false;
			
			List<Entity> entities = Game.getLevel().getEntities();
			
			for (int j = 0; j < entities.size(); j++) {
				if (entities.get(j) instanceof Entity) {
					if (w.isColliding(entities.get(j))) {
						intersects = true;
						break;
					}
				}
			}
			
			if (!intersects) {
				entities.add(w);
			}
		}		
	}
	
	public static void addNPCs(int amount) {
		Random rnd = new Random();
		for (int i = 0; i < amount; i++) {
			
			int rndX = rnd.nextInt(Game.getLevel().getWidth());
			int rndY = rnd.nextInt(Game.getLevel().getHeight());
			
			Game.getLevel().getPlayers().add(new NPC(rndX, rndY, "Bot " + (Game.getLevel().getPlayers().size() + i)));
		}
	}
}
