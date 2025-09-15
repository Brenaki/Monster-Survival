package config.combat;

import java.awt.Graphics2D;

import game.entity.base.Entity;

/**
 * 
 * @author bnk  
 */
public abstract class Projectile {
	protected Shooter owner;
	protected double x, y, vx, vy;
	protected int damage;
	protected float range;
	protected double traveled;

	public Projectile(Shooter owner, double x, double y, double vx, double vy, int damage, float range) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.vx = vy == 0 && vx == 0 ? 0.0001 : vx;
		this.vy = vy;
		this.damage = damage;
		this.range = range;
		this.traveled = 0;
	}

	public void update(double dt) {
		x += vx * dt;
		y += vy * dt;
		this.traveled += Math.sqrt(vx * vx + vy * vy) * dt;
	}

	public abstract void render(Graphics2D g2d);
	
	public boolean isExpired() {
		return this.traveled > this.range;
	}

	// Retorna true se colidiu (para remover o projétil)
	public boolean onHit(Entity target) {
		double dx = target.getX() - this.x;
		double dy = target.getY() - this.y;
		double dist = Math.sqrt(dx * dx + dy * dy);
		double rx = target.getColission().getrangeX();
		double ry = target.getColission().getrangeY();
		boolean hit = dist < (rx + ry) * 0.5;
		if (hit && target.getHealth() > 0) {
			target.setHealth(Math.max(0, target.getHealth() - this.damage));
		}
		return hit;
	}

	public Team getTeam() {
		return owner.getTeam();
	}

	// -- Getters --
	public double getX() { return x; }
	public double getY() { return y; }
	public double getVx() { return vx; }
	public double getVy() { return vy; }
	public int getDamage() { return damage; }
	public float getRange() { return range; }
	public double getTraveled() { return traveled; }

	// -- Setters --
	public void setVelocity(double vx, double vy) { this.vx = vx; this.vy = vy; }
}