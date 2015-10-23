package de.jenssproede.gfx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.entities.Player;
import de.jenssproede.helpers.EntityHelper;
import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class Console implements Renderable, KeyListener {

	public static boolean showConsole = false;
	
	private UnicodeFont font;
	
	private StringBuffer inputString;
	private String messageString = "";
	private String lastCommand = "";
	
	private int width = Game.getWindowSize().getWidth();
	private int height = 65;
	
	public Console() {
		this.font = FontHelper.getFontWithSize(14);
		this.inputString = new StringBuffer();
	}
	
	public void addToString(char keyChar) {
		if (inputString.length() < 100) 
			inputString.append(keyChar);
		else
			messageString = "Sie können maximal 120 Zeichen pro Befehl eingeben!";
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(50, 50, 50, 220));
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.black);
		g.drawLine(0, height, width, height);
		g.drawLine(0, height - 22, width, height - 22);
				
		g.setColor(Color.white);
		
		FontHelper.drawString(font, 5, height - 20, "Drücken Sie 'ESC', um die Konsole zu verlassen. Drücken Sie [Pfeil-Oben] um den letzten Befehl einzugeben.");
		
		FontHelper.drawString(font, 5, 5, "Ihr Befehl: " + inputString.toString());
		FontHelper.drawString(font, 5, 20, messageString);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}

	@Override
	public void keyPressed(int keyCode, char keyChar) {
		if (keyCode == Input.KEY_ENTER) {
			sendCommand();
		} else if (keyCode == Input.KEY_BACK) {
			removeLastChar();
		} else if (keyCode == Input.KEY_DELETE) {
			clearCommand();
		} else if (keyCode == Input.KEY_UP) {
			clearCommand();
			inputString.append(lastCommand);
		} else if (isCharValid(keyChar)) {
			addToString(keyChar);
		}
	}

	private boolean isCharValid(char c) {
		return (Character.isLetter(c) || 
				Character.isDigit(c) || 
				Character.isSpaceChar(c) ||
				c == '/' ||
				c == '.');
	}
	
	private void sendCommand() {
		String command = inputString.toString();
		clearCommand();
		lastCommand = command;
		
		if (command.startsWith("addnpcs")) {
			int mobs = Integer.parseInt(command.replace("addnpcs ", ""));
			EntityHelper.addNPCs(mobs);
			messageString = "Es wurden " + mobs + " NPCs hinzugefügt!";
		} else if (command.startsWith("npcshoot")) {
			boolean shoot = Boolean.parseBoolean(command.replace("npcshoot ", ""));
			Game.npcShoot = shoot;
			
			if (shoot)
				messageString = "Die NPCs schiessen jetzt wieder!";
			else
				messageString = "Die NPCs schiessen jetzt nicht mehr!";
		} else if (command.startsWith("noclip")) {
			Player.noCollide = !Player.noCollide;
			messageString = "Noclip wurde " + (Player.noCollide ? "aktiviert":"deaktiviert") + "!";
		} else if (command.startsWith("hitbox")) {
			Game.drawHitboxes = !Game.drawHitboxes;
			messageString = "Hitboxes werden " + (Game.drawHitboxes ? "angezeigt":"ausgeblendet") + "!";
		} else if (command.startsWith("camlock")) {
			Game.getLevel().toggleCamLock();
			messageString = "Befehl 'camlock' wurde ausgeführt!";
		} else if (command.startsWith("clearnpcs")) {
			Game.getLevel().clearAllBots();
			messageString = "Alle NPCs wurden entfernt!";
		} else if (command.startsWith("clearentities")) {
			Game.getLevel().clearAllEntities();	
			messageString = "Alle Entities wurden entfernt!";
		} else if (command.startsWith("generatelevel")) {
			Game.getLevel().clearAllEntities();	
			EntityHelper.generateCrateLevel();
			messageString = "Es wurde ein neues Level generiert!";
		} else if (command.startsWith("remainingbullets")) {
			int rem = Integer.parseInt(command.replace("remainingbullets ", ""));
			Game.getLevel().getPlayer().getActiveWeapon().setRemainingBullets(rem);
			messageString = "Es sind noch " + rem + " Kugeln übrig!";
		} else if (command.startsWith("addpacks")) {
			int packs = Integer.parseInt(command.replace("addpacks ", ""));
			EntityHelper.spawnRefillPacks(packs);
			messageString = "Es wurden " + packs + " RefillPacks hinzugefügt!";
		} else if (command.startsWith("fakemsg")) {
			HUD.addMessage(command.replace("fakemsg ", ""));
			messageString = "Fake Nachricht hinzugefügt!";
		} else if (command.startsWith("god")) {
			Player.god = !Player.god;
			messageString = "Godmode wurde " + (Player.god ? "aktiviert":"deaktiviert") + "!";
		} else if (command.startsWith("clearhistory")) {
			HUD.deleteMessages();
			messageString = "Alle Nachrichten wurden gelöscht!";
		} else if (command.startsWith("debug")) {
			DebugMonitor.showDebugMonitor = !DebugMonitor.showDebugMonitor;
			messageString = "Debug Monitor wird " + (DebugMonitor.showDebugMonitor ? "angezeigt":"ausgeblendet") + "!";
		} else if (command.startsWith("help")) {
			messageString = "Hilfe: addnpcs [amount]; npcshoot [true|false]; noclip; hitbox; camlock; clearnpcs; clearentities; generatelevel; remainingbullets [amount]; "
					+ "addpacks [amount]; fakemsg [message]; god; clearhistory; debug";
		} else if (command.startsWith("infiniteammo")) {
			Game.getLevel().getPlayer().getActiveWeapon().setShots(99999999);
			messageString = "Sie haben nun unbegrenzt Munition!";
		} else if (command.startsWith("cooldown")) {
			Game.getLevel().getPlayer().automaticCooldown = Integer.parseInt(command.replace("cooldown ", ""));
			messageString = "Cooldownwert wurde geändert!";
		}
		else if (command.startsWith("zoom")) {
			float zoomLevel = Float.valueOf(command.replace("zoom ", ""));
			Game.getLevel().zoom = zoomLevel;
		} else {
			messageString = "Der Befehl '" + command + "' existiert leider nicht!";
			inputString.append("help");
		}
	}
	
	public void clearCommand() {
		inputString.delete(0, inputString.length());
	}
	
	private void removeLastChar() {
		if (inputString.length() > 0)
			inputString.deleteCharAt(inputString.length() - 1);
	}

	@Override
	public boolean isAcceptingInput() { return (Game.gameState == Game.GameState.CONSOLE); }

	@Override
	public void inputEnded() {}

	@Override
	public void inputStarted() {}

	@Override
	public void setInput(Input arg0) {}

	@Override
	public void keyReleased(int arg0, char arg1) {}
}
