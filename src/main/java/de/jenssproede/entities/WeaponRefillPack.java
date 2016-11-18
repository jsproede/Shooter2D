package de.jenssproede.entities;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.Texture;

import de.jenssproede.entities.weapons.Weapon;
import de.jenssproede.gfx.HUD;
import de.jenssproede.helpers.TextureHelper;
import de.jenssproede.main.Game;

public class WeaponRefillPack extends ReceivableEntity {

	private Texture tex;
	private boolean show = true;
	private int bullets;
	
	public WeaponRefillPack(float x, float y, int bullets) {
		super(x, y, 1, 1);
		
		tex = TextureHelper.getTexture("ammo2");
		setWidth(tex.getTextureWidth());
		setHeight(tex.getTextureHeight());
		
		this.bullets = bullets;
	}

	@Override
	public void render(Graphics g) {
		if (show) {
			Color.white.bind();
			tex.bind();
			
			glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex2f(getX(), getY());
				glTexCoord2f(1, 0);
				glVertex2f(getX() + getWidth(), getY());
				glTexCoord2f(1, 1);
				glVertex2f(getX() + getWidth(), getY() + getHeight());
				glTexCoord2f(0, 1);
				glVertex2f(getX(), getY() + getHeight());
			glEnd();
			
			if (Game.drawHitboxes) {
				g.setColor(Color.cyan);
				g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
			}
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
	public void delete(Weapon w) {
		if (w.increaseRemainingBullets(bullets)) {
			if (w.getOwner().equals(Game.getLevel().getPlayer()))
				HUD.addMessage("Neue Munition für " + w.getWeaponName() + " (" + bullets + " Schuss)");
			
			Game.getLevel().getEntities().remove(this);
		} else {
			if (w.getOwner().equals(Game.getLevel().getPlayer()))
				HUD.addMessage("Maximalanzahl an Munition für " + w.getWeaponName() + " überschritten");
		}
	}
	
	public int getBullets() {
		return bullets;
	}
}
