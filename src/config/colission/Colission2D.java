package config.colission;

/** 
 * 
 * @author bnk
*/
public class Colission2D {
    private double x;
    private double y;
    private double rangeX;
    private double rangeY;

    public Colission2D(double rangeX, double rangeY) {
        this.rangeX = rangeX;
        this.rangeY = rangeY;
    }

    // -- Getters --
    public double getX() { return x; }
    public double getY() { return y; }
    public double getrangeX() { return rangeX; }
    public double getrangeY() { return rangeY; }

    // -- Setters --
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setrangeX(double rangeX) { this.rangeX = rangeX; }
    public void setrangeY(double rangeY) { this.rangeY = rangeY; }
}
