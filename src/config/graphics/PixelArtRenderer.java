package config.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;
import java.awt.RenderingHints;

/**
 * Classe responsável por renderizar elementos gráficos no estilo 8-bit
 * @author bnk
 */
public class PixelArtRenderer {
    
    // Paleta de cores 8-bit limitada
    public static final Color BACKGROUND = new Color(20, 20, 40);     // Azul escuro
    public static final Color PLAYER_COLOR = new Color(255, 100, 100); // Vermelho claro
    public static final Color ENEMY_NORMAL = new Color(100, 255, 100); // Verde
    public static final Color ENEMY_FAST = new Color(100, 200, 255);   // Azul claro
    public static final Color ENEMY_TANK = new Color(255, 100, 100);  // Vermelho
    public static final Color ENEMY_BOSS = new Color(255, 100, 255);   // Magenta
    public static final Color PROJECTILE_COLOR = new Color(255, 255, 100); // Amarelo
    public static final Color XP_GEM_LOW = new Color(100, 255, 100);   // Verde
    public static final Color XP_GEM_MED = new Color(100, 200, 255);  // Azul claro
    public static final Color XP_GEM_HIGH = new Color(255, 100, 255); // Magenta
    public static final Color UI_TEXT = new Color(255, 255, 255);     // Branco
    public static final Color UI_ACCENT = new Color(255, 200, 100);   // Laranja
    
    /**
     * Configura o Graphics2D para renderização pixel-perfect
     */
    public static void setupPixelPerfect(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }
    
    /**
     * Desenha o player usando sprite ou fallback para pixel art
     */
    public static void drawPlayer(Graphics2D g2d, double x, double y, double rotation) {
        SpriteManager spriteManager = SpriteManager.getInstance();
        
        if (spriteManager.hasSprite("player")) {
            // Usa sprite se disponível
            spriteManager.drawPlayer(g2d, x, y, rotation);
        } else {
            // Fallback para pixel art usando GeneralPath
            drawPlayerFallback(g2d, x, y, rotation);
        }
    }
    
    /**
     * Fallback pixel art do player usando GeneralPath
     */
    private static void drawPlayerFallback(Graphics2D g2d, double x, double y, double rotation) {
        AffineTransform oldTransform = g2d.getTransform();
        
        // Aplica inversão horizontal baseada na direção X
        AffineTransform transform = new AffineTransform();
        transform.translate(x + 32, y + 32); // Centro do sprite (8x maior)
        
        // Determina se deve inverter horizontalmente baseado na direção X
        double scaleX = 8.0;
        if (Math.abs(Math.cos(rotation)) > Math.abs(Math.sin(rotation))) {
            // Movimento principal no eixo X - inverte horizontalmente
            scaleX = -8.0;
        }
        
        transform.scale(scaleX, 8.0); // Escala 8x para consistência
        transform.translate(-4, -4); // Volta para a posição original
        g2d.setTransform(transform);
        
        // Cria o corpo do player usando GeneralPath
        GeneralPath playerShape = new GeneralPath();
        playerShape.moveTo(1, 0);   // Topo
        playerShape.lineTo(3, 0);  // Topo direito
        playerShape.lineTo(4, 1); // Direita
        playerShape.lineTo(3, 2); // Baixo direito
        playerShape.lineTo(1, 2); // Baixo esquerdo
        playerShape.lineTo(0, 1);  // Esquerda
        playerShape.closePath();
        
        g2d.setColor(PLAYER_COLOR);
        g2d.fill(playerShape);
        
        // Desenha detalhes com linhas
        g2d.setColor(Color.WHITE);
        g2d.drawLine(1, 1, 3, 1); // Linha horizontal no meio
        g2d.drawLine(2, 0, 2, 1);  // Linha vertical no meio
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Desenha inimigo usando sprite ou fallback para pixel art
     */
    public static void drawEnemy(Graphics2D g2d, double x, double y, String enemyType, double scale) {
        SpriteManager spriteManager = SpriteManager.getInstance();
        
        if (spriteManager.hasSprite(getEnemySpriteKey(enemyType))) {
            // Usa sprite se disponível
            spriteManager.drawEnemy(g2d, x, y, enemyType, scale);
        } else {
            // Fallback para pixel art usando formas geométricas
            drawEnemyFallback(g2d, x, y, enemyType, scale);
        }
    }
    
    /**
     * Fallback pixel art do inimigo usando formas geométricas
     */
    private static void drawEnemyFallback(Graphics2D g2d, double x, double y, String enemyType, double scale) {
        AffineTransform oldTransform = g2d.getTransform();
        
        // Escala base 8x para consistência com sprites, multiplicada pela escala de profundidade
        double baseScale = 8.0;
        double finalScale = baseScale * scale;
        
        AffineTransform transform = new AffineTransform();
        transform.translate(x + 32, y + 32); // Centro ajustado para escala 8x
        transform.scale(finalScale, finalScale);
        transform.translate(-4, -4);
        g2d.setTransform(transform);
        
        Color enemyColor = getEnemyColor(enemyType);
        g2d.setColor(enemyColor);
        
        // Desenha corpo baseado no tipo (formas menores para escala 8x)
        switch (enemyType) {
            case "Fast":
                // Forma de losango usando GeneralPath
                GeneralPath fastShape = new GeneralPath();
                fastShape.moveTo(2, 0);
                fastShape.lineTo(4, 2);
                fastShape.lineTo(2, 4);
                fastShape.lineTo(0, 2);
                fastShape.closePath();
                g2d.fill(fastShape);
                break;
                
            case "Tank":
                // Forma retangular com cantos arredondados
                g2d.fillRoundRect(0, 0, 4, 4, 1, 1);
                // Linhas decorativas
                g2d.setColor(Color.WHITE);
                g2d.drawLine(1, 1, 3, 1);
                g2d.drawLine(1, 2, 3, 2);
                g2d.drawLine(1, 3, 3, 3);
                break;
                
            case "Boss":
                // Forma circular com detalhes
                g2d.fillOval(0, 0, 4, 4);
                // Desenha olhos usando elipses
                g2d.setColor(Color.WHITE);
                g2d.fillOval(1, 1, 1, 1);
                g2d.fillOval(2, 1, 1, 1);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(1, 1, 1, 1);
                g2d.fillOval(2, 1, 1, 1);
                break;
                
            default: // Normal
                // Forma quadrada simples
                g2d.fillRect(0, 0, 4, 4);
                // Linhas decorativas
                g2d.setColor(Color.WHITE);
                g2d.drawLine(1, 1, 3, 1);
                g2d.drawLine(1, 2, 3, 2);
                g2d.drawLine(1, 3, 3, 3);
                break;
        }
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Retorna a chave do sprite baseada no tipo de inimigo
     */
    private static String getEnemySpriteKey(String enemyType) {
        switch (enemyType) {
            case "Fast": return "enemy_fast";
            case "Tank": return "enemy_tank";
            case "Boss": return "enemy_boss";
            default: return "enemy_default";
        }
    }
    
    /**
     * Retorna cor baseada no tipo de inimigo
     */
    private static Color getEnemyColor(String enemyType) {
        switch (enemyType) {
            case "Fast": return ENEMY_FAST;
            case "Tank": return ENEMY_TANK;
            case "Boss": return ENEMY_BOSS;
            default: return ENEMY_NORMAL;
        }
    }
    
    /**
     * Desenha projétil usando linhas e curvas
     */
    public static void drawProjectile(Graphics2D g2d, double x, double y, double rotation) {
        AffineTransform oldTransform = g2d.getTransform();
        
        // Aplica rotação do projétil
        AffineTransform transform = new AffineTransform();
        transform.translate(x + 10, y + 10); // Centro ajustado para escala maior
        transform.rotate(rotation);
        transform.scale(1.5, 1.5); // Escala 1.5x para projéteis
        transform.translate(-5, -5);
        g2d.setTransform(transform);
        
        g2d.setColor(PROJECTILE_COLOR);
        
        // Desenha projétil usando GeneralPath com linhas e curvas
        GeneralPath projectileShape = new GeneralPath();
        projectileShape.moveTo(0, 5);
        projectileShape.lineTo(8, 2);
        projectileShape.quadTo(10, 5, 8, 8);
        projectileShape.lineTo(0, 5);
        projectileShape.closePath();
        
        g2d.fill(projectileShape);
        
        // Adiciona rastro usando linhas
        g2d.setColor(Color.WHITE);
        g2d.drawLine(-3, 5, 0, 5);
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Desenha gema de experiência com efeito pulsante
     */
    public static void drawExperienceGem(Graphics2D g2d, double x, double y, int value, double pulseScale) {
        AffineTransform oldTransform = g2d.getTransform();
        
        // Aplica escala base maior (2x) multiplicada pelo pulso
        double baseScale = 2.0;
        double finalScale = baseScale * pulseScale;
        
        AffineTransform transform = new AffineTransform();
        transform.translate(x + 8, y + 8); // Centro ajustado para escala maior
        transform.scale(finalScale, finalScale);
        transform.translate(-4, -4);
        g2d.setTransform(transform);
        
        Color gemColor = getGemColor(value);
        g2d.setColor(gemColor);
        
        // Desenha gema usando elipse
        Ellipse2D.Double gemShape = new Ellipse2D.Double(0, 0, 8, 8);
        g2d.fill(gemShape);
        
        // Adiciona brilho usando arco
        g2d.setColor(Color.WHITE);
        Arc2D.Double shine = new Arc2D.Double(1, 1, 6, 6, 45, 90, Arc2D.PIE);
        g2d.fill(shine);
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Desenha efeito de explosão usando linhas radiais
     */
    public static void drawExplosion(Graphics2D g2d, double x, double y, double scale) {
        AffineTransform oldTransform = g2d.getTransform();
        
        // Aplica escala base maior (2x) multiplicada pela escala da explosão
        double baseScale = 2.0;
        double finalScale = baseScale * scale;
        
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(finalScale, finalScale);
        g2d.setTransform(transform);
        
        g2d.setColor(Color.YELLOW);
        
        // Desenha linhas radiais para efeito de explosão
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI * 2) / 8;
            double endX = Math.cos(angle) * 20;
            double endY = Math.sin(angle) * 20;
            g2d.drawLine(0, 0, (int)endX, (int)endY);
        }
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Desenha barra de vida usando linhas
     */
    public static void drawHealthBar(Graphics2D g2d, double x, double y, int currentHealth, int maxHealth, int width) {
        // Aplica escala maior para a barra de vida
        AffineTransform oldTransform = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(x, y);
        transform.scale(1.5, 1.5); // Escala 1.5x para barras de vida
        g2d.setTransform(transform);
        
        // Fundo da barra
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width, 6);
        
        // Barra de vida atual
        int healthWidth = (int)((double)currentHealth / maxHealth * width);
        g2d.setColor(currentHealth > maxHealth * 0.5 ? Color.GREEN : Color.RED);
        g2d.fillRect(1, 1, healthWidth - 1, 4);
        
        // Bordas da barra
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0, 0, width, 0);
        g2d.drawLine(0, 6, width, 6);
        
        g2d.setTransform(oldTransform);
    }
    
    /**
     * Desenha barra de nível no topo da tela
     */
    public static void drawLevelBar(Graphics2D g2d, int level, int experience, int experienceToNextLevel, int screenWidth) {
        // Configura fonte para a barra de nível
        Font levelFont = new Font("Courier New", Font.BOLD, 14);
        g2d.setFont(levelFont);
        
        // Posição da barra (centro superior da tela)
        int barWidth = 300;
        int barHeight = 20;
        int barX = (screenWidth - barWidth) / 2;
        int barY = 20;
        
        // Fundo da barra
        g2d.setColor(new Color(40, 40, 60));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Borda da barra
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Barra de experiência atual
        int expWidth = (int)((double)experience / experienceToNextLevel * (barWidth - 4));
        g2d.setColor(UI_ACCENT);
        g2d.fillRoundRect(barX + 2, barY + 2, expWidth, barHeight - 4, 3, 3);
        
        // Texto do nível
        String levelText = "LEVEL " + level;
        int textWidth = g2d.getFontMetrics().stringWidth(levelText);
        int textX = barX + (barWidth - textWidth) / 2;
        int textY = barY + barHeight / 2 + 5;
        
        g2d.setColor(Color.WHITE);
        g2d.drawString(levelText, textX, textY);
        
        // Texto de experiência (abaixo da barra)
        String expText = experience + "/" + experienceToNextLevel + " XP";
        int expTextWidth = g2d.getFontMetrics().stringWidth(expText);
        int expTextX = barX + (barWidth - expTextWidth) / 2;
        int expTextY = barY + barHeight + 15;
        
        g2d.setColor(UI_TEXT);
        g2d.drawString(expText, expTextX, expTextY);
    }
    
    /**
     * Retorna cor baseada no valor da gema
     */
    private static Color getGemColor(int value) {
        if (value >= 50) return XP_GEM_HIGH;
        if (value >= 20) return XP_GEM_MED;
        return XP_GEM_LOW;
    }
}
