package utils;

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
import static utils.Utility.*;

/*
This class handles and deals with any function or GUI elements in regard to
the end game.
- Displays a score counter that counts the wins for each player
- Creates a replay button that will call the reset function and remove any end game exclusive elements
 */

public class ExtendedPlay {
    public TextFlow winCount;
    public Rectangle button;
    public StackPane replayButton;
    public Text replayButtonText, redScore, blueScore, turnCount;
    private int redWin = 0;
    private int blueWin = 0;
    private int totalTurn = 1;
    private Runnable resetCallback; // Reference to the reset function in utils.HexMap

    public static ExtendedPlay extendedPlay;
    public static Text endGameText;

    public ExtendedPlay(Pane root) {
        /*Display the current round and the number of wins for each player*/
        winCount = new TextFlow(
                new Text("Round: "),
                turnCount = new Text("1"),
                new Text("            "),
                makeTextWithHex("Red ", "0xc9180c"),
                redScore = new Text("0"),
                new Text(" | "),
                Utility.makeTextWithHex("Blue ", "0x0c42c9"),
                blueScore = new Text("0")
        );
        winCount.setLayoutX(625);
        winCount.setLayoutY(50);
        winCount.setStyle("-fx-font-size: 40px; -fx-font-weight: bold");

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

        replayButtonText = Utility.makeText("Replay!", new Point (935, 565));
        replayButtonText.setStyle("-fx-font-size: 42px; -fx-font-weight: bold");
        replayButtonText.setFill(Color.valueOf("#c9a00c"));
        replayButtonText.setStroke(Color.BLACK);
        replayButtonText.setStrokeLineCap(StrokeLineCap.ROUND);
        replayButtonText.setVisible(false);

        replayButton.getChildren().addAll(button, replayButtonText);
        root.getChildren().addAll(winCount, replayButton);
        replayButton.setOnMouseClicked(this::replayHandle);

        /*Small animations for the replay button*/
        replayButton.setOnMouseEntered(_ -> {
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

        replayButton.setOnMouseExited(_ -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), replayButton);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            replayButton.setTranslateX(0);
        });
        replayButtonText.setOnMouseClicked(this::replayHandle);
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
    public void newGameSplash () {
        replayButton.setVisible(false);
        button.setVisible(false);
        replayButtonText.setVisible(false);
    }

    /*Start new round upon replay button being clicked*/
    private void replayHandle (MouseEvent event) {
        if (resetCallback != null) {
            newGameSplash();
            resetCallback.run();
        }
    }

    private void updateWinCount () {
        redScore.setText(String.valueOf(redWin));
        blueScore.setText(String.valueOf(blueWin));
        turnCount.setText(String.valueOf(totalTurn));
    }

    //resets game state
    public static void reset(){
        /*Removal of end game splash screen elements*/
        if (endGameText != null && HexMap.root.getChildren().contains(endGameText)) {
            HexMap.root.getChildren().remove(endGameText);
            endGameText = null;
        }

        if (extendedPlay != null) {
            extendedPlay.newGameSplash();
        }

        /*Remove Circles */
        for(int i = 0; i < HexMap.root.getChildren().size(); i++){
            if(HexMap.root.getChildren().get(i).getClass() == Circle.class){
                HexMap.root.getChildren().remove(i);
                i--;
            }
        }

        /*Redraw player turn circle*/
        HexMap.currentPlayer = HexMap.PlayerTurn.RED;
        Utility.drawPlayerTurnCircle();

        /*Reset Variables */
        HexMap.blueCircles.clear();
        HexMap.redCircles.clear();
        HexMap.gameOver = false;
        HexMap.turnCount = 1;
    }
}
