package game.entity.enemy;

import java.awt.Graphics2D;

import config.combat.Team;
import game.entity.base.Entity;

/**
 * 
 * @author bnk
 */
public class Enemy extends Entity {
    private double directionX;
    private double directionY;
    private Team team = Team.ENEMY;
    private String enemyType;
    private int baseHealth;
    private double baseSpeed;
    private long lastDamageTime;
    private long damageCooldown = 500; // 0,5 segundo entre danos

    public Enemy(double x,
            double y,
            double speed,
            int width,
            int height,
            int health,
            boolean isVisible,
            long cooldown,
            String enemyType) {
        super(x, y, speed, width, height, health, isVisible, 22, 22);
        this.damageCooldown = cooldown;
        this.enemyType = enemyType;
        this.baseHealth = health;
        this.baseSpeed = speed;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public void paint(Graphics2D g2d) {
        // Mantém apenas a barra de vida aqui
        super.paint(g2d);
    }

    public void followPlayer(double pX, double pY) {
        // Calcula a direção para o player
        this.setDirectionX(pX - this.getX());
        this.setDirectionY(pY - this.getY());

        // Calcula distância até o player
        double distance = Math.sqrt(Math.pow(this.getDirectionX(), 2) + Math.pow(this.getDirectionY(), 2));

        if (distance > this.getColission().getrangeX() && distance > this.getColission().getrangeY()) {
            // Normaliza a direção
            this.setDirectionX(this.getDirectionX() / distance);
            this.setDirectionY(this.getDirectionY() / distance);

            // Move o inimigo na direção do player
            this.setX(this.getX() + (this.getDirectionX() * this.getSpeed()));
            this.setY(this.getY() + (this.getDirectionY() * this.getSpeed()));
        }
    }

    public boolean canDamage() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastDamageTime) >= damageCooldown;
    }
    
    public void dealDamage() {
        this.lastDamageTime = System.currentTimeMillis();
    }

    // -- Getters --
    public double getDirectionX() { return this.directionX; }
    public double getDirectionY() { return this.directionY; }
    public Team getTeam() { return this.team; }
    public String getEnemyType() { return this.enemyType; }
    public int getBaseHealth() { return this.baseHealth; }
    public double getBaseSpeed() { return this.baseSpeed; }

    // -- Setters --
    public void setDirectionX(double directionX) { this.directionX = directionX; }
    public void setDirectionY(double directionY) { this.directionY = directionY; }
}
