package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;

/**
 * Projétil de granada que explode
 * @author bnk
 */
public class Grenade extends Projectile {
    private boolean exploded = false;
    
    public Grenade(Shooter owner, double x, double y, double vx, double vy) {
        super(owner, x, y, vx, vy, 60, 400f);
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!exploded) {
            // Desenha a granada como um círculo verde
            g2d.setColor(Color.GREEN);
            g2d.fillOval((int) this.x - 4, (int) this.y - 4, 8, 8);
            
            // Desenha o pino
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) this.x - 2, (int) this.y - 6, 4, 2);
        } else {
            // Desenha a explosão
            g2d.setColor(Color.ORANGE);
            g2d.fillOval((int) this.x - 15, (int) this.y - 15, 30, 30);
            g2d.setColor(Color.RED);
            g2d.fillOval((int) this.x - 10, (int) this.y - 10, 20, 20);
        }
    }
    
    @Override
    public boolean onHit(game.entity.base.Entity target) {
        if (!exploded) {
            exploded = true;
            // Granada causa dano em área maior
            return super.onHit(target);
        }
        return false;
    }
    
    public boolean isExploded() {
        return exploded;
    }
}
