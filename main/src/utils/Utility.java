package utils;
/*Imports */
import javafx.scene.layout.Pane;
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
    Draws Circle of current player color on target position
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


    //Draws text at given point
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

    public static void drawPlayerTurnCircle(Pane root){
        playerTurnCircle = drawCircle(new Point (900, 500));
        root.getChildren().add(playerTurnCircle);
        HexMap.circles.add(playerTurnCircle);
    }
}
