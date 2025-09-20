package game.upgrade;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

/**
 * Representa uma carta de power-up que pode ser selecionada pelo jogador
 * @author bnk
 */
public class UpgradeCard {
    private String upgradeName;
    private String description;
    private Color cardColor;
    private Color borderColor;
    private boolean isSelected;
    private boolean isHovered;
    private int x, y;
    private int width = 200;
    private int height = 280;
    
    public UpgradeCard(String upgradeName, String description, Color cardColor) {
        this.upgradeName = upgradeName;
        this.description = description;
        this.cardColor = cardColor;
        this.borderColor = Color.WHITE;
        this.isSelected = false;
        this.isHovered = false;
    }
    
    /**
     * Renderiza a carta usando primitivas gráficas 2D
     */
    public void render(Graphics2D g2d, int x, int y) {
        this.x = x;
        this.y = y;
        
        AffineTransform oldTransform = g2d.getTransform();
        
        try {
            // Efeito de escala baseado no hover/seleção
            double scale = 1.0;
            if (isHovered) scale = 1.05;
            if (isSelected) scale = 1.1;
            
            // Aplica transformação de escala
            AffineTransform transform = new AffineTransform();
            transform.translate(x + width/2.0, y + height/2.0);
            transform.scale(scale, scale);
            transform.translate(-width/2.0, -height/2.0);
            g2d.setTransform(transform);
            
            // Desenha fundo da carta com bordas arredondadas
            RoundRectangle2D cardRect = new RoundRectangle2D.Double(0, 0, width, height, 15, 15);
            
            // Cor da carta baseada no estado
            Color currentCardColor = cardColor;
            if (isSelected) {
                currentCardColor = new Color(
                    Math.min(255, cardColor.getRed() + 50),
                    Math.min(255, cardColor.getGreen() + 50),
                    Math.min(255, cardColor.getBlue() + 50)
                );
            }
            
            g2d.setColor(currentCardColor);
            g2d.fill(cardRect);
            
            // Desenha borda da carta
            g2d.setColor(borderColor);
            g2d.setStroke(new java.awt.BasicStroke(3));
            g2d.draw(cardRect);
            
            // Desenha ícone da carta usando formas geométricas
            drawCardIcon(g2d);
            
            // Desenha texto da carta
            drawCardText(g2d);
            
        } finally {
            g2d.setTransform(oldTransform);
        }
    }
    
    /**
     * Desenha ícone da carta usando primitivas gráficas
     */
    private void drawCardIcon(Graphics2D g2d) {
        int iconSize = 60;
        int iconX = (width - iconSize) / 2;
        int iconY = 30;
        
        g2d.setColor(Color.WHITE);
        
        // Desenha ícone baseado no tipo de upgrade
        switch (upgradeName) {
            case "Speed Boost":
                drawSpeedIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Health Boost":
                drawHealthIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Weapon Damage":
                drawDamageIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Weapon Speed":
                drawSpeedIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Basic Gun":
                drawGunIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Magic Wand":
                drawWandIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Energy Sword":
                drawSwordIcon(g2d, iconX, iconY, iconSize);
                break;
            case "Grenade Launcher":
                drawLauncherIcon(g2d, iconX, iconY, iconSize);
                break;
            default:
                drawDefaultIcon(g2d, iconX, iconY, iconSize);
        }
    }
    
    /**
     * Desenha ícone de velocidade usando linhas e curvas
     */
    private void drawSpeedIcon(Graphics2D g2d, int x, int y, int size) {
        // Desenha setas de velocidade usando linhas
        g2d.setStroke(new java.awt.BasicStroke(3));
        for (int i = 0; i < 3; i++) {
            int lineY = y + 20 + (i * 10);
            g2d.drawLine(x + 10, lineY, x + size - 10, lineY);
            // Ponta da seta
            g2d.drawLine(x + size - 15, lineY - 3, x + size - 10, lineY);
            g2d.drawLine(x + size - 15, lineY + 3, x + size - 10, lineY);
        }
    }
    
    /**
     * Desenha ícone de vida usando formas geométricas
     */
    private void drawHealthIcon(Graphics2D g2d, int x, int y, int size) {
        // Desenha coração usando curvas
        int centerX = x + size/2;
        int centerY = y + size/2;
        
        // Corpo do coração usando elipse
        g2d.fillOval(centerX - 15, centerY - 10, 15, 15);
        g2d.fillOval(centerX, centerY - 10, 15, 15);
        
        // Parte inferior do coração usando triângulo
        int[] xPoints = {centerX - 15, centerX, centerX + 15};
        int[] yPoints = {centerY + 5, centerY + 20, centerY + 5};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
    
    /**
     * Desenha ícone de dano usando linhas
     */
    private void drawDamageIcon(Graphics2D g2d, int x, int y, int size) {
        g2d.setStroke(new java.awt.BasicStroke(4));
        // Desenha explosão usando linhas radiais
        int centerX = x + size/2;
        int centerY = y + size/2;
        
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8;
            int endX = centerX + (int)(Math.cos(angle) * 20);
            int endY = centerY + (int)(Math.sin(angle) * 20);
            g2d.drawLine(centerX, centerY, endX, endY);
        }
    }
    
    /**
     * Desenha ícone de arma usando GeneralPath
     */
    private void drawGunIcon(Graphics2D g2d, int x, int y, int size) {
        java.awt.geom.GeneralPath gun = new java.awt.geom.GeneralPath();
        
        // Cano da arma
        gun.moveTo(x + 10, y + size/2);
        gun.lineTo(x + size - 10, y + size/2);
        gun.lineTo(x + size - 5, y + size/2 - 5);
        gun.lineTo(x + size - 5, y + size/2 + 5);
        gun.closePath();
        
        // Gatilho
        gun.moveTo(x + 15, y + size/2 + 5);
        gun.lineTo(x + 20, y + size/2 + 10);
        gun.lineTo(x + 15, y + size/2 + 15);
        
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.draw(gun);
    }
    
    /**
     * Desenha ícone de varinha mágica usando curvas
     */
    private void drawWandIcon(Graphics2D g2d, int x, int y, int size) {
        // Varinha
        g2d.setStroke(new java.awt.BasicStroke(3));
        g2d.drawLine(x + size/2, y + 10, x + size/2, y + size - 10);
        
        // Estrela no topo usando curvas
        int centerX = x + size/2;
        int centerY = y + 10;
        
        // Desenha estrela usando arcos
        for (int i = 0; i < 5; i++) {
            double angle = (i * Math.PI * 2) / 5;
            int starX = centerX + (int)(Math.cos(angle) * 8);
            int starY = centerY + (int)(Math.sin(angle) * 8);
            g2d.drawLine(centerX, centerY, starX, starY);
        }
    }
    
    /**
     * Desenha ícone de espada usando GeneralPath
     */
    private void drawSwordIcon(Graphics2D g2d, int x, int y, int size) {
        java.awt.geom.GeneralPath sword = new java.awt.geom.GeneralPath();
        
        // Lâmina
        sword.moveTo(x + size/2, y + 10);
        sword.lineTo(x + size/2 + 2, y + size - 20);
        sword.lineTo(x + size/2 - 2, y + size - 20);
        sword.closePath();
        
        // Guarda
        sword.moveTo(x + size/2 - 8, y + size - 20);
        sword.lineTo(x + size/2 + 8, y + size - 20);
        
        // Punho
        sword.moveTo(x + size/2 - 3, y + size - 20);
        sword.lineTo(x + size/2 - 3, y + size - 10);
        sword.lineTo(x + size/2 + 3, y + size - 10);
        sword.lineTo(x + size/2 + 3, y + size - 20);
        
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.draw(sword);
    }
    
    /**
     * Desenha ícone de lançador de granadas
     */
    private void drawLauncherIcon(Graphics2D g2d, int x, int y, int size) {
        // Tubo do lançador
        g2d.setStroke(new java.awt.BasicStroke(4));
        g2d.drawLine(x + 10, y + size/2, x + size - 10, y + size/2);
        
        // Granada
        g2d.fillOval(x + size - 15, y + size/2 - 5, 10, 10);
        
        // Alça
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawArc(x + 15, y + size/2 - 8, 20, 16, 0, 180);
    }
    
    /**
     * Desenha ícone padrão
     */
    private void drawDefaultIcon(Graphics2D g2d, int x, int y, int size) {
        // Desenha estrela simples
        int centerX = x + size/2;
        int centerY = y + size/2;
        
        g2d.setStroke(new java.awt.BasicStroke(3));
        for (int i = 0; i < 5; i++) {
            double angle = (i * Math.PI * 2) / 5;
            int outerX = centerX + (int)(Math.cos(angle) * 15);
            int outerY = centerY + (int)(Math.sin(angle) * 15);
            g2d.drawLine(centerX, centerY, outerX, outerY);
        }
    }
    
    /**
     * Desenha texto da carta
     */
    private void drawCardText(Graphics2D g2d) {
        // Configura fonte
        Font titleFont = new Font("Courier New", Font.BOLD, 16);
        Font descFont = new Font("Courier New", Font.PLAIN, 12);
        
        // Título
        g2d.setFont(titleFont);
        g2d.setColor(Color.WHITE);
        
        // Centraliza o título
        int titleWidth = g2d.getFontMetrics().stringWidth(upgradeName);
        int titleX = (width - titleWidth) / 2;
        g2d.drawString(upgradeName, titleX, 120);
        
        // Descrição
        g2d.setFont(descFont);
        g2d.setColor(new Color(240, 240, 240));
        
        // Quebra a descrição em linhas
        String[] words = description.split(" ");
        StringBuilder currentLine = new StringBuilder();
        int lineY = 150;
        int maxWidth = width - 20;
        
        for (String word : words) {
            String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            int testWidth = g2d.getFontMetrics().stringWidth(testLine);
            
            if (testWidth > maxWidth && currentLine.length() > 0) {
                // Desenha linha atual
                int lineWidth = g2d.getFontMetrics().stringWidth(currentLine.toString());
                int lineX = (width - lineWidth) / 2;
                g2d.drawString(currentLine.toString(), lineX, lineY);
                
                // Nova linha
                currentLine = new StringBuilder(word);
                lineY += 15;
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }
        
        // Desenha última linha
        if (currentLine.length() > 0) {
            int lineWidth = g2d.getFontMetrics().stringWidth(currentLine.toString());
            int lineX = (width - lineWidth) / 2;
            g2d.drawString(currentLine.toString(), lineX, lineY);
        }
    }
    
    /**
     * Verifica se o ponto está dentro da carta
     */
    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && 
               mouseY >= y && mouseY <= y + height;
    }
    
    // Getters e Setters
    public String getUpgradeName() { return upgradeName; }
    public String getDescription() { return description; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
    public boolean isHovered() { return isHovered; }
    public void setHovered(boolean hovered) { this.isHovered = hovered; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
