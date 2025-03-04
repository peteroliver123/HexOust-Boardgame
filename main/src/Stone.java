import javafx.scene.shape.Circle;

public class Stone extends Circle {
    double x;
    double y;

    public Stone(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
