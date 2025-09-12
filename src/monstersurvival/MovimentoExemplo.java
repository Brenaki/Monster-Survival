package monstersurvival;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 *
 * @author deinfo
 */
public class MovimentoExemplo extends JPanel implements KeyListener, Runnable {
    private int x = 100; 
    private int y = 100;
    private int speed = 5;
    private boolean up, down, left, right, damage;
    private int damageY;
    
    public MovimentoExemplo() {
        setPreferredSize(new Dimension(500, 500));
        
        setFocusable(true);
        addKeyListener(this);
        
        new Thread(this).start();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
                
        g2d.setColor(Color.RED);
        g2d.fillRect(this.x, this.y, 40, 40);
        
        g2d.setColor(Color.WHITE);
        g2d.drawString("Use as setas para mover o quadrado", 10, 20);
        
        if (damage) {
            int xDamage = this.x;
            int yDamage = this.damageY;
            g2d.setColor(Color.YELLOW);
            GeneralPath gp = new GeneralPath();
            gp.moveTo(xDamage + 10, yDamage);
            gp.lineTo(xDamage + 30, yDamage);
            gp.lineTo(xDamage + 20, yDamage - 15);
            gp.closePath();
            g2d.fill(gp);
        }

        
    }
    
    private void update() {
    if (this.up) this.y -= this.speed;
    if (this.down) this.y += this.speed;
    if (this.left) this.x -= this.speed;
    if (this.right) this.x += this.speed;

        if (damage) {
            damageY -= 10;

            if (damageY < -20) { 
                damage = false; 
            }
        }
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
        
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) this.up = true;
        if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) this.down = true;
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) this.left = true;
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) this.right = true;
        if (key == KeyEvent.VK_SPACE && !damage) {
            this.damage = true;
            this.damageY = this.y - 15;
        }

    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_NUMPAD8) this.up = false;
        if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S || key == KeyEvent.VK_NUMPAD2) this.down = false;
        if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A || key == KeyEvent.VK_NUMPAD4) this.left = false;
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D || key == KeyEvent.VK_NUMPAD6) this.right = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Exemplo - Movimento Java2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new MovimentoExemplo());
        frame.pack();
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
