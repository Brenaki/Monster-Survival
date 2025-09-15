package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;
import config.combat.Team;

/**
 * Lançador de granadas com dano em área
 * @author bnk
 */
public class GrenadeLauncher extends Weapon {
    
    public GrenadeLauncher(double x, double y, Team team, Shooter owner) {
        super(x, y, team, owner, "Lançador de Granadas", 0.5f, 60, 500f);
    }
    
    @Override
    public void render(Graphics2D g2d) {
        // Desenha o lançador como um tubo verde
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int) this.x - 4, (int) this.y - 2, 8, 4);
        
        // Desenha a mira
        g2d.setColor(Color.RED);
        g2d.drawLine((int) this.x - 6, (int) this.y, (int) this.x + 6, (int) this.y);
        g2d.drawLine((int) this.x, (int) this.y - 4, (int) this.x, (int) this.y + 4);
        
        // Desenha informações da arma
        g2d.setColor(Color.WHITE);
        g2d.drawString(name + " Lv." + level, (int) this.x - 35, (int) this.y - 10);
    }
    
    @Override
    public Projectile createProjectile(double dirX, double dirY) {
        return new Grenade(owner, this.x, this.y, dirX * 250, dirY * 250);
    }
}
