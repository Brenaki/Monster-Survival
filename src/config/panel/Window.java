package config.panel;

import javax.swing.JPanel;

import config.spawn.SpawnManager;
import game.entity.enemy.Enemy;
import game.entity.player.Player;

import java.awt.Color;
import java.awt.Dimension;
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

    // Configurações de spawn
    private final int maxEnemies = 8;
    private final int spawnInterval = 2000; // 2 segundos

    public Window() {
        player = new Player(250, 250, 3, 8, 8, 100, true);
        enemies = new ArrayList<>();

        // Configura pontos de spawn nas bordas da tela
        int[] spawnX = { 0, 500, 0, 500 }; // Cantos da tela
        int[] spawnY = { 0, 0, 500, 500 };

        spawnManager = new SpawnManager(spawnX, spawnY, spawnInterval, maxEnemies);

        setPreferredSize(new Dimension(800, 600));

        setFocusable(true);
        addKeyListener(this);

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

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void update() {
        // Atualiza movimento do player
        if (this.player.getMoveUp())
            this.player.setY(this.player.getY() - this.player.getSpeed());
        if (this.player.getMoveDown())
            this.player.setY(this.player.getY() + this.player.getSpeed());
        if (this.player.getMoveLeft())
            this.player.setX(this.player.getX() - this.player.getSpeed());
        if (this.player.getMoveRight())
            this.player.setX(this.player.getX() + this.player.getSpeed());

        // Verifica se deve spawnar um novo inimigo
        if (spawnManager.shouldSpawn(this.enemies.size())) {
            spawnEnemy();
        }

        // Atualiza todos os inimigos
        updateEnemies();
        resolvePlayerEnemyCollisions();

        // Remove inimigos mortos ou fora da tela
        removeDeadEnemies();
    }

    private void spawnEnemy() {
        int[] spawnPos = spawnManager.getRandomSpawnPosition(
                this.player.getX(), this.player.getY(),
                getWidth(), getHeight());

        Enemy newEnemy = new Enemy(
                spawnPos[0], spawnPos[1],
                1.12, 8, 8, 100, true, 5, 1, 1000);

        this.enemies.add(newEnemy);
        System.out.println("Inimigo spawnado! Total: " + this.enemies.size());
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
                if (this.player.getHealth() > 0 && enemy.getCombat().shouldDamage()) {
                    this.player.setHealth(player.getHealth() - 1);
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
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
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
