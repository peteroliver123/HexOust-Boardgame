/*Imports */
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*Utility Class */
public class utility {
    /**************
       CONSTANTS
     ***************/

    public static final double LENGTH = 30;// Size of hexagon (Distance from center to any vertices)
    public static final Font DEFAULT_FONT = new Font("verdana", 30);//default font with name of font and size
    public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;//default text color

    /**************
    UTILITY METHODS
    **************/

    /*
    This function takes a double x, double y and draws a circle with centre(x,y) on
    the screen. If the current global color is blue the circle is blue else red.
     */
    public static Circle drawCircle(Point position) {
        Circle circle = new Circle();
        circle.setCenterX(position.getX());
        circle.setCenterY(position.getY());
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

    /*
    This function takes a String s, int x, int y and draws
    the text in String s to the screen at position(x,y)
     */
    public static Text makeText(String s, Point position){
        Text text = new Text(s);
        text.setX(position.getX()); // X position on screen
        text.setY(position.getY()); // Y position on screen
        text.setFont(DEFAULT_FONT);
        text.setFill(DEFAULT_TEXT_COLOR);
        return text;
    }
}
