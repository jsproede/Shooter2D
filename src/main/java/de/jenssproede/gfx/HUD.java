package de.jenssproede.gfx;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;

import de.jenssproede.entities.weapons.Weapon;
import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class HUD implements Renderable {

	private static List<String> infoMessages = new ArrayList<String>();
	
	private UnicodeFont font;
	private UnicodeFont msgFont;
	
	private Weapon equipedWeapon = null;
	private Texture weaponImage = null;
	
	private DebugMonitor debugMonitor = null;
	
	
	public HUD() {
		font = FontHelper.getFontWithSize(14);
		msgFont = FontHelper.getFontWithSize(16);
		debugMonitor = new DebugMonitor(font);
	}

	@Override
	public void render(Graphics g) {
		g.pushTransform();

		if (equipedWeapon != null) {
			Dimension winSize = Game.getWindowSize();
			
			FontHelper.drawString(font, 5, winSize.getHeight() - 20, equipedWeapon.getRemainingShots() + " | " + equipedWeapon.getRemainingBullets() +
					( (equipedWeapon.getRemainingShots() == 0 && !equipedWeapon.isReloading() && equipedWeapon.getRemainingBullets() > 0) ? " (R drücken zum Nachladen)" : "") +
					(equipedWeapon.isReloading() ? " (Lädt nach ...)":"") + (equipedWeapon.isEmpty() ? " (Waffe ist leer, Finden Sie Munitionspakete!)" : ""));
			
			if (weaponImage != null) {
				Color.white.bind();
				weaponImage.bind();
				
				int posY = 20;
				
				glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2f(5, winSize.getHeight() - weaponImage.getImageHeight() - posY);
					glTexCoord2f(1, 0);
					glVertex2f(5 + weaponImage.getImageWidth(), winSize.getHeight() - weaponImage.getImageHeight() - posY);
					glTexCoord2f(1, 1);
					glVertex2f(5 + weaponImage.getImageWidth(), winSize.getHeight() - posY);
					glTexCoord2f(0, 1);
					glVertex2f(5, winSize.getHeight() - posY);
				glEnd();
			}
		}
		
		debugMonitor.render(g);
		
		if (!infoMessages.isEmpty()) {
			for (int i = 0; i < infoMessages.size() && i < 8; i++) {
				String msg = infoMessages.get(i);
				int stringWidth = FontHelper.getWidth(msgFont, msg);
				
				FontHelper.drawString(msgFont, Game.getWindowSize().getWidth() - stringWidth - 10, (i * 18) + 5, msg,
						new Color(255, 255, 255, 160));
			}
		}

		if (DebugMonitor.showDebugMonitor) {
			g.setColor(Color.gray);
			g.drawLine(0, (Game.HEIGHT / 2), Game.WIDTH, (Game.HEIGHT / 2));
			g.drawLine(Game.WIDTH / 2, 0, Game.WIDTH / 2, Game.HEIGHT);
		}

		g.popTransform();	
	}

	@Override
	public void update(GameContainer gc, int delta) {
		equipedWeapon = Game.getLevel().getPlayer().getActiveWeapon();
		weaponImage = equipedWeapon.getImage();
		
		debugMonitor.update(gc, delta);
	}
	
	public static void addMessage(String msg) {
		if (infoMessages.size() > 0 && infoMessages.get(0).equals(msg)) {
			// Nachricht nicht hinzufügen
		} else {
			infoMessages.add(0, msg);
		}
	}	
	
	public static void deleteMessages() {
		infoMessages.clear();
	}
}
