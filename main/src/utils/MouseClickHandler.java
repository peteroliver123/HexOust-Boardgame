package utils;
/*Imports */
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import static utils.Utility.*;

public class MouseClickHandler implements EventHandler<MouseEvent> {
    Pane root;
    Hexagon hexagon;


    public MouseClickHandler(Pane root, Hexagon hexagon) {
        this.root = root;
        this.hexagon = hexagon;
    }

    @Override
    public void handle(MouseEvent event) {
        if(noValidMoves){
            System.out.println("No moves availale for " + HexMap.currentPlayer);
            changePlayer();
        }

        /*Remove Invalid Move Text */
        if (ExtendedPlay.invalidMoveText != null && HexMap.root.getChildren().contains(ExtendedPlay.invalidMoveText)) {
            HexMap.root.getChildren().remove(ExtendedPlay.invalidMoveText);
            ExtendedPlay.invalidMoveText = null;
        }

        Point mousePoint = new Point(event.getX(), event.getY());
        if(invalidClick(mousePoint)){
            return;
        }

        if (state[(int) hexagon.getCoordinatePosition().getX()][(int) hexagon.getCoordinatePosition().getY()] == 1) {
            nonCapture();
        } else if (state[(int) hexagon.getCoordinatePosition().getX()][(int) hexagon.getCoordinatePosition().getY()] == 2) {
            ArrayList[] arrayOfArrayLists = new ArrayList[100];
            Possibilities.isCapturing((int) hexagon.getCoordinatePosition().getX(), (int) hexagon.getCoordinatePosition().getY(), arrayOfArrayLists);
            capture(arrayOfArrayLists);
            checkWin();

        } else {
            ExtendedPlay.invalidMoveText = makeText("Invalid Move!", new Point(720, 310));
            HexMap.root.getChildren().add(ExtendedPlay.invalidMoveText);
        }
        Possibilities.getBoardState();
    }

    public void changePlayer() {
        HexMap.currentPlayer = HexMap.currentPlayer.next();

        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            playerTurnCircle.setFill(Color.BLUE);
        } else {
            playerTurnCircle.setFill(Color.RED);
        }
        HexMap.turnCount++;
    }

    public void addCircleToBoard() {
        Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
        root.getChildren().add(circle);
        HexMap.circles.add(circle);
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            HexMap.blueHexagons.add(hexagon);
        } else {
            HexMap.redHexagons.add(hexagon);
        }
    }

    public void nonCapture(){
        addCircleToBoard();
        changePlayer();
    }

    public void capture(ArrayList[] a){
        addCircleToBoard();
        int i = 0;
        ArrayList<Point> capturedCenters = new ArrayList<>();

        while(a[i] != null){
            ArrayList<Hexagon> d = a[i];
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
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            HexMap.redHexagons.remove(hex);
        } else {
            HexMap.blueHexagons.remove(hex);
        }
    }

    private boolean invalidClick(Point mousePoint){
        /*PRE-CHECKS */
        if (!hexagon.contains(mousePoint)) {
            Possibilities.getBoardState();
            return true;
        }
        if (HexMap.gameOver) {
            System.out.println("Game is over, please start a new one to keep playing!");
            Possibilities.getBoardState();
            return true;
        }
        return false;
    }

    private void checkWin(){
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.blueHexagons : HexMap.redHexagons;
        if (enemyHexagons.isEmpty()) {
            ExtendedPlay.endGameText = makeText("Game Over! " + HexMap.currentPlayer + " won in " + HexMap.turnCount + " turns!", new Point(720, 310));
            root.getChildren().add(ExtendedPlay.endGameText);
            HexMap.gameOver = true;

            /*Call end game splash screen and set replay button to color of winner (For fun and to also demonstrate
            the certain graphical functions available to us as well as how they are called)
            */
            String winnerColor = (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) ? "0x0c42c9" : "0xc9180c";
            ExtendedPlay.extendedPlay.endGameSplash(HexMap.currentPlayer, ExtendedPlay::reset);
            ExtendedPlay.extendedPlay.button.setFill(LinearGradient.valueOf
                    ("from 0px 0px to 10px 20px, " +
                            "reflect, " + winnerColor + " 0.0%, 0x000000 100.0%")
            );
        }
    }

}
