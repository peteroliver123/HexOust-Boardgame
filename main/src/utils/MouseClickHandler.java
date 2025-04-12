package utils;
/*Imports */
import javafx.event.EventHandler;
import javafx.scene.Node;
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
        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.redCircles : HexMap.blueCircles;
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.blueCircles : HexMap.redCircles;

        /*Remove Invalid Move Text */
        if (ExtendedPlay.invalidMoveText != null && HexMap.root.getChildren().contains(ExtendedPlay.invalidMoveText)) {
            HexMap.root.getChildren().remove(ExtendedPlay.invalidMoveText);
            ExtendedPlay.invalidMoveText = null;
        }

        Point mousePoint = new Point(event.getX(), event.getY());
        /*PRE-CHECKS */
        if(!hexagon.contains(mousePoint)){
            Possibilities.getBoardState();
            return;
        }
        if(HexMap.gameOver){
            System.out.println("Game is over, please start a new one to keep playing!");
            Possibilities.getBoardState();
            return;
        }

        if(Possibilities.state[(int) hexagon.getCoordinatePosition().getX()][(int) hexagon.getCoordinatePosition().getY()] == 1){
            nonCapture();
        } else if (Possibilities.state[(int) hexagon.getCoordinatePosition().getX()][(int) hexagon.getCoordinatePosition().getY()] == 2){
            ArrayList[] arrayOfArrayLists = new ArrayList[100];
            Possibilities.isCapturing((int) hexagon.getCoordinatePosition().getX(), (int) hexagon.getCoordinatePosition().getY(), 1, arrayOfArrayLists);

            Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
            root.getChildren().add(circle);
            if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
                HexMap.blueCircles.add(hexagon);
            } else {
                HexMap.redCircles.add(hexagon);
            }
            capture(arrayOfArrayLists);

            //check for victory
            if(enemyHexagons.isEmpty()){
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
        else {
            ExtendedPlay.invalidMoveText = makeText("Invalid Move!", new Point(720, 310));
            HexMap.root.getChildren().add(ExtendedPlay.invalidMoveText);
        }
        Possibilities.getBoardState();
    }

    public void changePlayer(){
        HexMap.currentPlayer = HexMap.currentPlayer.next();

        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            playerTurnCircle.setFill(Color.BLUE);
        } else {
            playerTurnCircle.setFill(Color.RED);
        }
        HexMap.turnCount++;
    }

    public void nonCapture(){
        Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
        root.getChildren().add(circle);
        //adds new hexagon to appropriate array based on current player turn
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            HexMap.blueCircles.add(hexagon);
        } else {
            HexMap.redCircles.add(hexagon);
        }
        changePlayer();
    }

    public void capture(ArrayList[] a){
        Point centre = null;
        int i = 0;

        while(a[i] != null){
            ArrayList<Hexagon> d = a[i];
            for (Hexagon b : d) {
                if (b instanceof Hexagon) {
                    if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
                        HexMap.redCircles.remove(b);
                    } else {
                        HexMap.blueCircles.remove(b);
                    }
                    /*Con-current access exception can occur */
                    centre = b.getCentre();
                }
                for(int j = 0; j < root.getChildren().size(); j++){
                    Node node = root.getChildren().get(j);
                    if(node instanceof Circle){
                        Circle circle;
                        circle = (Circle) node;
                        if((int)circle.getCenterX() == (int) centre.getX() && (int)circle.getCenterY() == (int) centre.getY()){
                            root.getChildren().remove(circle);
                        }
                    }
                }
            }
            i ++;
        }
    }

}
