package config.panel;

import javax.swing.JPanel;

import config.combat.Projectile;
import config.spawn.SpawnManager;
import game.entity.enemy.Enemy;
import game.entity.player.Player;
import game.entity.pickup.ExperienceGem;
import game.entity.weapons.Weapon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

    // Configurações de spawn
    private int maxEnemies = 8;
    private int spawnInterval = 2000; // 2 segundos

    // Tempo de jogo
    private long lastTimeNano;
    private double deltaTime;
    private long gameStartTime;
    private long gameDuration = 300000; // 5 minutos (300 segundos)
    
    // Game over
    private String gameOverText = "Game Over";
    private String messageGameOverText = "";
    private boolean gameOver = false;
    private boolean restart = false;
    
    
    public Window() {
        this.player = new Player(960, 540, 3, 8, 8, 10, true);
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.experienceGems = new ArrayList<>();

        // Configura pontos de spawn nas bordas da tela
        int[] spawnX = { 0, 1920, 0, 1920 }; // Cantos da tela
        int[] spawnY = { 0, 0, 1080, 1080 };

        spawnManager = new SpawnManager(spawnX, spawnY, spawnInterval, maxEnemies);

        setPreferredSize(new Dimension(1920, 1080));

        setFocusable(true);
        addKeyListener(this);

        lastTimeNano = System.nanoTime();
        gameStartTime = System.currentTimeMillis();
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // -- Background --
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Desenha o player
        player.paint(g2d);

        // Desenha todos os inimigos
        for (Enemy enemy : this.enemies) {
            enemy.paint(g2d);
        }

        // Desenha projéteis
        for (Projectile projectile : this.projectiles) {
            projectile.render(g2d);
        }

        // Desenha gems de experiência
        for (ExperienceGem gem : this.experienceGems) {
            gem.paint(g2d);
        }

        // Desenha informações do jogo
        drawGameInfo(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void drawGameInfo(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        
        // Tempo restante
        long timeRemaining = gameDuration - (System.currentTimeMillis() - gameStartTime);
        int secondsRemaining = (int) (timeRemaining / 1000);
        g2d.drawString("Tempo: " + secondsRemaining + "s", 10, 20);
        
        // Inimigos na tela
        g2d.drawString("Inimigos: " + enemies.size(), 10, 40);
        
        // Gems na tela
        g2d.drawString("Gems: " + experienceGems.size(), 10, 60);
        
        // Desenha XP (necessaria para proximo nivel) e o Level do player
        g2d.drawString("XP: " + this.player.getExperience() + "/" + this.player.getExperienceToNextLevel(), 10, 80);
        g2d.drawString("Level: " + this.player.getLevel(), 10, 100);

        // Desenha instruções
        drawInstructions(g2d);

        // Game Over
        if (gameOver) {
            // Pinta a tela de preto
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Define fonte maior para o Game Over
            Font gameOverFont = new Font("Arial", Font.BOLD, 96);
            g2d.setFont(gameOverFont);
            
            // Calcula o centro da tela para o texto "GAME OVER"
            int gameOverWidth = g2d.getFontMetrics().stringWidth(this.gameOverText);
            int gameOverX = (getWidth() - gameOverWidth) / 2;
            
            // Desenha o Game Over em Vermelho
            g2d.setColor(Color.RED);
            g2d.drawString(this.gameOverText, gameOverX, getHeight()/2);

            // Define fonte menor para o texto
            Font messageFont = new Font("Arial", Font.PLAIN, 32);
            g2d.setFont(messageFont);
            
            // Calcula o centro da tela para o texto de sobrevivência
            int messageWidth = g2d.getFontMetrics().stringWidth(this.messageGameOverText);
            int messageX = (getWidth() - messageWidth) / 2;
            
            // Desenha o texto a 
            g2d.setColor(Color.WHITE);
            g2d.drawString(this.messageGameOverText, messageX, getHeight()/2 + 50);
            
            
            // Define fonte menor para o texto
            Font restartFont = new Font("Arial", Font.PLAIN, 24);
            g2d.setFont(restartFont);
            
            // Reinicia o Jogo
            String textRestart = "Renicie o jogo usando a tecla 'R'";
            int restartWidth = g2d.getFontMetrics().stringWidth(textRestart);
            int restartX = (getWidth() - restartWidth) / 2;
            
            g2d.setColor(Color.WHITE);
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

        this.player.reset();
        this.gameOver = false;
        this.restart = false;
        this.gameStartTime = System.currentTimeMillis(); // reinicia o cronômetro
    }

    
    private void drawInstructions(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawString("WASD ou Setas para mover", 10, getHeight() - 40);
        g2d.drawString("Sobreviva até o amanhecer!", 10, getHeight() - 20);
    }

    private void gameOver(String why){
        if (this.gameOver) {
            return; // Já está em game over
        }
        this.gameOver = true;
        this.messageGameOverText = why;
    }

    private void update() {
        if (this.gameOver) return;
        
        // Verifica se o tempo acabou
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
					// Se o inimigo morreu, gera gem de experiência
					if (e.getHealth() <= 0) {
						spawnExperienceGem(e.getX(), e.getY());
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
}
