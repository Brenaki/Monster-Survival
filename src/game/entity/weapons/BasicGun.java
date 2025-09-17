package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;
import config.combat.Team;

/**
 * Arma básica do jogo - Pistola simples
 * @author bnk
 */
public class BasicGun extends Weapon {
    
    public BasicGun(double x, double y, Team team, Shooter owner) {
        super(x, y, team, owner, "Pistola Básica", 3.0f, 15, 300f);
    }
    
    @Override
    public void render(Graphics2D g2d) {
        // Desenha a pistola como um retângulo cinza
        g2d.setColor(Color.GRAY);
        g2d.fillRect((int) this.x - 3, (int) this.y - 2, 6, 4);
        
        // Desenha o cano
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int) this.x + 3, (int) this.y - 1, 4, 2);
        
        // Desenha informações da arma
        g2d.setColor(Color.WHITE);
        g2d.drawString(name + " Lv." + level, (int) this.x - 20, (int) this.y - 10);
    }
    
    @Override
    public Projectile createProjectile(double dirX, double dirY) {
        return new Bullet(owner, this.x, this.y, dirX * 400, dirY * 400);
    }
}
