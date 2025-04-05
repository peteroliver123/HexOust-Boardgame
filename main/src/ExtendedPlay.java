/*Imports*/
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

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
    private int totalTurn = 0;
    private Runnable resetCallback; // Reference to the reset function in HexMap

    public ExtendedPlay(Pane root) {
        /*Display the current round and the number of wins for each player*/
        winCount = new TextFlow(
                new Text("Round: "),
                turnCount = new Text("0"),
                new Text("            "),
                makeText("Red ", "0xc9180c"),
                redScore = new Text("0"),
                new Text(" | "),
                makeText("Blue ", "0x0c42c9"),
                blueScore = new Text("0")
        );
        winCount.setLayoutX(625);
        winCount.setLayoutY(50);
        winCount.setStyle("-fx-font-size: 40px; -fx-font-weight: bold");

        /*Replay button made in a stackpane such that the replay button text will be centered inside of the button*/
        replayButton = new StackPane();
        replayButton.setLayoutX(900);
        replayButton.setLayoutY(540);
        replayButton.setPrefSize(210, 70);
        button = new Rectangle(210, 70);
        button.setArcWidth(20);
        button.setArcHeight(20);
        button.setStroke(Color.BLACK);
        button.setVisible(false);

        replayButtonText = new Text("Replay!");
        replayButtonText.setX(935);
        replayButtonText.setY(565);
        replayButtonText.setStyle("-fx-font-size: 42px; -fx-font-weight: bold");
        replayButtonText.setFill(Color.valueOf("#c9a00c"));
        replayButtonText.setStroke(Color.BLACK);
        replayButtonText.setStrokeLineCap(StrokeLineCap.ROUND);
        replayButtonText.setVisible(false);

        replayButton.getChildren().addAll(button, replayButtonText);
        root.getChildren().addAll(winCount, replayButton);
        replayButton.setOnMouseClicked(this::replayHandle);

        /*Small animations for the replay button*/
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

    /*Helper function for producing some text with some hexadecimal colour*/
    private Text makeText(String content, String colorHex) {
        Text t = new Text(content);
        t.setFill(Color.web(colorHex));
        return t;
    }
}
