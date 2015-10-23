package de.jenssproede.entities;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.Texture;

import de.jenssproede.helpers.TextureHelper;
import de.jenssproede.main.Game;

public class Crate extends Entity {

	private Texture tex;
	
	public Crate(float x, float y) {		
		super(x, y, 128, 128);
		
		if (new Random().nextInt(20) > 11) {
			tex = TextureHelper.getTexture("crate");
		} else {
			tex = TextureHelper.getTexture("crate2");
		}
		
		setWidth(tex.getTextureWidth());
		setHeight(tex.getTextureHeight());
	}

	@Override
	public void render(Graphics g) {
		
		glPushMatrix();
		
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
		
		glPopMatrix();
	}

	@Override
	public void update(GameContainer gc, int delta) {
		// TODO Auto-generated method stub

	}
	
	public boolean intersectsWithCrate(Crate c) {
		return this.hitbox.intersects(c.hitbox);
	}
}
