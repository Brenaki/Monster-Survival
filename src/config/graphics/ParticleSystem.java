package config.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Sistema de partículas para efeitos visuais 8-bit
 * @author bnk
 */
public class ParticleSystem {
    
    private List<Particle> particles;
    
    public ParticleSystem() {
        this.particles = new ArrayList<>();
    }
    
    /**
     * Adiciona explosão de partículas na posição especificada
     */
    public void addExplosion(double x, double y, int count) {
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2 * i) / count;
            double speed = 50 + Math.random() * 100;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            
            Particle particle = new Particle(x, y, vx, vy, 0.5 + Math.random() * 0.5);
            particles.add(particle);
        }
    }
    
    /**
     * Adiciona efeito de coleta de XP
     */
    public void addXPCollection(double x, double y) {
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * Math.PI * 2;
            double speed = 20 + Math.random() * 30;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            
            Particle particle = new Particle(x, y, vx, vy, 0.3 + Math.random() * 0.4);
            particle.setColor(Color.CYAN);
            particles.add(particle);
        }
    }
    
    /**
     * Atualiza todas as partículas
     */
    public void update(double deltaTime) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = it.next();
            particle.update(deltaTime);
            
            if (particle.isDead()) {
                it.remove();
            }
        }
    }
    
    /**
     * Renderiza todas as partículas
     */
    public void render(Graphics2D g2d) {
        for (Particle particle : particles) {
            particle.render(g2d);
        }
    }
    
    /**
     * Limpa todas as partículas
     */
    public void clear() {
        particles.clear();
    }
    
    /**
     * Classe interna para representar uma partícula individual
     */
    private static class Particle {
        private double x, y;
        private double vx, vy;
        private double life;
        private double maxLife;
        private Color color;
        private double size;
        
        public Particle(double x, double y, double vx, double vy, double life) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.life = life;
            this.maxLife = life;
            this.color = Color.YELLOW;
            this.size = 2 + Math.random() * 3;
        }
        
        public void update(double deltaTime) {
            x += vx * deltaTime;
            y += vy * deltaTime;
            life -= deltaTime;
            
            // Aplica fricção
            vx *= 0.98;
            vy *= 0.98;
        }
        
        public void render(Graphics2D g2d) {
            if (life <= 0) return;
            
            double alpha = life / maxLife;
            Color renderColor = new Color(
                (int)(color.getRed() * alpha),
                (int)(color.getGreen() * alpha),
                (int)(color.getBlue() * alpha)
            );
            
            g2d.setColor(renderColor);
            
            // Desenha partícula usando GeneralPath
            GeneralPath particleShape = new GeneralPath();
            particleShape.moveTo(x, y);
            particleShape.lineTo(x + size, y);
            particleShape.lineTo(x + size/2, y + size);
            particleShape.closePath();
            
            g2d.fill(particleShape);
        }
        
        public boolean isDead() {
            return life <= 0;
        }
        
        public void setColor(Color color) {
            this.color = color;
        }
    }
}
