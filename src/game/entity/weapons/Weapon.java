package game.entity.weapons;

import java.awt.Graphics2D;

import config.combat.Shooter;
import config.combat.Team;

/**
 * Classe base abstrata para todas as armas do jogo
 * @author bnk
 */
public abstract class Weapon implements Shooter {
    protected double x, y;
    protected Team team;
    protected Shooter owner;
    protected String name;
    protected float attackSpeed; // ataques por segundo
    protected int damage;
    protected float range;
    protected long lastAttackTime;
    protected int level = 1;
    
    public Weapon(double x, double y, Team team, Shooter owner, String name, 
                  float attackSpeed, int damage, float range) {
        this.x = x;
        this.y = y;
        this.team = team;
        this.owner = owner;
        this.name = name;
        this.attackSpeed = attackSpeed;
        this.damage = damage;
        this.range = range;
        this.lastAttackTime = System.currentTimeMillis();
    }
    
    public void updatePosition(double ownerX, double ownerY, double lookDirX, double lookDirY) {
        // Posiciona a arma na frente do player baseado na direção que está olhando
        double offset = 30.0; // distância da arma em relação ao player
        this.x = ownerX + (lookDirX * offset);
        this.y = ownerY + (lookDirY * offset);
    }
    
    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        long cooldown = (long) (1000.0 / attackSpeed); // Converte ataques por segundo para cooldown em ms
        return (currentTime - lastAttackTime) >= cooldown;
    }
    
    public void attack() {
        this.lastAttackTime = System.currentTimeMillis();
    }
    
    public void levelUp() {
        this.level++;
        this.attackSpeed += 0.2f; // Aumenta velocidade de ataque
        this.damage += 5; // Aumenta dano
        System.out.println("Arma " + name + " evoluiu para nível " + level);
    }
    
    // Método abstrato para desenhar a arma
    public abstract void render(Graphics2D g2d);
    
    // Método abstrato para criar projétil específico da arma
    public abstract config.combat.Projectile createProjectile(double dirX, double dirY);
    
    // -- Getters --
    public Shooter getOwner() { return owner; }
    public String getName() { return name; }
    public float getAttackSpeed() { return attackSpeed; }
    public int getDamage() { return damage; }
    public float getRange() { return range; }
    public int getLevel() { return level; }
    
    @Override
    public double getX() { return x; }
    
    @Override
    public double getY() { return y; }
    
    @Override
    public Team getTeam() { return team; }
    

    // -- Setters --
    public void setOwner(Shooter owner) { this.owner = owner; }
}