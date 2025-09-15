package game.entity.weapons;

import java.awt.Color;
import java.awt.Graphics2D;

import config.combat.Projectile;
import config.combat.Shooter;

/**
 * 
 * @author bnk
 */
public class Fireball extends Projectile {
    public Fireball(Shooter owner, double x, double y, double vx, double vy) {
        super(owner, x, y, vx, vy, 20, 800f);
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.ORANGE);
        g2d.fillRect((int) this.x, (int) this.y, 20, 20);
    }
}
