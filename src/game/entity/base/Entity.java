package game.entity.base;

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

    public Entity(double x, double y, double speed, int width, int height, int health, boolean isVisible) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.health = health;
        this.isVisible = isVisible;
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
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setHealth(int health) { this.health = health; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }
}
