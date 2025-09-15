package game.entity.base;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import config.colission.Colission2D;

/**
* 
* @author bnk
*/
public class Entity {
    private double x;
    private double y;
    private double speed;
    private int width;
    private int height;
    private int health;
    private boolean isVisible;
    private Colission2D colission;

    public Entity(double x, double y, double speed, int width, int height, int health, boolean isVisible, double rangeX, double rangeY) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.health = health;
        this.isVisible = isVisible;
        this.colission = new Colission2D(rangeX, rangeY);
        this.colission.setX(x);
        this.colission.setY(y);
    }

    // -- Getters --
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getSpeed() { return this.speed; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public int getHealth() { return this.health; }
    public boolean getIsVisible() { return this.isVisible; }

    // -- Setters --
    public void setX(double x) { 
        this.x = x; 
        this.colission.setX(x);
    }
    public void setY(double y) { 
        this.y = y; 
        this.colission.setY(y);
    }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setHealth(int health) { this.health = health; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }
    
    // -- Colision Methods --
    public Colission2D getColission() { return this.colission; }
    public void setColissionRange(double rangeX, double rangeY) {
        this.colission.setrangeX(rangeX);
        this.colission.setrangeY(rangeY);
    }
    
    public boolean isCollidingWith(Entity other) {
        double distance = Math.sqrt(
            Math.pow(this.getX() - other.getX(), 2) + 
            Math.pow(this.getY() - other.getY(), 2)
        );
        return distance < (this.getColission().getrangeX() + other.getColission().getrangeX()) && distance < (this.getColission().getrangeY() + other.getColission().getrangeY());
    }

    public void paint(Graphics2D g2d) {
        GeneralPath gp = new GeneralPath();
        if (this.getHealth() > 0) {
            g2d.drawString(String.valueOf(this.getHealth()) + "%", (int)this.getX(), (int)this.getY()-15);
            gp.moveTo(this.getX(), this.getY()-10);
            gp.lineTo(this.getX()+(getHealth()/2.5), this.getY()-10);
        }
        g2d.draw(gp);
    }
    
    public void resolveCollision(Entity other) {
        if (!this.isCollidingWith(other)) {
            return; // Não há colisão, não precisa fazer nada
        }
        
        // Calcula a distância entre as entidades
        double dx = other.getX() - this.getX();
        double dy = other.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Evita divisão por zero
        if (distance == 0) {
            distance = 0.1;
        }
        
        // Calcula a distância mínima necessária para não colidir
        double minDistance = this.getColission().getrangeX() + other.getColission().getrangeX();
        
        // Calcula o quanto precisa se afastar
        double overlap = minDistance - distance;
        
        // Normaliza a direção
        double separationX = (dx / distance) * (overlap / 2);
        double separationY = (dy / distance) * (overlap / 2);
        
        // Move ambas as entidades para se afastarem
        this.setX(this.getX() - separationX);
        this.setY(this.getY() - separationY);
        other.setX(other.getX() + separationX);
        other.setY(other.getY() + separationY);
    }
}
