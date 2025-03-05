import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class utility {
    /*Constants */
    private static final double LENGTH = 30; // Size of hexagon (Distance from center to any vertice)

    public static Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(LENGTH / 1.5);
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            circle.setFill(Color.BLUE); // Fill color
        } else {
            circle.setFill(Color.RED); // Fill color
        }
        circle.setStroke(Color.BLACK); // Border color
        circle.setStrokeWidth(2);
        return circle;
    }
}
