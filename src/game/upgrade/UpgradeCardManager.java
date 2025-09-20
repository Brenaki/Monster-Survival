package game.upgrade;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gerencia a seleção de cartas de power-up durante o level up
 * @author bnk
 */
public class UpgradeCardManager {
    private List<UpgradeCard> cards;
    private boolean isActive;
    private int selectedCardIndex;
    private Random random;
    
    // Cores para diferentes tipos de upgrades
    private static final Color SPEED_COLOR = new Color(100, 200, 255);
    private static final Color HEALTH_COLOR = new Color(255, 100, 100);
    private static final Color DAMAGE_COLOR = new Color(255, 200, 100);
    private static final Color WEAPON_COLOR = new Color(200, 100, 255);
    
    public UpgradeCardManager() {
        this.cards = new ArrayList<>();
        this.isActive = false;
        this.selectedCardIndex = 0;
        this.random = new Random();
    }
    
    /**
     * Ativa a seleção de cartas com 3 opções aleatórias
     */
    public void activateCardSelection() {
        this.isActive = true;
        this.selectedCardIndex = 0;
        this.cards.clear();
        
        // Gera 3 cartas únicas
        List<String> availableUpgrades = new ArrayList<>();
        availableUpgrades.add("Speed Boost");
        availableUpgrades.add("Health Boost");
        availableUpgrades.add("Weapon Damage");
        availableUpgrades.add("Weapon Speed");
        availableUpgrades.add("Basic Gun");
        availableUpgrades.add("Magic Wand");
        availableUpgrades.add("Energy Sword");
        availableUpgrades.add("Grenade Launcher");
        
        // Seleciona 3 upgrades únicos
        List<String> selectedUpgrades = new ArrayList<>();
        while (selectedUpgrades.size() < 3 && !availableUpgrades.isEmpty()) {
            int randomIndex = random.nextInt(availableUpgrades.size());
            String upgrade = availableUpgrades.remove(randomIndex);
            selectedUpgrades.add(upgrade);
        }
        
        // Cria as cartas
        for (String upgrade : selectedUpgrades) {
            UpgradeCard card = createCard(upgrade);
            cards.add(card);
        }
        
        System.out.println("Seleção de cartas ativada! Escolha uma das " + cards.size() + " opções.");
    }
    
    /**
     * Cria uma carta baseada no tipo de upgrade
     */
    private UpgradeCard createCard(String upgradeName) {
        String description;
        Color cardColor;
        
        switch (upgradeName) {
            case "Speed Boost":
                description = "Aumenta a velocidade de movimento do jogador";
                cardColor = SPEED_COLOR;
                break;
            case "Health Boost":
                description = "Aumenta a vida máxima do jogador";
                cardColor = HEALTH_COLOR;
                break;
            case "Weapon Damage":
                description = "Aumenta o dano de todas as armas";
                cardColor = DAMAGE_COLOR;
                break;
            case "Weapon Speed":
                description = "Aumenta a velocidade de ataque das armas";
                cardColor = DAMAGE_COLOR;
                break;
            case "Basic Gun":
                description = "Adiciona uma pistola básica ao arsenal";
                cardColor = WEAPON_COLOR;
                break;
            case "Magic Wand":
                description = "Adiciona uma varinha mágica que lança bolas de fogo";
                cardColor = WEAPON_COLOR;
                break;
            case "Energy Sword":
                description = "Adiciona uma espada de energia para combate corpo a corpo";
                cardColor = WEAPON_COLOR;
                break;
            case "Grenade Launcher":
                description = "Adiciona um lançador de granadas explosivas";
                cardColor = WEAPON_COLOR;
                break;
            default:
                description = "Upgrade misterioso";
                cardColor = Color.GRAY;
        }
        
        return new UpgradeCard(upgradeName, description, cardColor);
    }
    
    /**
     * Renderiza todas as cartas na tela
     */
    public void render(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (!isActive || cards.isEmpty()) return;
        
        // Desenha overlay escuro
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, screenWidth, screenHeight);
        
        // Calcula posições das cartas
        int cardSpacing = 50;
        int totalWidth = (cards.size() * 200) + ((cards.size() - 1) * cardSpacing);
        int startX = (screenWidth - totalWidth) / 2;
        int cardY = (screenHeight - 280) / 2;
        
        // Renderiza cada carta
        for (int i = 0; i < cards.size(); i++) {
            UpgradeCard card = cards.get(i);
            int cardX = startX + (i * (200 + cardSpacing));
            
            // Marca como selecionada se for a carta atual
            card.setSelected(i == selectedCardIndex);
            
            card.render(g2d, cardX, cardY);
        }
        
        // Desenha instruções
        drawInstructions(g2d, screenWidth, screenHeight);
    }
    
    /**
     * Desenha instruções de controle
     */
    private void drawInstructions(Graphics2D g2d, int screenWidth, int screenHeight) {
        // Configura fonte
        java.awt.Font font = new java.awt.Font("Courier New", java.awt.Font.BOLD, 18);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        
        // Texto de instrução
        String instruction = "Use as setas ou WASD para selecionar, ENTER para confirmar";
        int textWidth = g2d.getFontMetrics().stringWidth(instruction);
        int textX = (screenWidth - textWidth) / 2;
        int textY = screenHeight - 50;
        
        g2d.drawString(instruction, textX, textY);
    }
    
    /**
     * Move a seleção para a esquerda
     */
    public void selectPrevious() {
        if (!isActive) return;
        
        selectedCardIndex--;
        if (selectedCardIndex < 0) {
            selectedCardIndex = cards.size() - 1;
        }
    }
    
    /**
     * Move a seleção para a direita
     */
    public void selectNext() {
        if (!isActive) return;
        
        selectedCardIndex++;
        if (selectedCardIndex >= cards.size()) {
            selectedCardIndex = 0;
        }
    }
    
    /**
     * Confirma a seleção da carta atual
     */
    public String confirmSelection() {
        if (!isActive || cards.isEmpty()) return null;
        
        UpgradeCard selectedCard = cards.get(selectedCardIndex);
        String upgradeName = selectedCard.getUpgradeName();
        
        // Desativa a seleção
        isActive = false;
        cards.clear();
        
        System.out.println("Carta selecionada: " + upgradeName);
        return upgradeName;
    }
    
    /**
     * Verifica se a seleção de cartas está ativa
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Força o fechamento da seleção (para casos de emergência)
     */
    public void closeSelection() {
        isActive = false;
        cards.clear();
    }
    
    /**
     * Retorna o número de cartas disponíveis
     */
    public int getCardCount() {
        return cards.size();
    }
    
    /**
     * Retorna o índice da carta selecionada
     */
    public int getSelectedCardIndex() {
        return selectedCardIndex;
    }
}
