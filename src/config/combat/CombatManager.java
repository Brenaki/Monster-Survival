package config.combat;

/**
 * 
 * @author bnk
 */
public class CombatManager {
    private int damage;
    private float range;
    private long cooldown;
    private long lastDamageTime;

    public CombatManager(int damage, float range, long cooldown) {
        this.damage = damage;
        this.range = range;
        this.cooldown = cooldown;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public boolean shouldDamage() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastDamageTime >= cooldown) {
            lastDamageTime = currentTime;
            return true;
        }

        return false;
    }

    // == Getters --
    public int getDamage() { return damage; }
    public float getRange() { return range; }
    public long getCooldown() { return cooldown; }
    public long getLastDamageTime() { return lastDamageTime; }

    // -- Setters --
    public void setDamage(int damage) { this.damage = damage; }
    public void setRange(float range) { this.range = range; }
    public void setCooldown(long cooldown) { this.cooldown = cooldown; }
    public void setLastDamageTime(long lastDamageTime) { this.lastDamageTime = lastDamageTime; }
}
