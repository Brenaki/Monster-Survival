package game.entity.weapons;

import config.combat.Shooter;
import config.combat.Team;

/**
 * 
 * @author bnk
 */
public class Weapon implements Shooter {
    private double x, y;
    private Team team;
    private Shooter owner;
    
    public Weapon(double x, double y, Team team, Shooter owner) {
        this.x = x;
        this.y = y;
        this.team = team;
        this.owner = owner;
    }
    
    public void updatePosition(double ownerX, double ownerY, double lookDirX, double lookDirY) {
        // Posiciona a arma na frente do player baseado na direção que está olhando
        double offset = 30.0; // distância da arma em relação ao player
        this.x = ownerX + (lookDirX * offset);
        this.y = ownerY + (lookDirY * offset);
    }
    
    // -- Getters --
    public Shooter getOwner() { return owner; }
    
    @Override
    public double getX() { return x; }
    
    @Override
    public double getY() { return y; }
    
    @Override
    public Team getTeam() { return team; }
    

    // -- Setters --
    public void setOwner(Shooter owner) { this.owner = owner; }
}