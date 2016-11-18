package de.jenssproede.main;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.SoundStore;

import de.jenssproede.gfx.Console;
import de.jenssproede.gfx.DebugMonitor;
import de.jenssproede.gfx.HUD;
import de.jenssproede.helpers.EntityHelper;
import de.jenssproede.level.Level;
import de.jenssproede.menu.Dialog;
import de.jenssproede.menu.MainMenu;

public class Game extends BasicGame {

	private static final String TITLE = "Shooter 2D";
	
	public enum GameState {
		MENU, INGAME, CONSOLE, EXIT
	}
	
	public static GameState gameState = GameState.MENU;
	public static boolean drawHitboxes = false;
	public static boolean npcShoot = true;	
	
	private static String username = "not set";
	private static Level level;
	private static String[] arguments;
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = WIDTH / 16 * 9;
	private static boolean sound = true;
	
	private MainMenu menu;
	private HUD hud;	
	private Console console;
	
	public Game() {
		super("Das Spiel wird geladen! Bitte warten Sie ...");
	}
	
	public static void connectToServer(String server) {
		System.out.println("Connecting to " + server);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {		
		// Recognize which gamestate is active		

		level.render(g);
		
		switch (gameState) {
		case MENU:
			menu.render(g);
			break;
		case INGAME:
			hud.render(g);
			break;
		case CONSOLE:
			console.render(g);
			break;
		case EXIT:
			gc.exit();
			break;
		}
		
		Dialog.getInstance().render(g);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		menu = new MainMenu();
		level = new Level(2000, 2000);
		hud = new HUD();
		console = new Console();
		
		gc.getInput().addMouseListener(level.getPlayer());
		gc.getInput().addKeyListener(console);
		gc.getInput().addKeyListener(Dialog.getInstance().getTextfield());
		
		for (int i = 0; i < arguments.length; i++) {
			String s = arguments[i];
			
			if (s.startsWith("-")) {
			switch (s.replace("-", "").toLowerCase()) {
				case "nosound":
					Game.sound = false;
					menu.getButtonForTitle("soundeffekte aktiviert").setTitle("Soundeffekte deaktiviert");
					break;
				case "debug":
					DebugMonitor.showDebugMonitor = true;
					break;
				case "username":
					setUsername(arguments[i+1]);
					break;
				default:
					System.out.println("There are some invalid arguments!");
					break;
				}
			}
		}
		
		EntityHelper.generateCrateLevel();
		
		Display.setTitle(TITLE);
		
		// Enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    	GL11.glViewport(0,0,WIDTH,HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		gc.setSoundVolume(0.6f);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		gc.setSoundOn(sound);
		
		switch (gameState) {
		case MENU:
			showMouse(true);
			menu.update(gc, delta);
			
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE) &&
					!Dialog.getInstance().isShouldShow()) {
				gc.getInput().clearKeyPressedRecord();
				gameState = GameState.INGAME;
			}
			
			break;
		case INGAME:
			showMouse(false);
			hud.update(gc, delta);
			level.update(gc, delta);	
			
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				gc.getInput().clearKeyPressedRecord();
				gameState = GameState.MENU;
			}
			
			break;
		case CONSOLE:
			showMouse(true);
			console.update(gc, delta);
			
			if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
				console.clearCommand();
				gc.getInput().clearKeyPressedRecord();
				gameState = GameState.INGAME;
			}
			
			break;
		case EXIT:			
			AL.destroy();
			gc.exit();
			break;
		}
		
		if (Dialog.getInstance().isShouldShow())
			Dialog.getInstance().update(gc, delta);
	}
	
	public static void showMouse(boolean show) {
		if (show) {
			try {
				Mouse.setNativeCursor(null);
				Mouse.updateCursor();
			} catch (LWJGLException e1) {
				JOptionPane.showMessageDialog(null, "Es gab Probleme beim Wiederherstellen Ihres Mauszeigers.");
			}
		} else {
			Cursor emptyCursor;
			try {
				emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
				Mouse.setNativeCursor(emptyCursor);
			} catch (LWJGLException e) {
				JOptionPane.showMessageDialog(null, "Ihr Mauszeiger konnte nicht versteckt werden!");
			}
		}
	}
	
	public static void setSound(boolean sound) {
		Game.sound = sound;
	}
	
	public static Dimension getWindowSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
			
	public static Level getLevel() {
		return level;
	}
	
	public static void main (String[] args) throws SlickException {
		arguments = args;
		
		SoundStore.get().init();
		AppGameContainer container = new AppGameContainer(new Game());		
		container.setDisplayMode(WIDTH, HEIGHT, false);
		container.setShowFPS(false);
		container.setVSync(true);
		container.start();
	}
	
	public static void setUsername(String username) {
		Game.username = username;
		getLevel().getPlayer().setInternalName(username);
	}

	public static String getUsername() {
		return Game.username;
	}
	
	public static String[] getArguments() {
		return arguments;
	}
}
