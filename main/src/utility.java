import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class utility {
    /*Constants */
    public static final double LENGTH = 30;// Size of hexagon (Distance from center to any vertices)


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

    public static Text makeText(String s, int x, int y){
        Text text = new Text(s);
        text.setX(x); // X position on screen
        text.setY(y); // Y position on screen
        text.setFont(javafx.scene.text.Font.font("Verdana", 30));
        text.setFill(javafx.scene.paint.Color.BLACK);
        return text;
    }
}
