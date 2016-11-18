package de.jenssproede.entities;

import org.newdawn.slick.geom.Rectangle;

import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public abstract class Entity implements Renderable {

	private float x;
	private float y;
	private float width;
	private float height;
	
	public Rectangle hitbox;
	
	public Entity(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		hitbox = new Rectangle(x, y, width, height);
		
		collidesWithWorld();
	}

	public void collidesWithWorld() {
		if (Game.getLevel() != null) {
			if (this.x < 0) {
				setX(0);
			} else if (this.x > Game.getLevel().getWidth() - this.width) {
				setX(Game.getLevel().getWidth() - this.width);
			}
			
			if (this.y < 0) {
				setY(0);
			} else if (this.y > Game.getLevel().getHeight() - this.height) {
				setY(Game.getLevel().getHeight() - this.height);
			}
		}
	}
	
	public boolean isColliding(Entity e) {
		return e.hitbox.intersects(this.hitbox);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
		hitbox.setX(x);
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
		hitbox.setY(y);
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
		hitbox.setWidth(width);
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
		hitbox.setHeight(height);
	}	
}
