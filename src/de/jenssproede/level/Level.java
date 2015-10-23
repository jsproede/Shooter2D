package de.jenssproede.level;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.Texture;

import de.jenssproede.entities.Crate;
import de.jenssproede.entities.Entity;
import de.jenssproede.entities.Mob;
import de.jenssproede.entities.Player;
import de.jenssproede.entities.weapons.Bullet;
import de.jenssproede.helpers.TextureHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class Level implements Renderable {

	private List<Mob> players;
	private List<Entity> entities;
	private Player player;	
	
	private int width;
	private int height;
	
	private float offsetMaxX;
	private float offsetMaxY;
	
	private float offsetMinX = 0;
	private float offsetMinY = 0;
	
	private static float camX = 0;
	private static float camY = 0;
	
	private boolean camLock = true;
	public float zoom = 1;
	
	private Texture floor;
	
	public Level() {
		floor = TextureHelper.getTexture("floor");
		
		players = new ArrayList<Mob>();
		entities = new ArrayList<Entity>();
		
		width = Game.getWindowSize().getWidth();
		height = Game.getWindowSize().getHeight();
		
		player = new Player(10, 10, "Sie");
		players.add(player);
	}
	
	public Level(int width, int height) {
		this();
		this.width = width;
		this.height = height;
		
		offsetMaxX = width - Game.getWindowSize().getWidth() + 1;
		offsetMaxY = height - Game.getWindowSize().getHeight() + 1;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void addCrate(Crate c) {
		getEntities().add(c);
	}
	
	public void deleteBullet(Bullet deleteBullet) {
		getEntities().remove(deleteBullet);
	}
	
	public void clearAllBots() {
		getPlayers().clear();
		getPlayers().add(player);
	}

	public void clearAllEntities() {
		getEntities().clear();
	}
	
	public void toggleCamLock() {
		camLock = !camLock;
	}
	
	public static float getCamX() {
		return camX;
	}
	
	public static float getCamY() {
		return camY;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public synchronized List<Mob> getPlayers() {
		return players;
	}
	
	public void addPlayer(Mob m) {
		players.add(m);
	}
	
	public synchronized List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity e) {
		getEntities().add(e);
	}
	
	public synchronized void removeEntity(Entity e) {
		for (int i = 0; i < getEntities().size(); i++) {
			if (getEntities().get(i).equals(e)) {
				getEntities().remove(i);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {

		g.pushTransform();

		g.scale(zoom, zoom);

		g.translate(-(camX + player.getWidth() / 2 * zoom), -(camY + player.getHeight() / 2 * zoom));

		// Draw the map
//		g.setColor(Color.gray);
//		g.fillRect(1, 1, width - 1, height - 1);
		
		Color.white.bind();
		floor.bind();
		
		// Instead of gray background draw the texture over and over
		for (int i = 0; i < width; i+=floor.getImageWidth()) {
			for (int j = 0; j < height; j+=floor.getImageHeight())
			{				
				glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2f(i, j);
					glTexCoord2f(1, 0);
					glVertex2f(i + floor.getImageWidth(), j);
					glTexCoord2f(1, 1);
					glVertex2f(i + floor.getImageWidth(), j + floor.getImageHeight());
					glTexCoord2f(0, 1);
					glVertex2f(i, j + floor.getImageHeight());
				glEnd();
			}			
		}
		
		// End drawing the map
		
		for (int i = 0; i < getEntities().size(); i++) {
			getEntities().get(i).render(g);
		}
		
		for (int i = 0; i < getPlayers().size(); i++) {
			if (!(getPlayers().get(i) instanceof Player)) {
				getPlayers().get(i).render(g);
			}
		}
		
		player.render(g);

		g.popTransform();
	}

	@Override
	public void update(GameContainer gc, int delta) {

		player.update(gc, delta);
		
		camX = player.getX() - Game.getWindowSize().getWidth() / 2;
		camY = player.getY() - Game.getWindowSize().getHeight() / 2;
		
		if (camLock) {
			if (camX > offsetMaxX)
				camX = offsetMaxX;
			else if (camX < offsetMinX)
				camX = offsetMinX;
			
			if (camY > offsetMaxY)
				camY = offsetMaxY;
			else if (camY < offsetMinY)
				camY = offsetMinY;
		}
		
		for (int i = 0; i < getEntities().size(); i++) {
			getEntities().get(i).update(gc, delta);
		}
		
		for (int i = 0; i < getPlayers().size(); i++) {
			if (!(getPlayers().get(i) instanceof Player)) {
				getPlayers().get(i).update(gc, delta);
			}
		}
	}
}
