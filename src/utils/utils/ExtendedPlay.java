package utils.utils;

/*Imports*/
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import static utils.utils.Utility.*;

/**
This class handles and deals with any function or GUI elements in regard to
the end game and playing more than one round.
- Displays a score counter that counts the wins for each player
- Creates a replay button that will call the reset function and remove any end game exclusive elements
 */

public class ExtendedPlay {
    public Rectangle button;

    private StackPane replayButton;
    private Text replayButtonText;
    private final Text redScore;
    private final Text blueScore;
    private final Text turnCount;
    private int redWin = 0;
    private int blueWin = 0;
    private int totalTurn;
    private Runnable resetCallback; // Reference to the reset function in utils.HexMap

    public static ExtendedPlay extendedPlay;
    public static Text endGameText;

    private static boolean gameOver = false;

    public static boolean getGameOverStatus(){
        return gameOver;
    }

    public static void setGameOver(boolean newStatus){
        gameOver = newStatus;
    }

    public ExtendedPlay(Pane root) {
        totalTurn = 1;
        /*Display the current round and the number of wins for each player*/
        TextFlow winCount = new TextFlow(
                new Text("Round: "),
                turnCount = new Text("1"),
                new Text("            "),
                makeTextWithHex("Red ", "0xc9180c"),
                redScore = new Text("0"),
                new Text(" | "),
                makeTextWithHex("Blue ", "0x0c42c9"),
                blueScore = new Text("0")
        );
        winCount.setLayoutX(625);
        winCount.setLayoutY(50);
        winCount.setStyle("-fx-font-size: 40px; -fx-font-weight: bold");

        setUpReplayButton();

        replayButton.getChildren().addAll(button, replayButtonText);
        root.getChildren().addAll(winCount, replayButton);
        replayButton.setOnMouseClicked(this::replayHandle);

        setReplayButtonAnimations();

        replayButtonText.setOnMouseClicked(this::replayHandle);
    }

    public int getTurnCount(){
        return this.totalTurn;
    }

    public int getRedWin(){
        return redWin;
    }

    /*Display the splash screen for the end of a round*/
    public void endGameSplash (HexMap.PlayerTurn winner, Runnable resetCallback) {
        if (winner == HexMap.PlayerTurn.RED) redWin++;
        else if (winner == HexMap.PlayerTurn.BLUE) blueWin++;
        totalTurn++;
        updateWinCount();

        replayButton.setVisible(true);
        button.setVisible(true);
        replayButtonText.setVisible(true);
        this.resetCallback = resetCallback;
    }

    /*Remove the end game splash screen upon a new round*/
    public void clearEndGameSplash () {
        replayButton.setVisible(false);
        button.setVisible(false);
        replayButtonText.setVisible(false);
    }

    /*Start new round upon replay button being clicked*/
    private void replayHandle (MouseEvent event) {
        if (resetCallback != null) {
            clearEndGameSplash();
            resetCallback.run();
        }
    }

    private void updateWinCount () {
        redScore.setText(String.valueOf(redWin));
        blueScore.setText(String.valueOf(blueWin));
        turnCount.setText(String.valueOf(totalTurn));
    }

    /**
     * Resets game state, clearing placed stones and setting the board up for another round.
     */
    public static void reset(){
        /*Removal of end game splash screen elements*/
        if (endGameText != null && HexMap.root.getChildren().contains(endGameText)) {
            HexMap.root.getChildren().remove(endGameText);
            endGameText = null;
        }

        if (extendedPlay != null) {
            extendedPlay.clearEndGameSplash();
        }

        /*Remove Circles */
        for(Circle circle : HexMap.circles){
           HexMap.root.getChildren().remove(circle);
        }

        /*Redraw player turn circle*/
        if(HexMap.getCurrentPlayer() == HexMap.PlayerTurn.BLUE){
            HexMap.changePlayer();
        }
        drawPlayerTurnCircle(HexMap.root);

        /*Reset Variables */
        HexMap.getRedHexagons().clear();
        HexMap.getBlueHexagons().clear();
        gameOver = false;
        HexMap.setTurnCount(1);
        Possibilities.refreshBoardState();
    }


    /**
     * Helper function, sets simple hover animations for replay button
     */
    private void setReplayButtonAnimations(){
        replayButton.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), replayButton);
            scale.setToX(1.1);
            scale.setToY(1.1);

            TranslateTransition shake = new TranslateTransition(Duration.millis(80), replayButton);
            shake.setFromX(-3);
            shake.setToX(3);
            shake.setFromY(-2);
            shake.setToY(2);
            shake.setCycleCount(2);
            shake.setAutoReverse(true);

            scale.play();
            shake.play();
        });

        replayButton.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), replayButton);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            replayButton.setTranslateX(0);
        });
    }

    /**
     * Helper function, creates replay button and sets its style elements
     */
    private void setUpReplayButton(){
        /*Replay button made in a stack-pane such that the replay button text will be centered inside the button*/
        replayButton = new StackPane();
        replayButton.setLayoutX(900);
        replayButton.setLayoutY(540);
        replayButton.setPrefSize(210, 70);

        button = new Rectangle(210, 70);
        button.setArcWidth(20);
        button.setArcHeight(20);
        button.setStroke(Color.BLACK);
        button.setVisible(false);

        replayButtonText = makeText("Replay!", new Point (935, 565));
        replayButtonText.setStyle("-fx-font-size: 42px; -fx-font-weight: bold");
        replayButtonText.setFill(Color.valueOf("#c9a00c"));
        replayButtonText.setStroke(Color.BLACK);
        replayButtonText.setStrokeLineCap(StrokeLineCap.ROUND);
        replayButtonText.setVisible(false);
    }
}
