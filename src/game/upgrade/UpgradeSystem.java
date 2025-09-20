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
    
    // Contadores para upgrades de estatísticas
    private int speedBoostLevel = 0;
    private int healthBoostLevel = 0;
    private int weaponDamageLevel = 0;
    private int weaponSpeedLevel = 0;
    
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
        if (!(target instanceof game.entity.player.Player)) {
            return;
        }
        
        game.entity.player.Player player = (game.entity.player.Player) target;
        
        switch (upgradeName) {
            case "Speed Boost":
                speedBoostLevel++;
                player.setSpeed(player.getSpeed() + 0.5);
                System.out.println("Speed Boost aplicado! Nível: " + speedBoostLevel + 
                    " | Velocidade atual: " + player.getSpeed());
                break;
                
            case "Health Boost":
                healthBoostLevel++;
                int maxHealthIncrease = 20 + (healthBoostLevel * 5); // Aumenta com o nível
                player.setHealth(player.getHealth() + maxHealthIncrease);
                System.out.println("Health Boost aplicado! Nível: " + healthBoostLevel + 
                    " | Vida atual: " + player.getHealth());
                break;
                
            case "Weapon Damage":
                weaponDamageLevel++;
                // Aplica dano extra a todas as armas existentes
                for (Weapon weapon : player.getWeapons()) {
                    weapon.setDamage(weapon.getDamage() + 5);
                }
                System.out.println("Weapon Damage aplicado! Nível: " + weaponDamageLevel + 
                    " | Dano extra: +" + (weaponDamageLevel * 5));
                break;
                
            case "Weapon Speed":
                weaponSpeedLevel++;
                // Aplica velocidade extra a todas as armas existentes
                for (Weapon weapon : player.getWeapons()) {
                    weapon.setAttackSpeed(weapon.getAttackSpeed() + 0.3f);
                }
                System.out.println("Weapon Speed aplicado! Nível: " + weaponSpeedLevel + 
                    " | Velocidade extra: +" + (weaponSpeedLevel * 0.3f));
                break;
                
            case "Basic Gun":
                addOrLevelWeapon(player, "Pistola Básica", () -> 
                    new BasicGun(player.getX(), player.getY(), player.getTeam(), player));
                break;
                
            case "Magic Wand":
                addOrLevelWeapon(player, "Varinha Mágica", () -> 
                    new MagicWand(player.getX(), player.getY(), player.getTeam(), player));
                break;
                
            case "Energy Sword":
                addOrLevelWeapon(player, "Espada de Energia", () -> 
                    new EnergySword(player.getX(), player.getY(), player.getTeam(), player));
                break;
                
            case "Grenade Launcher":
                addOrLevelWeapon(player, "Lançador de Granadas", () -> 
                    new GrenadeLauncher(player.getX(), player.getY(), player.getTeam(), player));
                break;
        }
    }
    
    /**
     * Adiciona uma nova arma ou aumenta o nível de uma arma existente
     */
    private void addOrLevelWeapon(game.entity.player.Player player, String weaponName, WeaponFactory factory) {
        // Procura por uma arma existente do mesmo tipo
        Weapon existingWeapon = null;
        for (Weapon weapon : player.getWeapons()) {
            if (weapon.getName().equals(weaponName)) {
                existingWeapon = weapon;
                break;
            }
        }
        
        if (existingWeapon != null) {
            // Arma já existe, aumenta o nível
            existingWeapon.levelUp();
            System.out.println(weaponName + " evoluiu para nível " + existingWeapon.getLevel());
        } else {
            // Arma não existe, adiciona nova
            Weapon newWeapon = factory.create();
            // Posiciona a arma corretamente
            newWeapon.updatePosition(player.getX(), player.getY(), player.getLookDirX(), player.getLookDirY());
            player.getWeapons().add(newWeapon);
            System.out.println(weaponName + " adicionada ao arsenal!");
        }
    }
    
    // Interface funcional para criar armas
    @FunctionalInterface
    private interface WeaponFactory {
        Weapon create();
    }
    
    // Getters para os níveis dos upgrades
    public int getSpeedBoostLevel() { return speedBoostLevel; }
    public int getHealthBoostLevel() { return healthBoostLevel; }
    public int getWeaponDamageLevel() { return weaponDamageLevel; }
    public int getWeaponSpeedLevel() { return weaponSpeedLevel; }
}
