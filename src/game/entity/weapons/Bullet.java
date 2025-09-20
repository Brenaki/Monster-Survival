package game.entity.weapons;

import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;

/**
 * 
 * @author bnk
 */
public class Bullet extends Projectile {
    public Bullet(Shooter owner, double x, double y, double vx, double vy) {
        super(owner, x, y, vx, vy, 10, 800f);
    }

    @Override
    public void render(Graphics2D g2d) {
        // Mantém apenas informações básicas aqui se necessário
    }
}
