package config.graphics;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Sistema de gerenciamento de sprites 8-bit
 * Carrega e renderiza sprites com transformações geométricas
 * @author bnk
 */
public class SpriteManager {
    
    private static SpriteManager instance;
    private Map<String, BufferedImage> sprites;
    
    private SpriteManager() {
        this.sprites = new HashMap<>();
        loadSprites();
    }
    
    public static SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }
    
    /**
     * Carrega todos os sprites da pasta sprites/
     */
    private void loadSprites() {
        try {
            // Carrega sprite do player
            if (spriteExists("Playert.png")) {
                sprites.put("player", loadSprite("Playert.png"));
            }
            
            // Carrega sprites dos inimigos
            if (spriteExists("Edefault.png")) {
                sprites.put("enemy_default", loadSprite("Edefault.png"));
            }
            if (spriteExists("Efast.png")) {
                sprites.put("enemy_fast", loadSprite("Efast.png"));
            }
            if (spriteExists("Etankt.png")) {
                sprites.put("enemy_tank", loadSprite("Etankt.png"));
            }
            if (spriteExists("Eboss.png")) {
                sprites.put("enemy_boss", loadSprite("Eboss.png"));
            }
            
            System.out.println("Sprites carregados com sucesso! Total: " + sprites.size());
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar sprites: " + e.getMessage());
            System.err.println("Continuando com renderização pixel art...");
        }
    }
    
    /**
     * Verifica se um sprite existe
     */
    private boolean spriteExists(String filename) {
        // Verifica no classpath primeiro
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sprites/" + filename);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignora erro de fechamento
            }
            return true;
        }
        
        // Verifica no diretório sprites/
        java.io.File spriteFile = new java.io.File("sprites/" + filename);
        return spriteFile.exists();
    }
    
    /**
     * Carrega um sprite específico
     */
    private BufferedImage loadSprite(String filename) throws IOException {
        InputStream inputStream = null;
        
        try {
            // Primeiro tenta carregar do classpath
            inputStream = getClass().getClassLoader().getResourceAsStream("sprites/" + filename);
            
            if (inputStream == null) {
                // Se não encontrar no classpath, tenta carregar diretamente do diretório sprites/
                java.io.File spriteFile = new java.io.File("sprites/" + filename);
                if (spriteFile.exists()) {
                    inputStream = new java.io.FileInputStream(spriteFile);
                } else {
                    throw new IOException("Sprite não encontrado: " + filename);
                }
            }
            
            BufferedImage sprite = ImageIO.read(inputStream);
            
            if (sprite == null) {
                throw new IOException("Erro ao decodificar sprite: " + filename);
            }
            
            System.out.println("Sprite carregado: " + filename + " (" + sprite.getWidth() + "x" + sprite.getHeight() + ")");
            return sprite;
            
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignora erro de fechamento
                }
            }
        }
    }
    
    /**
     * Renderiza sprite do player com rotação
     */
    public void drawPlayer(Graphics2D g2d, double x, double y, double rotation) {
        BufferedImage sprite = sprites.get("player");
        if (sprite != null) {
            // Escala 8x para sprites de 8x8 ficarem 64x64
            drawSpriteWithTransform(g2d, sprite, x, y, rotation, 8.0, 8.0);
        }
    }
    
    /**
     * Renderiza sprite do inimigo baseado no tipo
     */
    public void drawEnemy(Graphics2D g2d, double x, double y, String enemyType, double scale) {
        String spriteKey = getEnemySpriteKey(enemyType);
        BufferedImage sprite = sprites.get(spriteKey);
        
        if (sprite != null) {
            // Adiciona rotação baseada no movimento para efeito dinâmico
            double rotation = Math.sin(System.currentTimeMillis() * 0.005) * 0.1;
            
            // Escala base 8x para sprites de 8x8 ficarem 64x64, multiplicada pela escala de profundidade
            double baseScale = 8.0;
            double finalScale = baseScale * scale;
            
            drawSpriteWithTransform(g2d, sprite, x, y, rotation, finalScale, finalScale);
        }
    }
    
    /**
     * Renderiza sprite com transformações geométricas
     */
    private void drawSpriteWithTransform(Graphics2D g2d, BufferedImage sprite, 
                                       double x, double y, double rotation, 
                                       double scaleX, double scaleY) {
        
        AffineTransform oldTransform = g2d.getTransform();
        
        try {
            // Cria transformação combinada
            AffineTransform transform = new AffineTransform();
            
            // Translação para o centro do sprite
            transform.translate(x + sprite.getWidth()/2.0, y + sprite.getHeight()/2.0);
            
            // Determina se deve inverter horizontalmente baseado na direção X
            double finalScaleX = scaleX;
            if (Math.abs(Math.cos(rotation)) > Math.abs(Math.sin(rotation))) {
                // Movimento principal no eixo X - inverte horizontalmente
                finalScaleX = -scaleX;
            }
            
            // Escala (com inversão horizontal se necessário)
            transform.scale(finalScaleX, scaleY);
            
            // Translação de volta para a posição original
            transform.translate(-sprite.getWidth()/2.0, -sprite.getHeight()/2.0);
            
            // Aplica transformação
            g2d.setTransform(transform);
            
            // Desenha o sprite
            g2d.drawImage(sprite, 0, 0, null);
            
        } finally {
            // Sempre restaura transformação original
            g2d.setTransform(oldTransform);
        }
    }
    
    /**
     * Retorna a chave do sprite baseada no tipo de inimigo
     */
    private String getEnemySpriteKey(String enemyType) {
        switch (enemyType) {
            case "Fast": return "enemy_fast";
            case "Tank": return "enemy_tank";
            case "Boss": return "enemy_boss";
            default: return "enemy_default";
        }
    }
    
    /**
     * Retorna um sprite específico
     */
    public BufferedImage getSprite(String key) {
        return sprites.get(key);
    }
    
    /**
     * Verifica se um sprite foi carregado
     */
    public boolean hasSprite(String key) {
        return sprites.containsKey(key) && sprites.get(key) != null;
    }
    
    /**
     * Retorna informações sobre os sprites carregados
     */
    public void printSpriteInfo() {
        System.out.println("=== SPRITES CARREGADOS ===");
        for (Map.Entry<String, BufferedImage> entry : sprites.entrySet()) {
            BufferedImage sprite = entry.getValue();
            System.out.println(entry.getKey() + ": " + 
                sprite.getWidth() + "x" + sprite.getHeight() + " pixels");
        }
        System.out.println("=========================");
    }
}
