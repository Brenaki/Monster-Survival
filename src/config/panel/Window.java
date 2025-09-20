package config.panel;

import javax.swing.JPanel;

import config.combat.Projectile;
import config.spawn.SpawnManager;
import config.graphics.PixelArtRenderer;
import config.graphics.ParticleSystem;
import config.graphics.SpriteManager;
import game.entity.enemy.Enemy;
import game.entity.player.Player;
import game.entity.pickup.ExperienceGem;
import game.entity.weapons.Weapon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author bnk
 */
public class Window extends JPanel implements KeyListener, Runnable {
    private final Player player;
    private final List<Enemy> enemies;
    private final SpawnManager spawnManager;
    private final List<Projectile> projectiles;
    private final List<ExperienceGem> experienceGems;
    private final ParticleSystem particleSystem;

    // Configurações de spawn
    private int maxEnemies = 8;
    private int spawnInterval = 2000; // 2 segundos

    // Tempo de jogo
    private long lastTimeNano;
    private double deltaTime;
    private long gameStartTime;
    private long gameDuration = 300000; // 5 minutos (300 segundos)
    
    // Sistema de pontuação
    private int score = 0;
    private int enemiesKilled = 0;
    private int gemsCollected = 0;
    
    // Game over
    private String gameOverText = "GAME OVER";
    private String messageGameOverText = "";
    private boolean gameOver = false;
    private boolean restart = false;
    
    // Start screen
    private boolean startScreen = true;
    private String startScreenTitle = "MONSTER SURVIVAL";
    private String startScreenSubtitle = "Sobreviva até o amanhecer!";
    
    // Loading screen
    private boolean loadingScreen = false;
    private String loadingText = "CARREGANDO...";
    
    // Efeitos visuais
    private double pulseTime = 0;
    
    
    public Window() {
        // Inicia na tela de carregamento
        this.loadingScreen = true;
        
        this.player = new Player(960, 540, 3, 8, 8, 100, true);
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.experienceGems = new ArrayList<>();
        this.particleSystem = new ParticleSystem();

        // Configura pontos de spawn nas bordas da tela
        int[] spawnX = { 0, 1920, 0, 1920 }; // Cantos da tela
        int[] spawnY = { 0, 0, 1080, 1080 };

        spawnManager = new SpawnManager(spawnX, spawnY, spawnInterval, maxEnemies);

        setPreferredSize(new Dimension(1920, 1080));

        setFocusable(true);
        addKeyListener(this);

        lastTimeNano = System.nanoTime();
        // gameStartTime será definido quando o jogador pressionar ENTER
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        // Configura renderização pixel-perfect para estilo 8-bit
        PixelArtRenderer.setupPixelPerfect(g2d);

        // -- Background 8-bit (corrige bug da tela branca) --
        g2d.setColor(PixelArtRenderer.BACKGROUND);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Desenha grid de fundo usando linhas
        drawBackgroundGrid(g2d);

        // Atualiza tempo do jogo para efeitos visuais
        pulseTime += deltaTime * 3; // Velocidade do pulso

        // Desenha o player com rotação
        double playerRotation = Math.atan2(player.getLookDirY(), player.getLookDirX());
        PixelArtRenderer.drawPlayer(g2d, player.getX(), player.getY(), playerRotation);
        
        // Desenha barra de vida do player
        PixelArtRenderer.drawHealthBar(g2d, player.getX(), player.getY() - 20, 
            player.getHealth(), player.getInitHealth(), 64);

        // Desenha todos os inimigos com escala baseada na distância
        for (Enemy enemy : this.enemies) {
            double distance = Math.sqrt(Math.pow(enemy.getX() - player.getX(), 2) + Math.pow(enemy.getY() - player.getY(), 2));
            double scale = Math.max(0.7, 1.0 - (distance / 1000.0)); // Efeito de profundidade
            PixelArtRenderer.drawEnemy(g2d, enemy.getX(), enemy.getY(), enemy.getEnemyType(), scale);
            
            // Desenha barra de vida usando linhas
            PixelArtRenderer.drawHealthBar(g2d, enemy.getX(), enemy.getY() - 15, 
                enemy.getHealth(), enemy.getBaseHealth(), 40);
        }

        // Desenha projéteis com rotação
        for (Projectile projectile : this.projectiles) {
            double projRotation = Math.atan2(projectile.getVy(), projectile.getVx());
            PixelArtRenderer.drawProjectile(g2d, projectile.getX(), projectile.getY(), projRotation);
        }

        // Desenha gems de experiência com efeito pulsante
        for (ExperienceGem gem : this.experienceGems) {
            double pulseScale = 1.0 + 0.2 * Math.sin(pulseTime);
            PixelArtRenderer.drawExperienceGem(g2d, gem.getX(), gem.getY(), 
                gem.getExperienceValue(), pulseScale);
        }
        
        // Renderiza sistema de partículas
        particleSystem.render(g2d);

        // Desenha informações do jogo com estilo 8-bit
        drawGameInfo(g2d);
        
        // Desenha barra de nível no topo da tela
        PixelArtRenderer.drawLevelBar(g2d, player.getLevel(), player.getExperience(), 
            player.getExperienceToNextLevel(), getWidth());
    }

    /**
     * Desenha grid de fundo usando linhas
     */
    private void drawBackgroundGrid(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 60)); // Cor mais escura para o grid
        
        // Linhas verticais
        for (int x = 0; x < getWidth(); x += 40) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        
        // Linhas horizontais
        for (int y = 0; y < getHeight(); y += 40) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }
    private void drawGameInfo(Graphics2D g2d) {
        // Configura fonte 8-bit
        Font pixelFont = new Font("Courier New", Font.BOLD, 16);
        Font largeFont = new Font("Courier New", Font.BOLD, 24);
        g2d.setFont(pixelFont);
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        
        // Tempo restante
        long timeRemaining = gameDuration - (System.currentTimeMillis() - gameStartTime);
        int secondsRemaining = (int) (timeRemaining / 1000);
        g2d.drawString("TEMPO: " + secondsRemaining + "s", 20, 30);
        
        // Pontuação
        g2d.setColor(PixelArtRenderer.UI_ACCENT);
        g2d.drawString("SCORE: " + score, 20, 50);
        
        // Estatísticas
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        g2d.drawString("INIMIGOS: " + enemies.size(), 20, 70);
        g2d.drawString("GEMS: " + experienceGems.size(), 20, 90);
        g2d.drawString("MORTOS: " + enemiesKilled, 20, 110);
        g2d.drawString("COLETADOS: " + gemsCollected, 20, 130);

        // Desenha instruções
        drawInstructions(g2d);

        // Loading Screen
        if (loadingScreen) {
            drawLoadingScreen(g2d);
            return;
        }

        // Start Screen
        if (startScreen) {
            drawStartScreen(g2d);
            return;
        }

        // Game Over
        if (gameOver) {
            // Pinta a tela com efeito de fade
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Define fonte maior para o Game Over
            g2d.setFont(largeFont);
            
            // Calcula o centro da tela para o texto "GAME OVER"
            int gameOverWidth = g2d.getFontMetrics().stringWidth(this.gameOverText);
            int gameOverX = (getWidth() - gameOverWidth) / 2;
            
            // Desenha o Game Over em Vermelho
            g2d.setColor(Color.RED);
            g2d.drawString(this.gameOverText, gameOverX, getHeight()/2);

            // Define fonte menor para o texto
            g2d.setFont(pixelFont);
            
            // Calcula o centro da tela para o texto de sobrevivência
            int messageWidth = g2d.getFontMetrics().stringWidth(this.messageGameOverText);
            int messageX = (getWidth() - messageWidth) / 2;
            
            // Desenha o texto
            g2d.setColor(PixelArtRenderer.UI_TEXT);
            g2d.drawString(this.messageGameOverText, messageX, getHeight()/2 + 40);
            
            // Pontuação final
            String finalScore = "PONTUAÇÃO FINAL: " + score;
            int scoreWidth = g2d.getFontMetrics().stringWidth(finalScore);
            int scoreX = (getWidth() - scoreWidth) / 2;
            g2d.setColor(PixelArtRenderer.UI_ACCENT);
            g2d.drawString(finalScore, scoreX, getHeight()/2 + 70);
            
            // Define fonte menor para o texto de reiniciar
            Font restartFont = new Font("Courier New", Font.PLAIN, 14);
            g2d.setFont(restartFont);
            
            // Reinicia o Jogo
            String textRestart = "Pressione 'R' para reiniciar";
            int restartWidth = g2d.getFontMetrics().stringWidth(textRestart);
            int restartX = (getWidth() - restartWidth) / 2;
            
            g2d.setColor(PixelArtRenderer.UI_TEXT);
            g2d.drawString(textRestart, restartX, getHeight()/2 + 100);
            if(this.restart) {
               this.restart(); 
            }
        }
    }
    
    public void restart() {
        this.enemies.clear();
        this.projectiles.clear();
        this.experienceGems.clear();
        this.particleSystem.clear();

        this.player.reset();
        this.gameOver = false;
        this.restart = false;
        this.loadingScreen = true; // Volta para a tela de carregamento
        this.startScreen = true; // Depois vai para a tela inicial
        // gameStartTime será definido quando o jogador pressionar ENTER
        
        // Reset do sistema de pontuação
        this.score = 0;
        this.enemiesKilled = 0;
        this.gemsCollected = 0;
        this.pulseTime = 0;
    }

    
    private void drawInstructions(Graphics2D g2d) {
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        g2d.drawString("WASD ou Setas para mover", 20, getHeight() - 40);
        g2d.drawString("Sobreviva até o amanhecer!", 20, getHeight() - 20);
    }
    
    private void drawStartScreen(Graphics2D g2d) {
        // Configura fonte 8-bit
        Font pixelFont = new Font("Courier New", Font.BOLD, 16);
        Font largeFont = new Font("Courier New", Font.BOLD, 48);
        Font mediumFont = new Font("Courier New", Font.BOLD, 24);
        
        // Pinta a tela com efeito de fade
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Define fonte grande para o título
        g2d.setFont(largeFont);
        
        // Calcula o centro da tela para o título
        int titleWidth = g2d.getFontMetrics().stringWidth(startScreenTitle);
        int titleX = (getWidth() - titleWidth) / 2;
        
        // Desenha o título em laranja
        g2d.setColor(new Color(255, 200, 100));
        g2d.drawString(startScreenTitle, titleX, getHeight()/2 - 60);
        
        // Define fonte média para o subtítulo
        g2d.setFont(mediumFont);
        
        // Calcula o centro da tela para o subtítulo
        int subtitleWidth = g2d.getFontMetrics().stringWidth(startScreenSubtitle);
        int subtitleX = (getWidth() - subtitleWidth) / 2;
        
        // Desenha o subtítulo
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        g2d.drawString(startScreenSubtitle, subtitleX, getHeight()/2 - 10);
        
        // Define fonte menor para as instruções
        g2d.setFont(pixelFont);
        
        // Instruções de controle
        String instructions1 = "WASD ou Setas para mover";
        String instructions2 = "Mouse para mirar e atirar";
        String instructions3 = "Colete gemas para subir de nível";
        
        int inst1Width = g2d.getFontMetrics().stringWidth(instructions1);
        int inst2Width = g2d.getFontMetrics().stringWidth(instructions2);
        int inst3Width = g2d.getFontMetrics().stringWidth(instructions3);
        
        int inst1X = (getWidth() - inst1Width) / 2;
        int inst2X = (getWidth() - inst2Width) / 2;
        int inst3X = (getWidth() - inst3Width) / 2;
        
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        g2d.drawString(instructions1, inst1X, getHeight()/2 + 30);
        g2d.drawString(instructions2, inst2X, getHeight()/2 + 50);
        g2d.drawString(instructions3, inst3X, getHeight()/2 + 70);
        
        // Define fonte menor para o texto de iniciar
        Font startFont = new Font("Courier New", Font.PLAIN, 14);
        g2d.setFont(startFont);
        
        // Texto para iniciar o jogo
        String startText = "Pressione ENTER para iniciar";
        int startWidth = g2d.getFontMetrics().stringWidth(startText);
        int startX = (getWidth() - startWidth) / 2;
        
        // Efeito pulsante para o texto de iniciar
        double startPulse = 1.0 + 0.2 * Math.sin(pulseTime * 5);
        g2d.setColor(new Color(255, 255, 255, (int)(180 * startPulse)));
        g2d.drawString(startText, startX, getHeight()/2 + 120);
    }
    
    private void drawLoadingScreen(Graphics2D g2d) {
        // Configura fonte 8-bit
        Font pixelFont = new Font("Courier New", Font.BOLD, 16);
        Font largeFont = new Font("Courier New", Font.BOLD, 32);
        
        // Pinta a tela com fundo escuro
        g2d.setColor(new Color(20, 20, 40));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Define fonte grande para o texto de carregamento
        g2d.setFont(largeFont);
        
        // Calcula o centro da tela para o texto
        int loadingWidth = g2d.getFontMetrics().stringWidth(loadingText);
        int loadingX = (getWidth() - loadingWidth) / 2;
        
        // Desenha o texto de carregamento
        g2d.setColor(new Color(255, 200, 100));
        g2d.drawString(loadingText, loadingX, getHeight()/2);
        
        // Define fonte menor para pontos de carregamento
        g2d.setFont(pixelFont);
        
        // Desenha pontos animados
        String dots = "";
        int dotCount = (int)((pulseTime * 3) % 4);
        for (int i = 0; i < dotCount; i++) {
            dots += ".";
        }
        
        int dotsWidth = g2d.getFontMetrics().stringWidth(dots);
        int dotsX = (getWidth() - dotsWidth) / 2;
        
        g2d.setColor(PixelArtRenderer.UI_TEXT);
        g2d.drawString(dots, dotsX, getHeight()/2 + 40);
        
        // Desenha barra de progresso simples
        int barWidth = 300;
        int barHeight = 20;
        int barX = (getWidth() - barWidth) / 2;
        int barY = getHeight()/2 + 80;
        
        // Fundo da barra
        g2d.setColor(new Color(40, 40, 60));
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Borda da barra
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 5, 5);
        
        // Barra de progresso animada
        int progressWidth = (int)((Math.sin(pulseTime * 2) + 1) * 0.5 * (barWidth - 4));
        g2d.setColor(PixelArtRenderer.UI_ACCENT);
        g2d.fillRoundRect(barX + 2, barY + 2, progressWidth, barHeight - 4, 3, 3);
    }

    private void gameOver(String why){
        if (this.gameOver) {
            return; // Já está em game over
        }
        this.gameOver = true;
        this.messageGameOverText = why;
    }

    private void update() {
        // Atualiza efeito pulsante sempre
        pulseTime += deltaTime;
        
        // Verifica se ainda está carregando sprites
        SpriteManager spriteManager = SpriteManager.getInstance();
        if (spriteManager.isLoading()) {
            loadingScreen = true;
            return;
        } else if (loadingScreen) {
            // Carregamento concluído, vai para tela inicial
            loadingScreen = false;
            return;
        }
        
        if (this.startScreen) {
            // Atualiza apenas o efeito pulsante na tela inicial
            return;
        }
        
        if (this.gameOver) return;
        
        // Atualiza sistema de partículas
        particleSystem.update(deltaTime);
        
        // Verifica se o tempo acabou (corrige bug do tempo não parar)
        if (System.currentTimeMillis() - gameStartTime >= gameDuration) {
            this.gameOver("Sobreviveu até o amanhecer!");
            return;
        }
		// Atualiza direção de olhar conforme entrada atual
		double ldx = 0, ldy = 0;
		if (this.player.getMoveUp()) ldy -= 1;
		if (this.player.getMoveDown()) ldy += 1;
		if (this.player.getMoveLeft()) ldx -= 1;
		if (this.player.getMoveRight()) ldx += 1;
		if (ldx != 0 || ldy != 0) this.player.setLookDir(ldx, ldy);

		// Atualiza movimento do player
		if (this.player.getMoveUp())
			this.player.setY(this.player.getY() - this.player.getSpeed());
		if (this.player.getMoveDown())
			this.player.setY(this.player.getY() + this.player.getSpeed());
		if (this.player.getMoveLeft())
			this.player.setX(this.player.getX() - this.player.getSpeed());
		if (this.player.getMoveRight())
			this.player.setX(this.player.getX() + this.player.getSpeed());

        // Game over, se o player morrer
        if(this.player.isAlive() == false) {
            this.gameOver("Você morreu!");
        }
        
		// Atualiza posição das armas
		this.player.updateWeaponPositions();

		// Atualiza dificuldade baseada no tempo
		updateDifficulty();
		
		// Verifica se deve spawnar um novo inimigo
		if (spawnManager.shouldSpawn(this.enemies.size())) {
			spawnEnemy();
		}

		// Atualiza todos os inimigos
		updateEnemies();
		resolvePlayerEnemyCollisions();

		// Tiro automático das armas do player
		autoShoot();

		// Atualiza projéteis
		updateProjectiles(this.deltaTime);

		// Atualiza gems de experiência
		updateExperienceGems();

		// Remove inimigos mortos ou fora da tela
		removeDeadEnemies();
	}

	private void autoShoot() {
		// Para cada arma do player, verifica se pode atirar
		for (Weapon weapon : player.getWeapons()) {
			if (weapon.canAttack()) {
				double dirX = this.player.getLookDirX();
				double dirY = this.player.getLookDirY();
				if (dirX == 0 && dirY == 0) { dirX = 1.0; dirY = 0.0; }

				// Cria projétil específico da arma
				Projectile p = weapon.createProjectile(dirX, dirY);
				projectiles.add(p);
				weapon.attack(); // Marca que a arma atirou
			}
		}
	}

	private void updateProjectiles(double dt) {
		Iterator<Projectile> it = projectiles.iterator();
		while (it.hasNext()) {
			Projectile p = it.next();

			// Movimento e distância
			p.update(dt);

			// Checa colisão com inimigos
			boolean hit = false;
			for (Enemy e : enemies) {
				if (e.getTeam() == p.getTeam()) continue;
				if (p.onHit(e)) {
					hit = true;
					// Se o inimigo morreu, gera gem de experiência e efeitos
					if (e.getHealth() <= 0) {
						spawnExperienceGem(e.getX(), e.getY());
						// Adiciona efeito de explosão
						particleSystem.addExplosion(e.getX(), e.getY(), 8);
						// Atualiza pontuação
						enemiesKilled++;
						score += getEnemyScore(e.getEnemyType());
					}
					break;
				}
			}
			if (hit) {
				it.remove();
				continue;
			}

			// Remove se expirar por alcance
			if (p.isExpired()) {
				it.remove();
				continue;
			}

			// Remove se sair da tela com margem
			if (p.getX() < -50 || p.getX() > getWidth() + 50 || p.getY() < -50 || p.getY() > getHeight() + 50) {
				it.remove();
			}
		}
	}

	private void updateExperienceGems() {
		Iterator<ExperienceGem> it = experienceGems.iterator();
		while (it.hasNext()) {
			ExperienceGem gem = it.next();
			
			// Remove gems expiradas
			if (gem.isExpired()) {
				it.remove();
				continue;
			}
			
			// Verifica colisão com o player
			if (player.isCollidingWith(gem)) {
				player.addExperience(gem.getExperienceValue());
				// Adiciona efeito de coleta
				particleSystem.addXPCollection(gem.getX(), gem.getY());
				// Atualiza pontuação
				gemsCollected++;
				score += gem.getExperienceValue() * 2; // Pontos extras por coletar gems
				it.remove();
			}
		}
	}
	
	private void spawnExperienceGem(double x, double y) {
		// Gera valor de XP baseado no nível do player
		int xpValue = 10 + (player.getLevel() * 2);
		ExperienceGem gem = new ExperienceGem(x, y, xpValue);
		experienceGems.add(gem);
	}
	
	private void updateDifficulty() {
		// Não atualiza dificuldade se o jogo acabou
		if (this.gameOver) return;
		
		long timeElapsed = System.currentTimeMillis() - gameStartTime;
		int minutesElapsed = (int) (timeElapsed / 60000); // minutos
		
		// Aumenta número máximo de inimigos a cada minuto
		maxEnemies = 8 + (minutesElapsed * 2);
		
		// Diminui intervalo de spawn a cada minuto
		spawnInterval = Math.max(500, 2000 - (minutesElapsed * 200));
		
		// Atualiza o spawn manager
		spawnManager.setMaxMonsters(maxEnemies);
		spawnManager.setSpawnInterval(spawnInterval);
	}

    private void spawnEnemy() {
        int[] spawnPos = spawnManager.getRandomSpawnPosition(
                this.player.getX(), this.player.getY(),
                getWidth(), getHeight());

        // Escolhe tipo de inimigo baseado no nível do player
        String enemyType = chooseEnemyType();
        Enemy newEnemy = createEnemyByType(spawnPos[0], spawnPos[1], enemyType);

        this.enemies.add(newEnemy);
        System.out.println("Inimigo " + enemyType + " spawnado! Total: " + this.enemies.size());
    }
    
    private String chooseEnemyType() {
        int playerLevel = player.getLevel();
        double rand = Math.random();
        
        if (playerLevel >= 10 && rand < 0.1) {
            return "Boss";
        } else if (playerLevel >= 5 && rand < 0.3) {
            return "Tank";
        } else if (rand < 0.4) {
            return "Fast";
        } else {
            return "Normal";
        }
    }
    
    private Enemy createEnemyByType(int x, int y, String type) {
        switch (type) {
            case "Fast":
                return new Enemy(x, y, 2.5, 6, 6, 50, true, 500, "Fast");
            case "Tank":
                return new Enemy(x, y, 0.8, 12, 12, 200, true, 1000, "Tank");
            case "Boss":
                return new Enemy(x, y, 1.2, 16, 16, 500, true, 1500, "Boss");
            default: // Normal
                return new Enemy(x, y, 1.12, 8, 8, 100, true, 800, "Normal");
        }
    }

    private void updateEnemies() {
        // Primeiro, atualiza o movimento dos inimigos
        for (Enemy enemy : this.enemies) {
            enemy.followPlayer(this.player.getX(), this.player.getY());
        }

        // Depois, resolve colisões entre inimigos
        resolveEnemyCollisions();
    }

    private void resolveEnemyCollisions() {
        // Verifica colisão entre todos os pares de inimigos
        for (int i = 0; i < this.enemies.size(); i++) {
            for (int j = i + 1; j < this.enemies.size(); j++) {
                Enemy enemy1 = this.enemies.get(i);
                Enemy enemy2 = this.enemies.get(j);

                // Se estão colidindo, separa eles
                if (enemy1.isCollidingWith(enemy2)) {
                    enemy1.resolveCollision(enemy2);
                }
            }
        }
    }

    private void removeDeadEnemies() {
        Iterator<Enemy> iterator = this.enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();

            // Remove se estiver fora da tela (com margem)
            if (enemy.getX() < -50 || enemy.getX() > getWidth() + 50 ||
                    enemy.getY() < -50 || enemy.getY() > getHeight() + 50) {
                iterator.remove();
                System.out.println("Inimigo removido (fora da tela). Total: " + this.enemies.size());
            }
            if (enemy.getHealth() == 0) {
                iterator.remove();
                System.out.println("Inimigo removido (morto). Total: " + this.enemies.size());
            }
        }
    }

    public int getEnemyCount() {
        return enemies.size();
    }

    private void resolvePlayerEnemyCollisions() {
        // Verifica colisão entre player e inimigo
        for (Enemy enemy : this.enemies) {
            if (this.player.isCollidingWith(enemy)) {
                this.player.resolveCollision(enemy);
                // Dano corpo a corpo - inimigo causa dano direto no player
                if (this.player.getHealth() > 0 && enemy.canDamage()) {
                    this.player.setHealth(player.getHealth() - 1    ); // 5 de dano por contato
                    enemy.dealDamage(); // Marca que o inimigo causou dano
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            long now = System.nanoTime();
            this.deltaTime = (now - this.lastTimeNano) / 1_000_000_000.0; // segundos
            this.lastTimeNano = now;

            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Controle da tela inicial
        if (startScreen && key == KeyEvent.VK_ENTER) {
            startScreen = false;
            gameStartTime = System.currentTimeMillis(); // Inicia o cronômetro
            return;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
            this.player.setMoveUp(true);
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
            this.player.setMoveDown(true);
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            this.player.setMoveLeft(true);
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
            this.player.setMoveRight(true);
        if(key == KeyEvent.VK_R) this.restart = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8)
            this.player.setMoveUp(false);
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2)
            this.player.setMoveDown(false);
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4)
            this.player.setMoveLeft(false);
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6)
            this.player.setMoveRight(false);
        if(key == KeyEvent.VK_R) this.restart = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * Retorna pontuação baseada no tipo de inimigo
     */
    private int getEnemyScore(String enemyType) {
        switch (enemyType) {
            case "Fast": return 50;
            case "Tank": return 100;
            case "Boss": return 500;
            default: return 25; // Normal
        }
    }
}
