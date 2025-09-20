package game.entity.player;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import config.combat.Shooter;
import config.combat.Team;
import game.entity.base.Entity;
import game.entity.weapons.Weapon;
import game.entity.weapons.BasicGun;
import game.upgrade.UpgradeSystem;
import game.upgrade.UpgradeCardManager;

/**
 * 
 * @author bnk
 */
public class Player extends Entity implements Shooter {
	private boolean moveUp;
	private boolean moveDown;
	private boolean moveLeft;
	private boolean moveRight;
	private Team team = Team.PLAYER;
	private double lookDirX = 1.0;
	private double lookDirY = 0.0;
	private List<Weapon> weapons;
	private int level = 1;
	private int experience = 0;
	private int experienceToNextLevel = 100;
	private UpgradeSystem upgradeSystem;
	private UpgradeCardManager cardManager;
        
        // Init coords
        private float initX;
        private float initY;
	
	public Player(int x, int y, int speed, int width, int height, int health, boolean isVisible) {
		super(x, y, speed, width, height, health, isVisible, 22, 22);
                this.initX = x;
                this.initY = y;
		this.weapons = new ArrayList<>();
		this.upgradeSystem = new UpgradeSystem();
		this.cardManager = new UpgradeCardManager();
		// Começa com uma arma básica
		this.weapons.add(new BasicGun(x, y, Team.PLAYER, this));
	}

	public void paint(Graphics2D g2d) {
		super.paint(g2d);
		
		// Desenha as armas
		for (Weapon weapon : weapons) {
			weapon.render(g2d);
		}
	}
        
        public void reset() {
            this.setX(this.initX);
            this.setY(this.initY);
            this.setHealth(getInitHealth());
            this.setExperience(0);
            this.setLevel(1);
            this.getWeapons().clear();
            // Adicione a arma inicial se for o caso
            this.weapons.add(new BasicGun(this.initX, this.initY, Team.PLAYER, this));
        }


	public void setLookDir(double dx, double dy) {
		if (dx == 0 && dy == 0) return;
		double len = Math.sqrt(dx * dx + dy * dy);
		this.lookDirX = dx / len;
		this.lookDirY = dy / len;
	}
	
	public void updateWeaponPositions() {
		for (Weapon weapon : weapons) {
			weapon.updatePosition(this.getX(), this.getY(), lookDirX, lookDirY);
		}
	}
	
	public void addExperience(int xp) {
		this.experience += xp;
		if (this.experience >= experienceToNextLevel) {
			levelUp();
		}
	}
	
	private void levelUp() {
		this.level++;
		this.experience -= experienceToNextLevel;
		this.experienceToNextLevel = (int) (experienceToNextLevel * 1.2); // Aumenta exponencialmente
		
		// Ativa seleção de cartas em vez de upgrade automático
		cardManager.activateCardSelection();
		
		System.out.println("Level Up! Novo nível: " + level);
		System.out.println("Selecione uma carta de power-up!");
	}
	

	public boolean isAlive() {
		return this.getHealth() > 0;
	}
	
    // -- Getters --
	public boolean getMoveUp() { return this.moveUp; }
	public boolean getMoveDown() { return this.moveDown; }
	public boolean getMoveLeft() { return this.moveLeft; }
	public boolean getMoveRight() { return this.moveRight; }
	public Team getTeam() { return this.team; }
	public double getLookDirX() { return this.lookDirX; }
	public double getLookDirY() { return this.lookDirY; }
	public List<Weapon> getWeapons() { return this.weapons; }
	public int getLevel() { return this.level; }
	public int getExperience() { return this.experience; }
	public int getExperienceToNextLevel() { return this.experienceToNextLevel; }
	public UpgradeCardManager getCardManager() { return this.cardManager; }

	// -- Setters --
	public void setMoveUp(boolean moveUp) { this.moveUp = moveUp; }
	public void setMoveDown(boolean moveDown) { this.moveDown = moveDown; }
	public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
	public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setLevel(int level) { this.level = level; }
        
    /**
     * Aplica um upgrade selecionado pelo jogador
     */
    public void applySelectedUpgrade(String upgradeName) {
        upgradeSystem.applyUpgrade(upgradeName, this);
        System.out.println("Upgrade aplicado: " + upgradeName);
    }
}