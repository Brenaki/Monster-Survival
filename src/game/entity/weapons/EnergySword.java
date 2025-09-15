package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;
import config.combat.Team;

/**
 * Espada que cria cortes de energia
 * @author bnk
 */
public class EnergySword extends Weapon {
    
    public EnergySword(double x, double y, Team team, Shooter owner) {
        super(x, y, team, owner, "Espada de Energia", 0.8f, 40, 200f);
    }
    
    @Override
    public void render(Graphics2D g2d) {
        // Desenha a empunhadura da espada
        g2d.setColor(new Color(139, 69, 19)); // Marrom
        g2d.fillRect((int) this.x - 2, (int) this.y - 6, 4, 8);
        
        // Desenha a lâmina da espada
        g2d.setColor(Color.GRAY);
        g2d.fillRect((int) this.x - 1, (int) this.y - 12, 2, 12);
        
        // Desenha o brilho da energia
        g2d.setColor(Color.BLUE);
        g2d.drawRect((int) this.x - 1, (int) this.y - 12, 2, 12);
        
        // Desenha informações da arma
        g2d.setColor(Color.WHITE);
        g2d.drawString(name + " Lv." + level, (int) this.x - 30, (int) this.y - 20);
    }
    
    @Override
    public Projectile createProjectile(double dirX, double dirY) {
        return new SwordSlash(owner, this.x, this.y, dirX * 300, dirY * 300);
    }
}
