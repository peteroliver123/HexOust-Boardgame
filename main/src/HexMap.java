import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.ArrayList;


public class HexMap extends Application {
    private static final int SIZE = 7; // Size of hexmap board (Adjustable board size (Possible extra additions later))
    private static final double CENTRE_X = 100;
    private static final double CENTRE_Y = 520; // Center coordinates of the first initial hexagon (bottom left, [0][0])
    private static final double LENGTH = 30; // Size of hexagon (Distance from center to any vertice)
    private static final double PADDING = 150;
    public static Hexagon[][] hexagons = new Hexagon[2 * SIZE - 1][2 * SIZE - 1]; // 2D array of all the hexagons on the hexmap, based on size of board.
    public static Circle playerTurnCircle;
    public static ArrayList<Hexagon> redCircles = new ArrayList<Hexagon>();
    public static ArrayList<Hexagon> blueCircles = new ArrayList<Hexagon>();

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
        int column = SIZE; // Amount of hexagons in each column. Incr/Decr depending on ascFlag.
        boolean ascFlag = true; // Ascension flag. Turns false after generating the center column of hexagon.

        for (int q = 0; q < ((SIZE * 2) - 1); q++) { // Loop for all columns
            if (!(q >= SIZE-1)) { // Loop for generating columns that ascend (towards center column)
                for (int i = 0; i < column; i++) {
                    Hexagon hex = createHexagon(x, tempY, q, i, root); // Create new hexagon using center point.
                    // System.out.println("q: " + q + ", i: " + i);
                    hexagons[q][i] = hex;
                    root.getChildren().add(hex); // Add hexagon to scene/map.
                    tempY -= ((Math.sqrt(0.75) * LENGTH) * 2); // Move down y coordinates by distance of 2 'h' (Pythagoras yummy!!)
                }
            } else {
                ascFlag = false; // Flag set to false, loop to generate descending columns.
                for (int i = 0; i < column; i++) {
                    Hexagon hex = createHexagon(x, tempY, q, i, root);
                    // System.out.println("q: " + q + ", i: " + i);
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

        // Circle piece corresponding to current player's turn.
        playerTurnCircle = utility.drawCircle(900, 500);
        root.getChildren().add(playerTurnCircle);

        /*Display Text*/
        Text text = makeMoveText();
        root.getChildren().add(text);

        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = initialize();
        Scene scene = new Scene(root, 1280, 720); // Initialize new scene, taking in field/map and size parameters.
        primaryStage.setScene(scene); // Set scene to stage.
        primaryStage.setTitle("HexMap"); // Title of stage.
        scene.setOnKeyPressed(new keyPressHandler()); //quit button
        primaryStage.show(); // Render stage.
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

    private class keyPressHandler implements EventHandler<KeyEvent>{
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
                        hex.getX(), hex.getY(), 0, 0, MouseButton.PRIMARY, 1,
                        false, false, false, false, true,
                        false, false, false, false, false, null);
                clickHandler.handle(click);
            }
        }
    }

    private Hexagon createHexagon(double centerX, double centerY, int q, int r, Pane root) {
        Hexagon hex = new Hexagon(centerX, centerY, q, r); // Create a new polygon.
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

        utility.drawCircle(hex.getX(), hex.getY());
       // EventHandler<MouseEvent> Hover = new MouseHoverHandler(root, hex, hoverCircle);
     //   hex.setOnMouseEntered(Hover);
     //   hex.setOnMouseExited(Hover);
        hex.setOnMouseClicked(new MouseClickHandler(root, hex));
        return hex;
    }

    public Text makeMoveText(){
        Text text = new Text("To Make a Move");
        text.setX(950); // X position on screen
        text.setY(510); // Y position on screen
        text.setFont(javafx.scene.text.Font.font("Verdana", 30));
        text.setFill(javafx.scene.paint.Color.BLACK);
        return text;
    }


  /*  public Circle drawHoverCircle(double x, double y){
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(LENGTH / 1.5);
        circle.setMouseTransparent(true); // Allows mouse events to pass through.
        return circle;
    }*/

    //resets game state
    public void reset(){
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
        playerTurnCircle = utility.drawCircle(900, 500);
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