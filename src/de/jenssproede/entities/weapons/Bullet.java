package de.jenssproede.entities.weapons;

import java.util.UUID;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.jenssproede.entities.Entity;
import de.jenssproede.entities.ReceivableEntity;
import de.jenssproede.entities.Mob;
import de.jenssproede.main.Game;

public class Bullet extends ReceivableEntity {

	private float maxSpeed = 1f;//3f;
	
	private float xSpeed;
	private float ySpeed;
	
	private UUID id;
	
	private Mob starter;
	private int damage;
	
	private boolean playerHit = false;
	
	public boolean isPlayerHit() {
		return playerHit;
	}

	public Bullet(float startX, float startY, float endX, float endY, Mob starter, int damage) {
		super(startX, startY, 5, 5);
		id = UUID.randomUUID();
		
		this.starter = starter;
		this.damage = damage;
		
		xSpeed = (endX - startX) / 1;
		ySpeed = (endY - startY) / 1;
		
		double factor = maxSpeed / Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
		
		xSpeed *= factor;
		ySpeed *= factor;
		
//		System.out.println("Bullet [" + id.toString() + "] has spawned!");
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
		
		g.setColor(Color.gray);
		g.drawOval(getX() - 1, getY() - 1, getWidth() + 1, getHeight() + 1);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		move(xSpeed *  delta, ySpeed * delta);
	}
	
	public void move(double dx, double dy) {
		setX(getX() + (float)dx);
		setY(getY() + (float)dy);
				
		Entity e = null;
		
		if ( (e = hasCollided()) != null) {
			delete(null);
			
			if (e instanceof Mob) {
				Mob m = (Mob)e;
				m.decreaseHealth(starter, damage);
			}
		}
		
		collidesWithWorld();
	}
	
	public Entity hasCollided() {
		
		Entity ent = null;
		
		if (this.hitbox.intersects(starter.hitbox)) {
			return null;
		}
		
		for (Entity m : Game.getLevel().getEntities()) {
			if (m instanceof ReceivableEntity) {
				break;
			}
			
			if (m.hitbox.intersects(this.hitbox)) {
				ent = m;
			}
		}
		
		for (Mob m : Game.getLevel().getPlayers()) {
			if (m.hitbox.intersects(this.hitbox)) {
				playerHit = true;
				ent = m;
			}
		}
		
		return ent;
	}
	
	public void delete(Weapon w) {
//		System.out.println("Bullet [" + id.toString() + "] has been removed!");
		Game.getLevel().deleteBullet(this);
	}
	
	public void collidesWithWorld() {
		if (getX() < 0 || getX() > Game.getLevel().getWidth() ||
				getY() < 0 || getY() > Game.getLevel().getHeight()) {
			delete(null);
		}
	}
}
