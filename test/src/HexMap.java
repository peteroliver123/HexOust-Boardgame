import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class HexMap extends Application {
    private static final int SIZE = 7; // Size of hexmap board (Adjustable board size (Possible extra additions later))
    private static final double CENTRE_X = 100;
    private static final double CENTRE_Y = 520; // Center coordinates of the first initial hexagon (top left, [0][0])
    private static final double LENGTH = 30; // Size of hexagon (Distance from center to any vertice)
    private static final double PADDING = 150;
    private Polygon[][] hexagons = new Polygon[2 * SIZE - 1][2 * SIZE - 1]; // 2D array of all the hexagons on the hexmap, based on size of board.

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane(); // Initialize the field/map
        double x = CENTRE_X;
        double y = CENTRE_Y; // Variables assigned constants.
        double tempY = y; // Temporary y variable for generating hexagons at a perfect ideal spacing vertically.
        int column = SIZE; // Amount of hexagons in each column. Incr/Decr depending on ascFlag.
        boolean ascFlag = true; // Ascension flag. Turns false after generating the center column of hexagon.

        for (int q = 0; q < ((SIZE * 2) - 1); q++) { // Loop for all columns
            if (!(q >= SIZE-1)) { // Loop for generating columns that ascend (towards center column)
                for (int i = 0; i < column; i++) {
                    Polygon hex = createHexagon(x, tempY); // Create new hexagon using center point.
                    System.out.println("q: " + q + ", i: " + i);
                    hexagons[q][i] = hex;
                    root.getChildren().add(hex); // Add hexagon to scene/map.
                    tempY -= ((Math.sqrt(0.75) * LENGTH) * 2); // Move down y coordinates by distance of 2 'h' (Pythagoras yummy!!)
                }
            } else {
                ascFlag = false; // Flag set to false, loop to generate descending columns.
                for (int i = 0; i < column; i++) {
                    Polygon hex = createHexagon(x, tempY);
                    System.out.println("q: " + q + ", i: " + i);
                    hexagons[q][i] = hex;
                    root.getChildren().add(hex);
                    tempY -= ((Math.sqrt(0.75) * LENGTH) * 2);
                }
            }

            if (ascFlag) { // Increase y coordinate of next initial hexagon for next column
                y += (Math.sqrt(0.75) * LENGTH);
                column++;
            } else { // Decrease y coordinate of next initial hexagon for next column
                y -= (Math.sqrt(0.75) * LENGTH);
                column--;
            }
            x += (LENGTH * 1.5); // X coordinate of next column
            tempY = y; // Reset tempY to new y
        }

        Scene scene = new Scene(root, 1280, 720); // Initialize new scene, taking in field/map and size parameters.
        primaryStage.setScene(scene); // Set scene to stage.
        primaryStage.setTitle("HexMap"); // Title of stage.
        primaryStage.show(); // Render stage.
    }

    private Polygon createHexagon(double centerX, double centerY) {
        Polygon hex = new Polygon(); // Create a new polygon.
        hex.getPoints().addAll(new Double[]{ // Add coordinates of vertices (in Double form), where vertices are ordered circumferentially.
                centerX - LENGTH, centerY,
                centerX - (LENGTH * 0.5), centerY + (Math.sqrt(0.75) * LENGTH),
                centerX + (LENGTH * 0.5), centerY + (Math.sqrt(0.75) * LENGTH),
                centerX + LENGTH, centerY,
                centerX + (LENGTH * 0.5), centerY - (Math.sqrt(0.75) * LENGTH),
                centerX - (LENGTH * 0.5), centerY - (Math.sqrt(0.75) * LENGTH),
        });
        hex.setStroke(Color.BLACK);
        hex.setFill(Color.web("#DEE6E8"));
        return hex;
    }

    public static void main(String[] args) {
        launch(args);
    }
}