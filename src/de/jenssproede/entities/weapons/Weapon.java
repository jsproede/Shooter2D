package de.jenssproede.entities.weapons;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.opengl.Texture;

import de.jenssproede.entities.Mob;
import de.jenssproede.helpers.SoundHelper;
import de.jenssproede.helpers.TextureHelper;
import de.jenssproede.main.Game;

public class Weapon {
	
	protected int shots;
	private int maxBullets;
	private int remainingBullets;
	private String weaponName;
	private boolean isAutomatic;
	private boolean isReloading;
	private int deltaCounter = 0;
	private int damage;
	
	private Mob owner;
	
	private List<Bullet> shotBullets;
	
	private Texture image = null;
	
	private float soundVolume = 0.2f;
	
	public Weapon(String weaponName, boolean isAutomatic, int maxBullets, Mob owner, int damage, float soundVolume, int remainingBullets) {
		this.weaponName = weaponName;
		this.maxBullets = maxBullets;
		this.isAutomatic = isAutomatic;
		this.shots = maxBullets;
		this.owner = owner;
		this.damage = damage;
		this.soundVolume = soundVolume;
		this.remainingBullets = remainingBullets;
		
		this.image = TextureHelper.getTexture(weaponName);
		
		shotBullets = new ArrayList<Bullet>();
	}

	public void shoot(float startX, float startY, float endX, float endY) {
		if (!isReloading) {
			if (shots != 0) {			
				if (isAutomatic)
					SoundHelper.playSound("fire_m4a1");
				else
					SoundHelper.playSound("fire");
				
				Bullet b = new Bullet(startX, startY, endX, endY, owner, damage);
				Game.getLevel().getEntities().add(b);
				shotBullets.add(b);
				
				shots--;
			} else {
				SoundHelper.playSound("empty");
			}
		}
	}
	
	public int getHits() {
		int counter = 0;
		
		for (int i = 0; i < shotBullets.size(); i++) {
			if (shotBullets.get(i).isPlayerHit()) {
				counter++;
			}
		}
		
		return counter;
	}
	
	public boolean lastBulletHit() {
		if (shotBullets.size() > 0) {
			return shotBullets.get(shotBullets.size() - 1).isPlayerHit();
		} else {
			return true;
		}
	}
	
	public void update(GameContainer gc, int delta) {
		
		gc.setSoundVolume(getSoundVolume());
		
		if (isReloading) {
			deltaCounter += delta;
			
			if (deltaCounter > 3000) {			
				if (remainingBullets >= maxBullets) {
					remainingBullets -= (maxBullets - shots);
					shots = maxBullets;
				} else {
					
					shots += remainingBullets;
					
					if (shots > 10) {
						remainingBullets = shots - 10;
						shots = 10;
					} else {
						remainingBullets = 0;
					}
				}
				
				deltaCounter = 0;
				isReloading = false;
			}
		}
	}
	
	public boolean isAutomatic() {
		return isAutomatic;
	}
	
	public void reload() {
		if (!isReloading && shots < maxBullets && remainingBullets > 0) {
			SoundHelper.playSound("reload");
			isReloading = true;
		}
	}
	
	public boolean isEmpty() {
		 return (getRemainingBullets() == 0 && 
				 getRemainingShots() == 0);
	}
	
	public int getRemainingShots() {
		return shots;
	}

	public String getWeaponName() {
		return weaponName;
	}

	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}
	
	public boolean isReloading() {
		return isReloading;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public Texture getImage() {
		return image;
	}

	public int getRemainingBullets() {
		return remainingBullets;
	}
	
	public void setRemainingBullets(int remBullets) {
		remainingBullets = remBullets;
	}

	public boolean increaseRemainingBullets(int i) {
		if (getRemainingBullets() <= 200) {
			this.setRemainingBullets(getRemainingBullets() + i);
			return true;
		} else {
			return false;
		}
	}
	
	public Mob getOwner() {
		return owner;
	}
	
	public void setShots(int shots) {
		this.shots = shots;
	}
}
