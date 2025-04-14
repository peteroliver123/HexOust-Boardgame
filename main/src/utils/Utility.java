package utils;
/*Imports */
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/*Utility Class  */
public class Utility {
    /**************
     CONSTANTS
     ***************/

    /*Hexagon Logic*/
    public static final double LENGTH = 30;//Length of side of each individual hexagon
    public static final int SIZE = 7; // Size of hexMap board
    public static Hexagon[][] hexagons = new Hexagon[2 * SIZE - 1][2 * SIZE - 1]; // 2D array of all the hexagons on the hexMap.
    public static int [][] state = new int[2 * SIZE - 1][2 * SIZE - 1];//2D array with state of play of all the hexagons on the hexMap
    // ^^^ 0 indicates invalid 1 indicates valid non capture 2 indicates valid capture

    /*Pane Sizing */
    public static final double BASE_WIDTH = 1280;
    public static final double BASE_HEIGHT = 720;

    /*GUI Elements */
    public static Circle playerTurnCircle;

    /*Text Settings */
    public static final Font DEFAULT_FONT = new Font("verdana", 30);
    public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;

    /*Game Management */
    public static boolean noValidMoves = false;

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

    /*Helper function for producing some text with some hexadecimal colour when we have no position*/
    public static Text makeTextWithHex(String content, String colorHex) {
        Text t = new Text(content);
        t.setFill(Color.web(colorHex));
        return t;
    }

    /*Background of board*/
    public static Rectangle background() {
        Rectangle background = new Rectangle(BASE_WIDTH, BASE_HEIGHT);
        background.setArcWidth(25);
        background.setArcHeight(25);
        background.setFill(Color.DIMGREY);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3);
        background.setStrokeLineCap(StrokeLineCap.ROUND);
        background.setStrokeType(StrokeType.INSIDE);
        return background;
    }

    /*Draw the current player colored circle beside the Make a Move Text */
    public static void drawPlayerTurnCircle(){
        playerTurnCircle = drawCircle(new Point (900, 500));
        HexMap.root.getChildren().add(playerTurnCircle);
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
