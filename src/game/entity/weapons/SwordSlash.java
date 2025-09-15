package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;

/**
 * Projétil de corte de espada
 * @author bnk
 */
public class SwordSlash extends Projectile {
    public SwordSlash(Shooter owner, double x, double y, double vx, double vy) {
        super(owner, x, y, vx, vy, 40, 200f);
    }

    @Override
    public void render(Graphics2D g2d) {
        // Desenha o corte como uma linha azul brilhante
        g2d.setColor(Color.BLUE);
        g2d.drawLine((int) this.x - 5, (int) this.y - 5, (int) this.x + 5, (int) this.y + 5);
        g2d.drawLine((int) this.x + 5, (int) this.y - 5, (int) this.x - 5, (int) this.y + 5);
        
        // Efeito de brilho
        g2d.setColor(Color.CYAN);
        g2d.drawLine((int) this.x - 3, (int) this.y - 3, (int) this.x + 3, (int) this.y + 3);
        g2d.drawLine((int) this.x + 3, (int) this.y - 3, (int) this.x - 3, (int) this.y + 3);
    }
}
