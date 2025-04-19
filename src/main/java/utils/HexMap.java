package utils;
/*Imports */
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.ArrayList;
import static utils.Utility.*;

//board class, controls the state of the actual board and the pieces on it.
public class HexMap extends Application {
    private static final double CENTRE_X = 100;
    private static final double CENTRE_Y = 515.88; // Center coordinates of the first initial hexagon (bottom left, [0][0])
    private static final ArrayList<Hexagon> redHexagons = new ArrayList<>();
    private static final ArrayList<Hexagon> blueHexagons = new ArrayList<>();
    private static PlayerTurn currentPlayer = PlayerTurn.RED;
    private static int turnCount = 1; //starts at one because it only increments when player is changed

    public static ArrayList<Circle> circles = new ArrayList<>();
    public static Pane root;

    public enum PlayerTurn {
        RED,
        BLUE;

        // Helper method to switch turns
        public PlayerTurn next() {
            return this == RED ? BLUE : RED;
        }
    }

    public static int getTurnCount(){return turnCount;}
    public static void setTurnCount(int newCount){turnCount = newCount;}
    public static PlayerTurn getCurrentPlayer(){return currentPlayer;}
    public static ArrayList<Hexagon> getBlueHexagons() {
        return blueHexagons;
    }
    public static ArrayList<Hexagon> getRedHexagons(){
        return redHexagons;
    }
    public static ArrayList<Hexagon> getPlayerHexagons(){return (currentPlayer == PlayerTurn.RED) ? redHexagons : blueHexagons;}
    public static ArrayList<Hexagon> getEnemyHexagons(){return (currentPlayer == PlayerTurn.RED) ? blueHexagons : redHexagons;}

    public Pane initialize() {
        root = new Pane(); // Initialize the field/map
        double x = CENTRE_X;
        double y = CENTRE_Y;
        double tempY = y; // Temporary y variable for generating hexagons at a perfect ideal spacing vertically.
        int column = SIZE; // Amount of hexagons in each column. Increment/Decrement depending on ascFlag.
        boolean ascFlag = true; // Ascension flag. Turns false after generating the center column of hexagon.

        for (int q = 0; q < ((SIZE * 2) - 1); q++) { // Loop for all columns
            makeColumn(column, q, new Point(x, tempY));
            if (q >= SIZE-1) {
                ascFlag = false;
            }
            if (ascFlag) { // Increase y coordinate of next initial hexagon for next column
                y += (Math.sqrt(0.75) * LENGTH);
                column++;
            } else { // Decrease y coordinate of next initial hexagon for next column
                y -= (Math.sqrt(0.75) * LENGTH);
                column--;
            }
            x += (LENGTH * 1.5); // X coordinate of next column
            tempY = y;
        }

        drawPlayerTurnCircle(root);

        Text text = makeText("To Make a Move", new Point(950, 510));
        root.getChildren().add(text);

        /*Prime the end game splash screen*/
        ExtendedPlay.extendedPlay = new ExtendedPlay(root);

        root.getChildren().addFirst(background());
        Possibilities.getBoardState();
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = initialize();
        LetterBox letterBox = new LetterBox();
        Scene scene = letterBox.scene(root, BASE_WIDTH, BASE_HEIGHT); // Initialize new scene and letterboxing the contents (Scaling + Center)

        primaryStage.setScene(scene);
        primaryStage.setTitle("HexOust");
        scene.setOnKeyPressed(new keyPressHandler()); //quit button
        primaryStage.show(); // Render stage.

        /*Set a minimum window size for the board*/
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    private static class keyPressHandler implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent keyEvent){
            if(keyEvent.getText().equals("q")){
                System.exit(1);
            }
            if(keyEvent.getText().equals("r")){
                ExtendedPlay.reset();
            }
        }
    }

    private Hexagon createHexagon(Point center, int q, int r, Pane root) {
        Hexagon hex = new Hexagon(new Point(center.getX(), center.getY()), new Point(q, r)); // Create a new polygon.
        hex.getPoints().addAll(// Add coordinates of vertices (in Double form), where vertices are ordered circumferentially.
                center.getX() - LENGTH, center.getY(),
                center.getX() - (LENGTH * 0.5), center.getY() + (Math.sqrt(0.75) * LENGTH),
                center.getX() + (LENGTH * 0.5), center.getY() + (Math.sqrt(0.75) * LENGTH),
                center.getX() + LENGTH, center.getY(),
                center.getX() + (LENGTH * 0.5), center.getY() - (Math.sqrt(0.75) * LENGTH),
                center.getX() - (LENGTH * 0.5), center.getY() - (Math.sqrt(0.75) * LENGTH));
        hex.setStroke(Color.BLACK);
        hex.setFill(Color.web("#DEE6E8"));

        circles.add(drawCircle(new Point(hex.getCentre().getX(), hex.getCentre().getY())));
        hex.setOnMouseClicked(new MoveHandler(root, hex));
        return hex;
    }

    private void makeColumn(int column, int q, Point center){
        for (int i = 0; i < column; i++) {
            Hexagon hex = createHexagon(center, q, i, root); // Create new hexagon using center point.
            hexagons[q][i] = hex;
            root.getChildren().add(hex);
            center.setY(center.getY() - ((Math.sqrt(0.75) * LENGTH) * 2)); // Move down y coordinates by distance of 2 'h'
        }
    }

    /*Background of board*/
    private static Rectangle background() {
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

    public static void changePlayer() {
        currentPlayer = currentPlayer.next();

        if (currentPlayer == PlayerTurn.BLUE) {
            playerTurnCircle.setFill(Color.BLUE);
        } else {
            playerTurnCircle.setFill(Color.RED);
        }
        turnCount++;
    }

    /*Application built with utils.HexMap as starter */
    public static void main(String[] args) {
        launch(args);
    }
}