package game.entity.enemy;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.CombatManager;
import config.combat.Team;
import game.entity.base.Entity;

/**
 * 
 * @author bnk
 */
public class Enemy extends Entity {
    private double directionX;
    private double directionY;
    private CombatManager combat;
    private Team team = Team.ENEMY;

    public Enemy(double x,
            double y,
            double speed,
            int width,
            int height,
            int health,
            boolean isVisible,
            long cooldown) {
        super(x, y, speed, width, height, health, isVisible, 22, 22);
        this.combat = new CombatManager(cooldown);
    }

    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int) this.getX(), (int) this.getY(), 40, 40);
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

    // -- Getters --
    public double getDirectionX() { return this.directionX; }
    public double getDirectionY() { return this.directionY; }
    public CombatManager getCombat() { return this.combat; }
    public Team getTeam() { return this.team; }

    // -- Setters --
    public void setDirectionX(double directionX) { this.directionX = directionX; }
    public void setDirectionY(double directionY) { this.directionY = directionY; }
}
