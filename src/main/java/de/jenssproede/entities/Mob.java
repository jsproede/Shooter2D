package de.jenssproede.entities;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.entities.weapons.Bullet;
import de.jenssproede.entities.weapons.Weapon;
import de.jenssproede.gfx.HUD;
import de.jenssproede.helpers.FontHelper;
import de.jenssproede.main.Game;


public abstract class Mob extends Entity {

	public static boolean god = false;

	protected float movementSpeed;	
	protected float angle, centerX, centerY;	
	
	protected List<Weapon> weapons;
	protected int selectedWeapon;
	
	protected int health;	
	private String internalName;	
	private UnicodeFont font;
	
	private boolean spawnProtected = true;
	private int spawnCounter = 2000;
		
	public Mob(float x, float y, String internalName) {
		super(x, y, 40, 40);
		this.setInternalName(internalName);
		this.movementSpeed = 0.2f;
		this.health = 100;
		
		this.weapons = new ArrayList<Weapon>();
		this.selectedWeapon = 0;
		
		font = FontHelper.getFontWithSize(12);
	}
	
	@Override
	public void render(Graphics g) {		
		if (spawnProtected) {
			FontHelper.drawString(font, getX() + getWidth() / 2 - FontHelper.getWidth(font, "Spawn protected") / 2,
					getY() - getHeight() / 2 - 15, "Spawn protected");
		} else if (god) {
			FontHelper.drawString(font, getX() + getWidth() / 2 - FontHelper.getWidth(font, "God") / 2,
					getY() - getHeight() / 2 - 15, "God");
		} else {
			FontHelper.drawString(font, getX() + getWidth() / 2 - FontHelper.getWidth(font, String.valueOf(getHealth())) / 2,
					getY() - getHeight() / 2 - 15, String.valueOf(getHealth()));
		}
		
		FontHelper.drawString(font, getX() + getWidth() / 2 - FontHelper.getWidth(font, getInternalName()) / 2, 
				getY() - getHeight() / 2, getInternalName());
	}
	
	@Override
	public void update(org.newdawn.slick.GameContainer gc, int delta) {
		if (spawnCounter > 0) {
			spawnCounter -= delta;
		} else {
			spawnProtected = false;
		}
	}
	
	public Weapon getActiveWeapon() {
		return weapons.get(selectedWeapon);
	}
	
	public float getDistanceToPlayer(Mob m) {
		float f = getDistanceBetween(this.getX(), this.getY(), m.getX(), m.getY());
		return f;
	}
	
	public float getDistanceBetween(float startX, float startY, float endX, float endY) {
	   float dx = endX - startX;
	   float dy = startY - endY;
	   return (float)Math.sqrt(dx*dx + dy*dy);
	}
	
	public float getTargetAngle(float startX, float startY, float targetX, float targetY) {
		float dx = targetX - startX;
		float dy = targetY - startY;
	  	return (float)Math.toDegrees(Math.atan2(dy, dx));
	}

	public void move(float dx, float dy, boolean ignoreCollide) {	
		
		if (dx != 0 && dy != 0) {
			move(dx, 0, ignoreCollide);
			move(0, dy, ignoreCollide);
			
			return;
		}
		
		setX(getX() + (dx * movementSpeed));
		setY(getY() + (dy * movementSpeed));
		
		if (!ignoreCollide && hasCollided()) {			
			setX(getX() + (-1 * (dx * movementSpeed)));
			setY(getY() + (-1 * (dy * movementSpeed)));
		}
		
		collidesWithWorld();
	}
	
	public boolean hasCollided() {
		for (int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity m = Game.getLevel().getEntities().get(i);
			
			if ((!(m instanceof Bullet) && !(m instanceof WeaponRefillPack)) 
					&& isColliding(m)) {
				return true;
			} else if ((m instanceof WeaponRefillPack) && isColliding(m)) {
				WeaponRefillPack w = (WeaponRefillPack)m;
				w.delete(getActiveWeapon());
			}
		}
		
		return false;
	}
	
	public void increaseHealth(int amount) {
		health += amount;
	}
	
	public void decreaseHealth(Mob starter, int amount) {
		if (!god && !spawnProtected) {
			health -= amount;
			
			if (health <= 0 && !(this instanceof Player)) {
				HUD.addMessage(starter.getInternalName() + " hat " + this.getInternalName() + " getötet");
				Game.getLevel().getPlayers().remove(this);
			} else if (this.health == 0 && this instanceof Player) {
				HUD.addMessage("Sie wurden von " + starter.getInternalName() + " getötet. Respawn ...");
				this.respawn();
			}
		}
	}
	
	public void respawn() {
		this.setX(10);
		this.setY(10);
		
		this.increaseHealth(100);
	}
	
	public String getInternalName() {
		return internalName;
	}

	public int getHealth() {
		return health;
	}
	
	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	
	public boolean isWeaponEquiped() {
		return (selectedWeapon != -1);
	}
}
