package de.jenssproede.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import de.jenssproede.helpers.FontHelper;
import de.jenssproede.interfaces.Renderable;
import de.jenssproede.main.Game;

public class Button implements Renderable {

	private String title;
	private float x, y, width, height;
	private UnicodeFont font;
	
	private int paddingY = 12;
	
	private boolean isMouseOver = false;
	private boolean enabled = true;
	
	public Button(String title, boolean enabled) {
		this(title);
		
		this.enabled = enabled;
	}
	
	public Button(String title) {
		this(title, 0);
	}
	
	public Button(String title, float y) {
		this.title = title;
		this.y = y;
		this.width = Game.getWindowSize().getWidth() / 2;
		
		font = FontHelper.getFontWithSize(20);
		this.x = Game.getWindowSize().getWidth() / 2 - this.width / 2;
		this.height = font.getHeight(title) + (paddingY + 5);
	}
	
	public Button(String title, float x, float y, float width) {
		this.title = title;
		this.x = x;
		this.y = y;
		
		font = FontHelper.getFontWithSize(14);
		this.width = width;
		this.height = font.getHeight(title) + (paddingY + 5);
	}

	@Override
	public void render(Graphics g) {
		if (isEnabled() && isMouseOver()) {
			g.setColor(new Color(100, 100, 100, 240));
		} else if (!isEnabled()) {
			g.setColor(new Color(10, 10, 10, 240));
		} else {
			g.setColor(new Color(50, 50, 50, 240));
		}	
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.white);
		g.drawRect(x - 1, y - 1, width + 1, height + 1);
		FontHelper.drawString(font, x + (width / 2 - font.getWidth(title) / 2), y + paddingY / 2 + 2, title);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}

	public void setMouseOver(boolean isMouseOver) {
		this.isMouseOver = isMouseOver;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
