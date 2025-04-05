/*Imports */
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*Utility Class */
public class Utility {
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

    /*Background of board*/
    public static Rectangle background() {
        Rectangle background = new Rectangle(HexMap.BASE_WIDTH, HexMap.BASE_HEIGHT);
        background.setArcWidth(25);
        background.setArcHeight(25);
        background.setFill(Color.DIMGREY);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
        background.setStrokeLineCap(StrokeLineCap.ROUND);
        background.setStrokeType(StrokeType.INSIDE);
        return background;
    }

    /*
    Draws a grid over the entire stage with variable width and height dimensions as well as
    variable step intervals.
    Made for debugging graphical symmetry/conformity for if any GUI elements are to be centered or
    adjusted to make space.
     */
//    public static void drawDebugGrid(Pane root, double width, double height, double step) {
//        for (double x = 0; x <= width; x += step) {
//            javafx.scene.shape.Line verticalLine = new javafx.scene.shape.Line(x, 0, x, height);
//            verticalLine.setStroke(Color.rgb(200, 0, 0, 0.2));
//            verticalLine.setStrokeWidth(1);
//            root.getChildren().add(verticalLine);
//
//            if (x % (step * 1) == 0) {
//                javafx.scene.text.Text label = new javafx.scene.text.Text("x = " + (int)x);
//                label.setX(x + 2);
//                label.setY(10);
//                label.setStyle("-fx-font-size: 9px;");
//                root.getChildren().add(label);
//            }
//        }
//
//        for (double y = 0; y <= height; y += step) {
//            javafx.scene.shape.Line horizontalLine = new javafx.scene.shape.Line(0, y, width, y);
//            horizontalLine.setStroke(Color.rgb(0, 0, 200, 0.2));
//            horizontalLine.setStrokeWidth(1);
//            root.getChildren().add(horizontalLine);
//
//            if (y % (step * 1) == 0) {
//                javafx.scene.text.Text label = new javafx.scene.text.Text("y = " + (int)y);
//                label.setX(2);
//                label.setY(y - 2);
//                label.setStyle("-fx-font-size: 9px;");
//                root.getChildren().add(label);
//            }
//        }
//    }
}
