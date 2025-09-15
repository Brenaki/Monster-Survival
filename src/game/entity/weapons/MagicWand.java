package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;
import config.combat.Team;

/**
 * Varinha mágica que dispara fireballs
 * @author bnk
 */
public class MagicWand extends Weapon {
    
    public MagicWand(double x, double y, Team team, Shooter owner) {
        super(x, y, team, owner, "Varinha Mágica", 1.5f, 25, 400f);
    }
    
    @Override
    public void render(Graphics2D g2d) {
        // Desenha a varinha como um bastão marrom
        g2d.setColor(new Color(139, 69, 19)); // Marrom
        g2d.fillRect((int) this.x - 1, (int) this.y - 8, 2, 16);
        
        // Desenha a ponta mágica (cristal)
        g2d.setColor(Color.CYAN);
        g2d.fillOval((int) this.x - 3, (int) this.y - 10, 6, 6);
        
        // Efeito de brilho
        g2d.setColor(Color.WHITE);
        g2d.drawOval((int) this.x - 3, (int) this.y - 10, 6, 6);
        
        // Desenha informações da arma
        g2d.setColor(Color.WHITE);
        g2d.drawString(name + " Lv." + level, (int) this.x - 25, (int) this.y - 15);
    }
    
    @Override
    public Projectile createProjectile(double dirX, double dirY) {
        return new Fireball(owner, this.x, this.y, dirX * 350, dirY * 350);
    }
}
