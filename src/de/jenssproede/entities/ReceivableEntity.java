package de.jenssproede.entities;

import de.jenssproede.entities.weapons.Weapon;

public abstract class ReceivableEntity extends Entity {

	public ReceivableEntity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public abstract void delete(Weapon w);
}