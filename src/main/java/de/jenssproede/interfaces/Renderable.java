package de.jenssproede.interfaces;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Renderable {
	public void render(Graphics g) ;
	public void update(GameContainer gc, int delta);	
}
