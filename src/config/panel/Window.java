package config.panel;

import javax.swing.JPanel;

import game.entity.enemy.Enemy;
import game.entity.player.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * @author bnk
 */
public class Window extends JPanel implements KeyListener, Runnable {
    private Player player;
    private Enemy enemy;

    public Window() {
        player = new Player(250, 250, 3, 8, 8, 10, true);
        enemy = new Enemy(200, 200, 1.12, 8, 8, 10, true, 45);

        setPreferredSize(new Dimension(500,500));

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

        player.paint(g2d);
        enemy.paint(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void update() {
        if (this.player.getMoveUp()) this.player.setY(this.player.getY() - this.player.getSpeed());
        if (this.player.getMoveDown()) this.player.setY(this.player.getY() + this.player.getSpeed());
        if (this.player.getMoveLeft()) this.player.setX(this.player.getX() - this.player.getSpeed());
        if (this.player.getMoveRight()) this.player.setX(this.player.getX() + this.player.getSpeed());
        this.enemy.followPlayer(this.player.getX(), this.player.getY());
    }
    
        
        @Override
        public void run(){
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
            
            if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) this.player.setMoveUp(true);
            if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) this.player.setMoveDown(true);
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) this.player.setMoveLeft(true);
            if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) this.player.setMoveRight(true);
    
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            
            if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) this.player.setMoveUp(false);
            if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) this.player.setMoveDown(false);
            if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) this.player.setMoveLeft(false);
            if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) this.player.setMoveRight(false);
        }
        
        @Override
        public void keyTyped(KeyEvent e) {}
}
