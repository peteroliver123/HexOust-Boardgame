/*Imports */
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.ArrayList;


public class HexMap extends Application {
    private static final int SIZE = 7; // Size of hexMap board (Adjustable board size (Possible extra additions later))
    private static final double CENTRE_X = 100;
    private static final double CENTRE_Y = 515.88; // Center coordinates of the first initial hexagon (bottom left, [0][0])
    public static final double BASE_WIDTH = 1280;
    public static final double BASE_HEIGHT = 720;
    public static Hexagon[][] hexagons = new Hexagon[2 * SIZE - 1][2 * SIZE - 1]; // 2D array of all the hexagons on the hexMap, based on size of board.
    public static Circle playerTurnCircle;
    public static ArrayList<Hexagon> redCircles = new ArrayList<>();
    public static ArrayList<Hexagon> blueCircles = new ArrayList<>();
    public static ExtendedPlay extendedPlay;
    public static Text endGameText;

    public static ArrayList<Hexagon> getBlueCircles() {
        return blueCircles;
    }

    public static ArrayList<Hexagon> getRedCircles(){
        return redCircles;
    }

    public enum PlayerTurn {
        RED,
        BLUE;

        // Helper method to switch turns
        public PlayerTurn next() {
            return this == RED ? BLUE : RED;
        }
    }

    public static PlayerTurn currentPlayer = PlayerTurn.RED;
    public static PlayerTurn startingPlayer = PlayerTurn.RED;
    public static boolean gameOver = false;
    public static int turnCount = 1; //starts at one because it only increments when player is changed
    public static Pane root;

    public Pane initialize() {
        root = new Pane(); // Initialize the field/map
        double x = CENTRE_X;
        double y = CENTRE_Y; // Variables assigned constants.
        double tempY = y; // Temporary y variable for generating hexagons at a perfect ideal spacing vertically.
        int column = SIZE; // Amount of hexagons in each column. Increment/Decrement depending on ascFlag.
        boolean ascFlag = true; // Ascension flag. Turns false after generating the center column of hexagon.

        for (int q = 0; q < ((SIZE * 2) - 1); q++) { // Loop for all columns
            if (!(q >= SIZE-1)) { // Loop for generating columns that ascend (towards center column)
                for (int i = 0; i < column; i++) {
                    Hexagon hex = createHexagon(x, tempY, q, i, root); // Create new hexagon using center point.
                    // System.out.println("q: " + q + ", i: " + i);
                    hexagons[q][i] = hex;
                    root.getChildren().add(hex); // Add hexagon to scene/map.
                    tempY -= ((Math.sqrt(0.75) * Utility.LENGTH) * 2); // Move down y coordinates by distance of 2 'h' (Pythagoras yummy!!)
                }
            } else {
                ascFlag = false; // Flag set to false, loop to generate descending columns.
                for (int i = 0; i < column; i++) {
                    Hexagon hex = createHexagon(x, tempY, q, i, root);
                    // System.out.println("q: " + q + ", i: " + i);
                    hexagons[q][i] = hex;
                    root.getChildren().add(hex);
                    tempY -= ((Math.sqrt(0.75) * Utility.LENGTH) * 2);
                }
            }

            if (ascFlag) { // Increase y coordinate of next initial hexagon for next column
                y += (Math.sqrt(0.75) * Utility.LENGTH);
                column++;
            } else { // Decrease y coordinate of next initial hexagon for next column
                y -= (Math.sqrt(0.75) * Utility.LENGTH);
                column--;
            }
            x += (Utility.LENGTH * 1.5); // X coordinate of next column
            tempY = y; // Reset tempY to new y
        }

        // Circle piece corresponding to current player's turn.
        playerTurnCircle = Utility.drawCircle(new Point (900, 500));
        root.getChildren().add(playerTurnCircle);

        /*Display Text*/
        Text text = Utility.makeText("To Make a Move", new Point (950, 510));
        root.getChildren().add(text);

        /*Prime the end game splash screen*/
        extendedPlay = new ExtendedPlay(root);

        //Utility.drawDebugGrid(root, BASE_WIDTH, BASE_HEIGHT, 50);
        root.getChildren().addFirst(Utility.background());
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = initialize();
        LetterBox letterBox = new LetterBox();
        Scene scene = letterBox.scene(root, BASE_WIDTH, BASE_HEIGHT); // Initialize new scene and letterboxing the contents (Scaling + Center)


        primaryStage.setScene(scene); // Set scene to stage.
        primaryStage.setTitle("HexMap"); // Title of stage.
        scene.setOnKeyPressed(new keyPressHandler()); //quit button
        primaryStage.show(); // Render stage.

        /*Set a minimum window size for the board*/
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }



  /*  private class MouseHoverHandler implements EventHandler<MouseEvent> {
        Pane root;
        Hexagon hexagon;
        Circle hoverCircle;

        public MouseHoverHandler(Pane root, Hexagon hexagon, Circle hoverCircle) {
            this.root = root;
            this.hexagon = hexagon;
            this.hoverCircle = hoverCircle;
        }

        @Override
        public void handle(MouseEvent event) {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Function to make the area of each hexagon interactable.
            if (hexagon.contains(mouseX, mouseY)) {
                boolean hasCircle = false;

                // Prevents re-interaction if mouse event is registered inside the hexagonal area not occupied by the circle
                for (javafx.scene.Node node : root.getChildren()) {
                    if (node instanceof Circle) {
                        Circle circle = (Circle) node;
                        if (circle.getCenterX() == hexagon.getX() && circle.getCenterY() == hexagon.getY()) {
                            hasCircle = true;
                            break;
                        }
                    }
                }

                if (!hasCircle) {
                    int q = hexagon.getQ();
                    int r = hexagon.getR();

                    if (true) {
                        if (currentPlayer == PlayerTurn.BLUE) {
                            hoverCircle.setFill(Color.rgb(0, 0, 255, 0.3));
                        } else {
                            hoverCircle.setFill(Color.rgb(255, 0, 0, 0.3));
                        }
                        hoverCircle.setStroke(Color.rgb(0, 0, 0, 0.3));
                        hoverCircle.setStrokeWidth(2);
                        root.getChildren().add(hoverCircle);
                    }
                }
            } else {
                hoverCircle.setFill(Color.rgb(0, 0, 0, 0));
                hoverCircle.setStroke(Color.rgb(0, 0, 0, 0));
                root.getChildren().remove(hoverCircle);
            }
        }
    }*/

    private static class keyPressHandler implements EventHandler<KeyEvent>{
        @Override
        public void handle(KeyEvent keyEvent){
            if(keyEvent.getText().equals("q")){
                System.exit(1);
            }
            if(keyEvent.getText().equals("r")){
                reset();
            }
            if(keyEvent.getText().equals("t")){
                Hexagon hex = HexMap.hexagons[1][2];
                MouseClickHandler clickHandler = new MouseClickHandler(root, hex);
                MouseEvent click = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                        hex.getCentre().getX(), hex.getCentre().getY(), 0, 0, MouseButton.PRIMARY, 1,
                        false, false, false, false, true,
                        false, false, false, false, false, null);
                clickHandler.handle(click);
            }
        }
    }

    private Hexagon createHexagon(double centerX, double centerY, int q, int r, Pane root) {
        Hexagon hex = new Hexagon(new Point(centerX, centerY), new Point (q, r)); // Create a new polygon.
        hex.getPoints().addAll(// Add coordinates of vertices (in Double form), where vertices are ordered circumferentially.
                centerX - Utility.LENGTH, centerY,
                centerX - (Utility.LENGTH * 0.5), centerY + (Math.sqrt(0.75) * Utility.LENGTH),
                centerX + (Utility.LENGTH * 0.5), centerY + (Math.sqrt(0.75) * Utility.LENGTH),
                centerX + Utility.LENGTH, centerY,
                centerX + (Utility.LENGTH * 0.5), centerY - (Math.sqrt(0.75) * Utility.LENGTH),
                centerX - (Utility.LENGTH * 0.5), centerY - (Math.sqrt(0.75) * Utility.LENGTH));
        hex.setStroke(Color.BLACK);
        hex.setFill(Color.web("#DEE6E8"));

        Utility.drawCircle(new Point (hex.getCentre().getX(), hex.getCentre().getY()));
       // EventHandler<MouseEvent> Hover = new MouseHoverHandler(root, hex, hoverCircle);
     //   hex.setOnMouseEntered(Hover);
     //   hex.setOnMouseExited(Hover);
        hex.setOnMouseClicked(new MouseClickHandler(root, hex));
        return hex;
    }


  /*  public Circle drawHoverCircle(double x, double y){
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(utility.LENGTH / 1.5);
        circle.setMouseTransparent(true); // Allows mouse events to pass through.
        return circle;
    }*/

    //resets game state
    public static void reset(){
        /*Removal of end game splash screen elements*/
        if (endGameText != null && root.getChildren().contains(endGameText)) {
            root.getChildren().remove(endGameText);
            endGameText = null;
        }

        if (extendedPlay != null) {
            extendedPlay.newGameSplash();
        }

        for(int i = 0; i < root.getChildren().size(); i++){
            if(root.getChildren().get(i).getClass() == Circle.class){
                root.getChildren().remove(i);
                i--;
            }
        }
        //makes game start with alternating players
        startingPlayer = PlayerTurn.RED;
        currentPlayer = startingPlayer;
        //redraw player turn circle
        playerTurnCircle = Utility.drawCircle(new Point (900, 500));
        root.getChildren().add(playerTurnCircle);

        blueCircles.clear();
        redCircles.clear();
        gameOver = false;

        turnCount = 1;
    }

    public static void main(String[] args) {
        launch(args);
    }
}