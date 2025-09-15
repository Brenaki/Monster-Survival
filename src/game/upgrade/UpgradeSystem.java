package game.upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.entity.weapons.Weapon;
import game.entity.weapons.BasicGun;
import game.entity.weapons.MagicWand;
import game.entity.weapons.EnergySword;
import game.entity.weapons.GrenadeLauncher;

/**
 * Sistema de upgrades para o jogo, similar ao Vampire Survivors
 * @author bnk
 */
public class UpgradeSystem {
    private List<String> availableUpgrades;
    private Random random;
    
    public UpgradeSystem() {
        this.availableUpgrades = new ArrayList<>();
        this.random = new Random();
        initializeUpgrades();
    }
    
    private void initializeUpgrades() {
        availableUpgrades.add("Speed Boost");
        availableUpgrades.add("Health Boost");
        availableUpgrades.add("Weapon Damage");
        availableUpgrades.add("Weapon Speed");
        availableUpgrades.add("Basic Gun");
        availableUpgrades.add("Magic Wand");
        availableUpgrades.add("Energy Sword");
        availableUpgrades.add("Grenade Launcher");
    }
    
    public List<String> getRandomUpgrades(int count) {
        List<String> selectedUpgrades = new ArrayList<>();
        List<String> tempList = new ArrayList<>(availableUpgrades);
        
        for (int i = 0; i < count && !tempList.isEmpty(); i++) {
            int randomIndex = random.nextInt(tempList.size());
            selectedUpgrades.add(tempList.remove(randomIndex));
        }
        
        return selectedUpgrades;
    }
    
    public void applyUpgrade(String upgradeName, Object target) {
        switch (upgradeName) {
            case "Speed Boost":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    player.setSpeed(player.getSpeed() + 0.5);
                }
                break;
            case "Health Boost":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    player.setHealth(player.getHealth() + 20);
                }
                break;
            case "Weapon Damage":
                if (target instanceof Weapon) {
                    Weapon weapon = (Weapon) target;
                    weapon.levelUp();
                }
                break;
            case "Weapon Speed":
                if (target instanceof Weapon) {
                    Weapon weapon = (Weapon) target;
                    weapon.levelUp();
                }
                break;
            case "Basic Gun":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    BasicGun newWeapon = new BasicGun(player.getX(), player.getY(), 
                        player.getTeam(), player);
                    player.getWeapons().add(newWeapon);
                }
                break;
            case "Magic Wand":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    MagicWand newWeapon = new MagicWand(player.getX(), player.getY(), 
                        player.getTeam(), player);
                    player.getWeapons().add(newWeapon);
                }
                break;
            case "Energy Sword":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    EnergySword newWeapon = new EnergySword(player.getX(), player.getY(), 
                        player.getTeam(), player);
                    player.getWeapons().add(newWeapon);
                }
                break;
            case "Grenade Launcher":
                if (target instanceof game.entity.player.Player) {
                    game.entity.player.Player player = (game.entity.player.Player) target;
                    GrenadeLauncher newWeapon = new GrenadeLauncher(player.getX(), player.getY(), 
                        player.getTeam(), player);
                    player.getWeapons().add(newWeapon);
                }
                break;
        }
    }
}
