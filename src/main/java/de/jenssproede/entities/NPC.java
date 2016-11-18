package de.jenssproede.entities;

import java.util.List;
import java.util.Random;

import de.jenssproede.gfx.DebugMonitor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.jenssproede.entities.weapons.Weapon;
import de.jenssproede.main.Game;

public class NPC extends Mob {

	private int deltaCounter;
	private int deltaCooldownCounter = 2000;
//	private int deltaChangePositionCounter = 0;
	
	private float dx, dy;
	private float lookingDirX, lookingDirY;
	
	private int playerWidth;
	
	private Mob enemyPlayer = null;
//	private Mob attacker = null;
	
	public NPC(float x, float y, String internalName) {
		super(x, y, internalName);
		deltaCounter = 0;
		
		movementSpeed = .2f;
		
		this.dx = 0;
		this.dy = 0;
		
		weapons.add(new Weapon("Pistole", false, 10, this, 5, 0.2f, 30));
		selectedWeapon = 0;
		
		playerWidth = new Random().nextInt(6);
	}

	@Override
	public void render(Graphics g) {
		super.render(g);		
		
		centerX = getX() + getWidth() / 2;
		centerY = getY() + getHeight() / 2;

		if (DebugMonitor.showDebugMonitor && enemyPlayer != null) {
			g.setColor(Color.white);
			g.drawLine(this.centerX, this.centerY, enemyPlayer.centerX, enemyPlayer.centerY);
		}
		
		angle = getTargetAngle(centerX, centerY, lookingDirX, lookingDirY);
		
		g.setColor(new Color(240, 100, 100));
		
		if (Game.drawHitboxes)
			g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		
		// Draw here the character (rotating is already done!!!)
		// Enable rotation
		g.rotate(centerX, centerY, angle);
		g.fillOval(centerX - 5, centerY - 25, 10, 50);		
		
		// Disable rotate
		g.rotate(centerX, centerY, -angle);
		// End drawing character (rotating is also disabled!)

		g.setColor(new Color(0, 0, 0));
		g.fillOval(getX(), getY(), getWidth(), getHeight() - playerWidth);
		
		// Enable rotation
		g.rotate(centerX, centerY, angle);
		
		g.setColor(Color.gray);		
		g.drawOval(centerX + 10, centerY - 3, 6, 6);
		
		// Disable rotate
		g.rotate(centerX, centerY, -angle);		
		
//		g.setColor(new Color(0, 0, 0));
//		g.fillOval(getX(), getY(), getWidth(), getHeight());
	}
	
	public void searchForAnotherEnemy() {
		if (true) {
			int nearestPlayer = Game.getLevel().getWidth();
			List<Mob> players = Game.getLevel().getPlayers();
			for (int i = 0; i < players.size(); i++) {
				
				if (players.get(i).equals(this)) continue;
				//if (enemyPlayer != null && enemyPlayer.equals(players.get(i))) { System.out.println("Not the same ...");continue; }
				
				int temp = (int)getDistanceBetween(getX(), getY(), players.get(i).getX(), players.get(i).getY());
				
				if (temp < nearestPlayer && temp < 500) {
					enemyPlayer = players.get(i);
					nearestPlayer = temp;
				}			
			}
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		
		deltaCounter += delta;
		
		getActiveWeapon().update(gc, delta);
		
		move(dx, dy, false);
		
		if (deltaCooldownCounter > 0) {
			deltaCooldownCounter -= delta;
		}
		
		if (deltaCounter >= 100) {
			deltaCounter = 0;
				
			if (enemyPlayer != null) {
				lookingDirX = enemyPlayer.getX();
				lookingDirY = enemyPlayer.getY();
				
				float dist = getDistanceBetween(getX(),  getY(), lookingDirX, lookingDirY);				
				
				if (dist >= 120) {
					if (lookingDirX < getX()) {
						dx = -delta;
					} else {
						dx = delta;
					}
					
					if (lookingDirY < getY()) {
						dy = -delta;
					} else {
						dy = delta;
					}
					
					
					if (Game.npcShoot && new Random().nextInt(100) > 95) {					
						if (getActiveWeapon().getRemainingShots() == 0) getActiveWeapon().reload();
						else getActiveWeapon().shoot(getX(), getY(), lookingDirX, lookingDirY);
					}
				} else {
					dx = 0;
					dy = 0;
					
					if ((!getActiveWeapon().lastBulletHit() && new Random().nextInt(100) > 50) || (enemyPlayer.getHealth() == 0)) {
						searchForAnotherEnemy();
						
					} else {					
						if (Game.npcShoot && new Random().nextInt(100) > 40) {					
							if (getActiveWeapon().getRemainingShots() == 0) getActiveWeapon().reload();
							else getActiveWeapon().shoot(getX(), getY(), lookingDirX, lookingDirY);
						}
					}
				}
			}
			
			if (deltaCooldownCounter <= 100) {
				
				searchForAnotherEnemy();
				
				deltaCooldownCounter = 0;
								
				
			}
		}
	}
}
