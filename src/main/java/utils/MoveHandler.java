package utils;

/* This class is to do with making moves and capturing */

/*Imports */
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import static utils.Utility.*;
import java.util.ArrayList;

/**
 * High level control of user click handling. Calls other helper classes in order to resolve user move.
 */
public class MoveHandler implements EventHandler<MouseEvent> {
    Pane root;
    Hexagon hexagon;
    public static Text invalidMoveText;

    public MoveHandler(Pane root, Hexagon hexagon) {
        this.root = root;
        this.hexagon = hexagon;
    }

    /**
     * Main handle method, calls auxiliary methods such as win-checking, or capture/non-capture move results.
     * @param event contains information about the MouseEvent that triggered the handle() method
     */
    @Override
    public void handle(MouseEvent event) {
        if (ExtendedPlay.getGameOverStatus()) {
            return;
        }

        /*Remove Invalid Move Text */
        if (invalidMoveText != null && HexMap.root.getChildren().contains(invalidMoveText)) {
            HexMap.root.getChildren().remove(invalidMoveText);
            invalidMoveText = null;
        }

        Point mousePoint = new Point(event.getX(), event.getY());
        if(invalidClick(mousePoint)){
            drawInvalidMoveText();
        } else {
            int stateX = (int) hexagon.getCoordinatePosition().getX();
            int stateY = (int) hexagon.getCoordinatePosition().getY();
            if (state[stateX][stateY] == 1) {//pre-checked to be non-capturing
                nonCapture();//draw new circle, switch turn
            } else if (state[stateX][stateY] == 2) {//pre-checked to be capturing
                @SuppressWarnings("unchecked")
                ArrayList<Hexagon>[] enemySubGroupsHolder = (ArrayList<Hexagon>[]) new ArrayList[100];
                Possibilities.isCapturing(new Point (stateX, stateY), enemySubGroupsHolder);//generate enemy subgroups
                capture(enemySubGroupsHolder);//remove enemy subgroups from board and draw new circle
                checkWin();//make sure capture didn't end game
            } else {//pre-checked to be invalid
                drawInvalidMoveText();
            }
        }
        Possibilities.refreshBoardState();//refresh state array before new click with updated board state.
    }

    public void drawInvalidMoveText(){
        invalidMoveText = makeText("Invalid Move!", new Point(720, 310));
        HexMap.root.getChildren().add(invalidMoveText);
    }

    public void addCircleToBoard() {
        Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
        root.getChildren().add(circle);
        HexMap.circles.add(circle);
        if (HexMap.getCurrentPlayer() == HexMap.PlayerTurn.BLUE) {
            HexMap.getBlueHexagons().add(hexagon);
        } else {
            HexMap.getRedHexagons().add(hexagon);
        }
    }

    public void nonCapture(){
        addCircleToBoard();
        HexMap.changePlayer();
    }

    /**
     * resolves a capture move. Does not determine if the move was originally a capture, just resolves the result.
     * Places down the stone being played and removes the captured stones.
     * @param enemySubGroupsHolder holds all distinct, non-connected groups of enemy stones
     */
    public void capture(ArrayList<Hexagon>[] enemySubGroupsHolder){
        addCircleToBoard();
        int i = 0;
        ArrayList<Point> capturedCenters = new ArrayList<>();

        while(enemySubGroupsHolder[i] != null){
            ArrayList<Hexagon> d = enemySubGroupsHolder[i];
            addCenters(capturedCenters, d);
            for (Point centre : capturedCenters) {
                for(Circle circle : HexMap.circles){
                    if((int)circle.getCenterX() == (int) centre.getX() && (int)circle.getCenterY() == (int) centre.getY()){
                        root.getChildren().remove(circle);
                    }
                }
            }
            i++;
        }
    }

    private void addCenters(ArrayList<Point> centersArray, ArrayList<Hexagon> hexes){
        for(Hexagon hex : hexes){
            removeHexFromMap(hex);
            centersArray.add(hex.getCentre());
        }
    }

    private void removeHexFromMap(Hexagon hex){
        if (HexMap.getCurrentPlayer() == HexMap.PlayerTurn.BLUE) {
            HexMap.getRedHexagons().remove(hex);
        } else {
            HexMap.getBlueHexagons().remove(hex);
        }
    }

    private boolean invalidClick(Point mousePoint){
        /*PRE-CHECKS */
        if (!hexagon.contains(mousePoint)) {
            Possibilities.refreshBoardState();
            return true;
        }
        return false;
    }

    private void checkWin(){
        ArrayList<Hexagon> enemyHexagons = HexMap.getEnemyHexagons();
        if (enemyHexagons.isEmpty()) {
            ExtendedPlay.endGameText = makeText("Game Over! " + HexMap.getCurrentPlayer() + " won in " + HexMap.getTurnCount() + " turns!", new Point(720, 310));
            root.getChildren().add(ExtendedPlay.endGameText);
            ExtendedPlay.setGameOver(true);
            renderVictoryGraphics();
        }
    }

    private void renderVictoryGraphics(){
         /*Call end game splash screen and set replay button to color of winner (For fun and to also demonstrate
            the certain graphical functions available to us as well as how they are called)
            */
        String winnerColor = (HexMap.getCurrentPlayer() == HexMap.PlayerTurn.BLUE) ? "0x0c42c9" : "0xc9180c";
        ExtendedPlay.extendedPlay.endGameSplash(HexMap.getCurrentPlayer(), ExtendedPlay::reset);
        ExtendedPlay.extendedPlay.button.setFill(LinearGradient.valueOf
                ("from 0px 0px to 10px 20px, " +
                        "reflect, " + winnerColor + " 0.0%, 0x000000 100.0%")
        );
    }

}

