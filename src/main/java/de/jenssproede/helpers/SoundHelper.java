package de.jenssproede.helpers;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundHelper {
	
	public static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	
	public static void playSound(String soundname) {
		
		if (sounds.containsKey(soundname)) {
			sounds.get(soundname).play();
		} else {
			try {
				sounds.put(soundname, new Sound("res/sounds/" + soundname + ".wav"));
				sounds.get(soundname).play();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}	
}
