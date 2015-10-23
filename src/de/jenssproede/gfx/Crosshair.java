package de.jenssproede.gfx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.jenssproede.interfaces.Renderable;
import de.jenssproede.level.Level;

public class Crosshair implements Renderable {

	private float mouseX, mouseY;
	private float offset = 10;
	
	public Crosshair() {
		this.mouseX = 0;
		this.mouseY = 0;
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(new Color(255, 0, 0, 255));
		
		mouseX += Level.getCamX();
		mouseY += Level.getCamY();
		
		g.drawLine(mouseX - offset, mouseY, mouseX + offset, mouseY);
		g.drawLine(mouseX, mouseY - offset, mouseX, mouseY + offset);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		mouseX = gc.getInput().getMouseX();
		mouseY = gc.getInput().getMouseY();
	}
}
