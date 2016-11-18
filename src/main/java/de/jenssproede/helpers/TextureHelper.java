package de.jenssproede.helpers;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureHelper {

	public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
		
	public static Texture getTexture(String filename) {
		
		if (textures.containsKey(filename)) {
			return textures.get(filename);
		}
		
		try {
			Texture texture = TextureLoader.getTexture("PNG", 
					ResourceLoader.getResourceAsStream("res/images/" + filename + ".png"));
			textures.put(filename, texture);
			return texture;
		} catch (IOException e) {
			System.out.println("Error while loading texture '" + filename + ".png'!");
			e.printStackTrace();
			
			return null;
		}
	}	
}
