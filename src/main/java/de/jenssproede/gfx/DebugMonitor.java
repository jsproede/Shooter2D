package de.jenssproede.gfx;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class DebugMonitor implements Renderable {

	public static boolean showDebugMonitor = false;
	
	private UnicodeFont font;
	
	private int FPS;
	
	public DebugMonitor(UnicodeFont font) {
		this.font = font;
		FPS = 0;
	}
	
	@Override
	public void render(Graphics g) {
		if (showDebugMonitor) {
			FontHelper.drawString(font, 5, 5, "Elemente in der Welt: " + Game.getLevel().getEntities().size());
			FontHelper.drawString(font, 5, 20, "Spieler (NPCs eingeschlossen) in der Welt: " + Game.getLevel().getPlayers().size());
			FontHelper.drawString(font, 5, 35, "FPS: " + String.valueOf(FPS));
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		FPS = gc.getFPS();
	}

}
