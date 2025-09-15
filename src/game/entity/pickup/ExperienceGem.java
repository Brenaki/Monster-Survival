package game.entity.pickup;

import java.awt.Color;
import java.awt.Graphics2D;

import game.entity.base.Entity;

/**
 * Representa uma gema de experiência que pode ser coletada pelo player
 * @author bnk
 */
public class ExperienceGem extends Entity {
    private int experienceValue;
    private long spawnTime;
    private long lifeTime = 10000; // 10 segundos de vida
    
    public ExperienceGem(double x, double y, int experienceValue) {
        super(x, y, 0, 8, 8, 1, true, 8, 8);
        this.experienceValue = experienceValue;
        this.spawnTime = System.currentTimeMillis();
    }
    
    public void paint(Graphics2D g2d) {
        // Desenha a gema com cor baseada no valor de XP
        if (experienceValue >= 50) {
            g2d.setColor(Color.PINK); // Gema rosa para XP alto
        } else if (experienceValue >= 20) {
            g2d.setColor(Color.CYAN); // Gema ciano para XP médio
        } else {
            g2d.setColor(Color.GREEN); // Gema verde para XP baixo
        }
        
        g2d.fillOval((int) this.getX(), (int) this.getY(), this.getWidth(), this.getHeight());
        
        // Desenha um brilho ao redor
        g2d.setColor(Color.WHITE);
        g2d.drawOval((int) this.getX() - 1, (int) this.getY() - 1, this.getWidth() + 2, this.getHeight() + 2);
    }
    
    public boolean isExpired() {
        return (System.currentTimeMillis() - spawnTime) > lifeTime;
    }
    
    public int getExperienceValue() {
        return experienceValue;
    }
}
