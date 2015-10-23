package de.jenssproede.menu;

import java.net.URI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.helpers.EntityHelper;
import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class Dialog implements Renderable {

	public static enum DialogResult {
		YES, NO;
	}
	
	private static Dialog dialog = null;	
	public static Dialog getInstance() {
		if (dialog == null) {
			dialog = new Dialog();
		}
		
		return dialog;
	}	
	
	private String message;
	private String toDo;
	
	private Button yes;
	private Button no;
	private Textfield textField;
	private int buttonWidth = 100;
	
	private int x, y;	
	private int width, height;
	
	private boolean shouldShow = false;
	private boolean showInputField = false;
	
	private UnicodeFont font = FontHelper.getFontWithSize(18);
	
	private Dialog() {		
		
		width = Game.getWindowSize().getWidth() / 2;
		height = 140;
		
		x = Game.getWindowSize().getWidth() / 2 - width / 2;
		y = Game.getWindowSize().getHeight() / 2 - height / 2;
		
		int middle = width / 2;
				
		
		yes = new Button("Ja", x + middle - buttonWidth - 10, y + height - 50, buttonWidth);
		no = new Button("Nein", x + middle + 10, y + height - 50, buttonWidth);
		textField = new Textfield(x + 10, y + height - 70, width - 20, 30);
	}
	
	public void showDialog(String message, String toDo) {
		
		this.message = message;
		this.toDo = toDo;
		this.showInputField = false;
		
		yes.setTitle("Ja");
		no.setTitle("Nein");
		
		height = 140;
		
		yes.setY(y + height - 50);
		no.setY(y + height - 50);
		
		setShouldShow(true);
	}
	
	public void showDialog(String message, String toDo, boolean showInputField, String yesButton) {
		showDialog(message, toDo, showInputField);
		yes.setTitle(yesButton);
	}
	
	public void showDialog(String message, String toDo, boolean showInputField) {
		showDialog(message, toDo);
		this.showInputField = showInputField;
		textField.clearCommand();
		
		height = 180;
		
		yes.setTitle("OK");
		no.setTitle("Abbrechen");
		yes.setY(y + height - 50);
		no.setY(y + height - 50);
	}
	
	@Override
	public void render(Graphics g) {
		if (isShouldShow()) {
			g.setColor(new Color(0f, 0f, 0f, 0.5f));
			g.fillRect(0, 0, Game.getWindowSize().getWidth(), Game.getWindowSize().getHeight());
			
			g.setColor(new Color(0f, 0f, 0f, 0.9f));
			g.fillRect(x - 1, y - 1, width + 1, height + 1);
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
			
			FontHelper.drawString(font, x + width / 2 - font.getWidth(message) / 2, y + 30, message);
			
			if (showInputField) {
				textField.render(g);
			}
			
			yes.render(g);
			no.render(g);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (isShouldShow()) {
			yes.setMouseOver(MainMenu.checkIfMouseOver(yes, gc.getInput().getMouseX(), gc.getInput().getMouseY()));
			no.setMouseOver(MainMenu.checkIfMouseOver(no, gc.getInput().getMouseX(), gc.getInput().getMouseY()));
		
			if (gc.getInput().isMousePressed(0)) {
				if (yes.isMouseOver()) {
					doAction(gc, delta);
				} else if (no.isMouseOver()) {
					this.shouldShow = false;
				}
			}
			
			if (showInputField) {
				textField.update(gc, delta);
			}
		}
	}
	
	public void doAction(GameContainer gc, int delta) {
		switch (toDo.toLowerCase()) {
		case "exit":
			gc.exit();
			break;
		case "website":
			try {
				java.awt.Desktop.getDesktop().browse(new URI("http://jenssproede.de/"));
			} catch (Exception e) {}
			break;
		case "server_connect":
			if (textField.isEmpty()) {
				return;
			} else {
				Game.connectToServer(textField.getValue());
			}
			
			break;
		case "username_change":
			if (textField.isEmpty()) {
				return;
			} else {
				Game.setUsername(textField.getValue());
			}
			
			break;
		case "add_bots":
			if (textField.isEmpty()) {
				return;
			} else {
				EntityHelper.addNPCs(Integer.parseInt(textField.getValue()));
			}
			break;
		default:
			System.out.println("Leere Eingabe -- Abbruch");
			return;
		}
		
		this.shouldShow = false;
	}
	
	public boolean isShouldShow() {
		return shouldShow;
	}

	public void setShouldShow(boolean shouldShow) {
		this.shouldShow = shouldShow;
	}

	public Textfield getTextfield() {
		return textField;
	}
}
