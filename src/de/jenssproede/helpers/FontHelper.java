package de.jenssproede.helpers;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class FontHelper {
	
	private static List<UnicodeFont> fonts = new ArrayList<UnicodeFont>();
	
	@SuppressWarnings("unchecked")
	public static UnicodeFont getFontWithSize(int fontSize) {
		
		for (UnicodeFont f : fonts) {
			if (f.getFont().getSize() == fontSize) {
				return f;
			}
		}
		
		try {
			UnicodeFont newFont = new UnicodeFont("res/font/Tahoma.ttf", fontSize, false, false);
			newFont.addAsciiGlyphs();
			newFont.getEffects().add(new ColorEffect());
			newFont.loadGlyphs();
            return newFont;
		} catch (SlickException e) {
			return null;
		}
	}

	public static void drawString(UnicodeFont font, float x, float y, String drawString) {
		font.drawString(x, y, drawString);
	}
	
	public static void drawString(UnicodeFont font, float x, float y, String drawString, Color color) {
		font.drawString(x, y, drawString, color);
	}
	
	public static int getWidth(UnicodeFont font, String s) {
		return font.getWidth(s);
	}
	
}
