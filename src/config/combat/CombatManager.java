package config.combat;

import game.entity.weapons.Fireball;

/**
 * 
 * @author bnk
 */
public class CombatManager {
    private long cooldownMs, lastDamageTime;

    public CombatManager(long cooldownMs) {
        this.cooldownMs = cooldownMs;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public boolean shouldDamage() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastDamageTime >= cooldownMs) {
            lastDamageTime = currentTime;
            return true;
        }

        return false;
    }

    public Projectile shootMagic(Shooter owner, double dirX, double dirY) { 
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) len = 1;
        double nx = dirX / len;
        double ny = dirY / len;
        double speed = 400.0;
        return new Fireball(owner, owner.getX(), owner.getY(), nx * speed, ny * speed);
    }

    public Projectile shootWeapon(Shooter owner, double dirX, double dirY) { 
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        if (len == 0) len = 1;
        double nx = dirX / len;
        double ny = dirY / len;
        double speed = 400.0;
        return new Fireball(owner, owner.getX(), owner.getY(), nx * speed, ny * speed);
    }

    // == Getters --
    public long getCooldownMs() { return cooldownMs; }
    public long getLastDamageTime() { return lastDamageTime; }

    // -- Setters --
    public void setCooldownMs(long cooldownMs) { this.cooldownMs = cooldownMs; }
    public void setLastDamageTime(long lastDamageTime) { this.lastDamageTime = lastDamageTime; }
}
