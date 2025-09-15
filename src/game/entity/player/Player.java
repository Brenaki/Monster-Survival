package game.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;

import game.entity.base.Entity;
/**
 * 
 * @author bnk
 */
public class Player extends Entity {
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    
    public Player(int x, int y, int speed, int width, int height, int health, boolean isVisible) {
        super(x, y, speed, width, height, health, isVisible, 22, 22);
    }

    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect((int) this.getX(), (int) this.getY(), 40, 40);
        super.paint(g2d);
    }

    // -- Getters --
    public boolean getMoveUp() { return this.moveUp; }
    public boolean getMoveDown() { return this.moveDown; }
    public boolean getMoveLeft() { return this.moveLeft; }
    public boolean getMoveRight() { return this.moveRight; }

    // -- Setters --
    public void setMoveUp(boolean moveUp) { this.moveUp = moveUp; }
    public void setMoveDown(boolean moveDown) { this.moveDown = moveDown; }
    public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
    public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }
}
