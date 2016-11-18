package de.jenssproede.menu;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.Dimension;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class MainMenu implements Renderable {

	private final String HEADLINE = "H A U P T M E N Ü";
	private final String HELPSTRING = 
			"Wichtige Kürzel: (ESC) Hauptmenü | (Q) Konsole";
	private final String COPYRIGHTSTRING = "(c) 2013 - Jens Sproede"
			+ " - 'Shooter' benutzt: slick.ninjacave.com | lwjgl.org";
		
	private List<Button> menuItems;
	
	private UnicodeFont headlineFont;
	private UnicodeFont subFont;
	
	public MainMenu() {
		init();
	}
	
	public void init() {
		headlineFont = FontHelper.getFontWithSize(40);
		subFont = FontHelper.getFontWithSize(14);
		
		menuItems = new ArrayList<Button>();		
		menuItems.add(new Button("Lokales Spiel mit Bots starten"));
		menuItems.add(new Button("Bots zum Spiel hinzufügen"));
		menuItems.add(new Button("Zu einem Server verbinden"));
		menuItems.add(new Button("Benutzername ändern"));
		menuItems.add(new Button("Soundeffekte aktiviert"));
		menuItems.add(new Button("Spiel-Einstellungen", false));
		menuItems.add(new Button("Webseite des Entwicklers"));
		menuItems.add(new Button("Spiel beenden"));
		
		sortMenuItems();
	}
	
	private void sortMenuItems() {
		for (int i = 0; i < menuItems.size(); i++) {
			Button b = menuItems.get(i);
			b.setY((i * 70) + 115);
		}
	}
	
	@Override
	public void render(Graphics g) {		
		g.setColor(new Color(10, 10, 10, 150));
		Dimension windowSize = Game.getWindowSize();
		g.fillRect(0, 0, windowSize.getWidth(), windowSize.getHeight());
		
		g.setColor(Color.white);
		
		if (Game.getArguments() != null) {
			StringBuffer sb = new StringBuffer("Spielparameter: ");
			
			for (String s : Game.getArguments()) {
				sb.append(s + " ");
			}
			
			FontHelper.drawString(subFont, 5, 5, sb.toString());
		}
		
		FontHelper.drawString(headlineFont, 
				windowSize.getWidth() / 2 - FontHelper.getWidth(headlineFont, HEADLINE) / 2, 20, HEADLINE);
		
		FontHelper.drawString(subFont, 10, windowSize.getHeight() - subFont.getLineHeight() - 10, 
				HELPSTRING);
		
		FontHelper.drawString(subFont, windowSize.getWidth() - subFont.getWidth(COPYRIGHTSTRING) - 10, 
				windowSize.getHeight() - subFont.getLineHeight() - 10, COPYRIGHTSTRING);
		
		String welcomeString = "Hallo " + Game.getUsername() + "!";
		FontHelper.drawString(subFont, windowSize.getWidth() / 2 - FontHelper.getWidth(subFont, welcomeString) / 2, 80, 
				welcomeString);
		
		for (Button m : menuItems) {
			m.render(g);
		}
	}

	public void update(GameContainer gc, int delta) {
		if (Game.gameState == Game.GameState.MENU && !Dialog.getInstance().isShouldShow()) {
			for (Button m : menuItems) {
				m.setMouseOver(checkIfMouseOver(m, gc.getInput().getMouseX(), gc.getInput().getMouseY()));
			}
			
			if (gc.getInput().isMousePressed(0)) {
				for (Button m : menuItems) {
					if (m.isMouseOver() && m.isEnabled()) {
						hasClicked(m.getTitle(), gc);
					}
				}
			}
		}
	}	
	
	private void hasClicked(String title, GameContainer gc) {		
		switch (title.toLowerCase()) {
		case "lokales spiel mit bots starten":
			Game.gameState = Game.GameState.INGAME;
			break;
		case "spiel beenden":
			Dialog.getInstance().showDialog("Sind Sie sich sicher, dass Sie das Spiel beenden möchten?", "exit");
			break;
		case "zu einem server verbinden":
			Dialog.getInstance().showDialog("Geben Sie die IP Adresse ein, um sich zu einem Server zu verbinden!", "server_connect", true, "Verbinden");
			break;
		case "spiel-einstellungen":
			// TODO: Implementieren
			break;
		case "webseite des entwicklers":
			Dialog.getInstance().showDialog("Die Entwicklerwebseite wird in Ihrem Standard-Browser geöffnet. Fortfahren?", "website");
			break;
		case "soundeffekte aktiviert":
			
			Button b = getButtonForTitle(title);
			
			Game.setSound(false);
			b.setTitle("Soundeffekte deaktiviert");
			
			break;
		case "soundeffekte deaktiviert":
			
			Button btn = getButtonForTitle(title);
			
			Game.setSound(true);
			btn.setTitle("Soundeffekte aktiviert");
			
			break;
		case "benutzername ändern":
			Dialog.getInstance().showDialog("Geben Sie Ihren neuen Benutzernamen ein!", "username_change", true, "Ändern");
			break;
		case "bots zum spiel hinzufügen":
			Dialog.getInstance().showDialog("Geben Sie an, wie viele Bots hinzugefügt werden sollen:", "add_bots", true, "Hinzufügen");
			break;
		}
	}
	
	public Button getButtonForTitle(String title) {
		for (Button b : menuItems) {
			if (b.getTitle().equalsIgnoreCase(title)) {
				return b;
			}
		}
		
		return null;
	}
	
	public static boolean checkIfMouseOver(Button m, int mX, int mY) {
		if (mX >= m.getX() && mX <= m.getX() + m.getWidth() &&
				mY >= m.getY() && mY <= m.getY() + m.getHeight() && m.isEnabled()) {
			return true;
		}
		
		return false;
	}
}
