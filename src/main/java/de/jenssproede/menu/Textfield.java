package de.jenssproede.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;

public class Textfield implements Renderable, KeyListener {

	private int x, y, width, height;
	private StringBuffer input = new StringBuffer();
	private UnicodeFont font;	
	
	public Textfield(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		font = FontHelper.getFontWithSize(16);
	}
	
	public String getValue() {
		return input.toString();
	}
	
	@Override
	public void render(Graphics g) {
		g.setColor(new Color(240, 240, 240, 230));
		g.fillRect(x, y, width, height);		
		
		g.setColor(new Color(255, 255, 255, 255));
		g.drawRect(x, y, width, height);
		
		FontHelper.drawString(font, x + 5, y + 5, input.toString(), Color.black);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			Dialog.getInstance().doAction(gc, delta);
		}
	}

	@Override
	public void keyPressed(int arg0, char arg1) {
		if (arg0 == Input.KEY_BACK) {
			removeLastChar();
		} else if (isCharValid(arg1)) {
			input.append(arg1);
		}
	}
	
	public boolean isEmpty() {
		return (input.length() == 0);
	}
	
	public void clearCommand() {
		input.delete(0, input.length());
	}
	
	private void removeLastChar() {
		if (input.length() > 0)
			input.deleteCharAt(input.length() - 1);
	}
	
	private boolean isCharValid(char c) {
		return (Character.isLetter(c) || 
				Character.isDigit(c) || 
				c == ':' ||
				c == '.' || c == ' ');
	}

	@Override
	public boolean isAcceptingInput() {
		return Dialog.getInstance().isShouldShow();
	}


	@Override
	public void inputEnded() {}


	@Override
	public void inputStarted() {}


	@Override
	public void setInput(Input arg0) {}


	@Override
	public void keyReleased(int arg0, char arg1) {}
}
