package game.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Shooter;
import config.combat.Team;
import game.entity.base.Entity;
import game.entity.weapons.Weapon;

/**
 * 
 * @author bnk
 */
public class Player extends Entity implements Shooter {
	private boolean moveUp;
	private boolean moveDown;
	private boolean moveLeft;
	private boolean moveRight;
	private Team team = Team.PLAYER;
	private double lookDirX = 1.0;
	private double lookDirY = 0.0;
	private Weapon activeWeapon;
	
	public Player(int x, int y, int speed, int width, int height, int health, boolean isVisible) {
		super(x, y, speed, width, height, health, isVisible, 22, 22);
		this.activeWeapon = new Weapon(x, y, Team.PLAYER, this);
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		g2d.fillRect((int) this.getX(), (int) this.getY(), 40, 40);
		
		super.paint(g2d);
		
		// Desenha a arma
		g2d.setColor(Color.GRAY);
		g2d.fillRect((int) activeWeapon.getX() - 5, (int) activeWeapon.getY() - 5, 10, 10);
	}

	public void setLookDir(double dx, double dy) {
		if (dx == 0 && dy == 0) return;
		double len = Math.sqrt(dx * dx + dy * dy);
		this.lookDirX = dx / len;
		this.lookDirY = dy / len;
	}
	
	public void updateWeaponPosition() {
		activeWeapon.updatePosition(this.getX(), this.getY(), lookDirX, lookDirY);
	}
	
    // -- Getters --
	public boolean getMoveUp() { return this.moveUp; }
	public boolean getMoveDown() { return this.moveDown; }
	public boolean getMoveLeft() { return this.moveLeft; }
	public boolean getMoveRight() { return this.moveRight; }
	public Team getTeam() { return this.team; }
	public double getLookDirX() { return this.lookDirX; }
	public double getLookDirY() { return this.lookDirY; }
	public Weapon getActiveWeapon() { return this.activeWeapon; }

	// -- Setters --
	public void setMoveUp(boolean moveUp) { this.moveUp = moveUp; }
	public void setMoveDown(boolean moveDown) { this.moveDown = moveDown; }
	public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
	public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }
}