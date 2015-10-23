package de.jenssproede.entities;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

import de.jenssproede.entities.weapons.Weapon;
import de.jenssproede.gfx.Crosshair;
import de.jenssproede.level.Level;
import de.jenssproede.main.Game;

public class Player extends Mob implements MouseListener {
	
	public int automaticCooldown = 50;
	public int nonAutomaticCooldown = 100;
	
	public static boolean noCollide = false;
	
	private float mouseX, mouseY;
	private Crosshair ch;
	private int deltaCounter;
	
	private int playerWidth;
	
	public Player(float x, float y, String internalName) {
		super(x, y, internalName);
		movementSpeed = 0.2f;
		ch = new Crosshair();
		deltaCounter = 0;
		
		weapons.add(new Weapon("Pistole", false, 10, this, 5, 0.8f, 30));
		weapons.add(new Weapon("Maschinengewehr", true, 30, this, 3, 0.8f, 60));
		
		selectedWeapon = 0;
		
		playerWidth = new Random().nextInt(6);
	}

	@Override
	public void render(Graphics g) {		
		super.render(g);
		
		g.pushTransform();
		
		mouseX += Level.getCamX();
		mouseY += Level.getCamY();
		
		g.setColor(new Color(255, 0, 0, 50));
		g.drawLine(getX() + getWidth() / 2, getY() + getHeight() / 2, mouseX, mouseY);
			
		centerX = getX() + getWidth() / 2;
		centerY = getY() + getHeight() / 2;
		
		angle = getTargetAngle(centerX, centerY, mouseX, mouseY);
		
		g.setColor(new Color(240, 100, 100));
		
		if (Game.drawHitboxes) {
			g.drawRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		}
		
		// Draw here the character (rotating is already done!!!)
		// Enable rotation
		g.rotate(centerX, centerY, angle);
		g.fillOval(centerX - 5, centerY - 25, 10, 50);		

		g.setColor(new Color(120, 210, 150));
		g.fillOval(getX(), getY(), getWidth() - playerWidth, getHeight());
		
		g.setColor(Color.gray);		
		g.drawOval(centerX + 10, centerY - 3, 6, 6);
		
		// Disable rotate
		g.rotate(centerX, centerY, -angle);
		// End drawing character (rotating is also disabled!)
		
		g.popTransform();
		
		ch.render(g);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		super.update(gc, delta);
		
		mouseX = gc.getInput().getMouseX();
		mouseY = gc.getInput().getMouseY();
		
		getActiveWeapon().update(gc, delta);
		
		ch.update(gc, delta);
		
		deltaCounter += delta;		
		
		float xa = 0, ya = 0;
		
		for (Mob m : Game.getLevel().getPlayers()) {
			if (m instanceof NPC) {
				NPC n = (NPC)m;
				float distance = getDistanceToPlayer(n);
				
				if (distance > 1000) {
					n.getActiveWeapon().setSoundVolume(0f);
				} else if (distance < 200) {
					n.getActiveWeapon().setSoundVolume(0.8f);
				} else {		
					n.getActiveWeapon().setSoundVolume( (1000-distance) / 1000);
				}
			}
		}
		
		if (Game.gameState == Game.GameState.INGAME) {
			if (gc.getInput().isKeyDown(Input.KEY_LSHIFT)) {
				movementSpeed = 0.7f;
			} else if (movementSpeed == 0.7f) {
				movementSpeed = 0.2f;
			}	
			
			if (gc.getInput().isKeyDown(Input.KEY_W)) {
				ya -= delta;
			}
			
			if (gc.getInput().isKeyDown(Input.KEY_S)) {
				ya += delta;
			}
			
			if (gc.getInput().isKeyDown(Input.KEY_A)) {
				xa -= delta;
			}
			
			if (gc.getInput().isKeyDown(Input.KEY_D)) {
				xa += delta;
			}
			
			if (xa != 0 || ya != 0) {			
				move(xa, ya, noCollide);
			}
			
			if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) && !getActiveWeapon().isAutomatic()) {
				if (deltaCounter >= nonAutomaticCooldown) {
					getActiveWeapon().shoot(
							centerX, 
							centerY,
							mouseX + Level.getCamX(), 
							mouseY + Level.getCamY());
					
					deltaCounter = 0;
				}
			} else if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && getActiveWeapon().isAutomatic()) {
				if (deltaCounter >= automaticCooldown) {
					getActiveWeapon().shoot(
							centerX, 
							centerY,
							mouseX + Level.getCamX(), 
							mouseY + Level.getCamY());
					
					deltaCounter = 0;
				}
			}
			
			if (gc.getInput().isKeyPressed(Input.KEY_R)) {
				getActiveWeapon().reload();
			}
			
			if (gc.getInput().isKeyPressed(Input.KEY_Q)) {
				Game.gameState = Game.GameState.CONSOLE;
			}
		}
	}
	
	public void equipNextWeapon() {
		if (selectedWeapon < weapons.size() - 1) {
			selectedWeapon++;
		}
	}
	
	public void equipLastWeapon() {
		if (selectedWeapon > 0) {
			selectedWeapon--;
		}
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAcceptingInput() {
		return (Game.gameState == Game.GameState.INGAME);
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) {
		
	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {
		
	}

	@Override
	public void mouseWheelMoved(int arg0) {
		if (arg0 < 0) {
			equipNextWeapon();
		} else if (arg0 > 0) {
			equipLastWeapon();
		}
	}
}
