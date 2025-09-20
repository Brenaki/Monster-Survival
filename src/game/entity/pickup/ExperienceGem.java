package game.entity.pickup;

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
        // Mantém apenas informações básicas aqui se necessário
    }
    
    public boolean isExpired() {
        return (System.currentTimeMillis() - spawnTime) > lifeTime;
    }
    
    public int getExperienceValue() {
        return experienceValue;
    }
}
