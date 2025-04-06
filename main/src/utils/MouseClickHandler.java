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

    ArrayList<Hexagon> blueCircles = HexMap.getBlueCircles();
    ArrayList<Hexagon> redCircles = HexMap.getRedCircles();

    ArrayList<Hexagon> friendlyNeighbour = new ArrayList<>();
    ArrayList<Hexagon> enemyNeighbour = new ArrayList<>();
    ArrayList<Hexagon> tempStorer = new ArrayList<>();

    public MouseClickHandler(Pane root, Hexagon hexagon) {
        this.root = root;
        this.hexagon = hexagon;
    }

    /*Checks valid moves not related to mouse event */
    public boolean checkMoveValid(int maxGroup){
        if(!isValidClick()){
            return false;
        }
        else {
            if (enemyNeighbour.isEmpty()) {
                System.out.println("Invalid move. Cant place by own stone");
                return false;
            }
            else if (maxGroup >= friendlyNeighbour.size()) {
                System.out.println("Invalid move! Group not big Enough");
                return false;
            }
            else {
                return true;
            }
        }
    }


    @Override
    public void handle(MouseEvent event) {
        boolean isNonCapturing = true;
        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? redCircles : blueCircles;
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? blueCircles : redCircles;
        friendlyNeighbour.clear();
        enemyNeighbour.clear();
        tempStorer.clear();
        Point mousePoint = new Point(event.getX(), event.getY());

        /*PRE-CHECKS */
        if(!hexagon.contains(mousePoint)){
            return;
        }
        if(HexMap.gameOver){
            System.out.println("Game is over, please start a new one to keep playing!");
            return;
        }

        if(!isValidClick()){
            return;
        }

        /*If it doesn't have circle perform checks*/
        /*NON CAPTURING */
        for(Hexagon a : playerHexagons){
            if(hexagon.isNeighbor(a)){
                isNonCapturing = false;
                friendlyNeighbour.add(a);
            }
        }

        if(isNonCapturing){
            nonCapture();
            return;
        }

        //Is not non-capturing so proceed.
        /*Find the enemies that are touching you */
        for(Hexagon a : enemyHexagons){
            if(hexagon.isNeighbor(a)){
                enemyNeighbour.add(a);
            }
        }

        /*Find size of new group */
        friendlyNeighbour.add(hexagon);
        for(int i = 0; i < friendlyNeighbour.size(); i++){
            Hexagon a = friendlyNeighbour.get(i);
            for(Hexagon b : playerHexagons){
                if(a.isNeighbor(b) && (!friendlyNeighbour.contains(b))){
                    friendlyNeighbour.add(b);
                }
                for(Hexagon d : enemyHexagons){
                    if(a.isNeighbor(b) && b.isNeighbor(d)){
                        enemyNeighbour.add(d);
                    }
                }
            }
        }



        //size of friendlyNeighbour is now size of new group

        /*ENEMY SUBGROUPS */
        int maxGroup = 0;
        int numSubGroups = 0;
        int size = 100;
        ArrayList[] arrayOfArrayLists = new ArrayList[size];

        for(Hexagon a : enemyNeighbour){
            tempStorer.add(a);
            for(int i = 0; i < tempStorer.size(); i++){
                Hexagon c = tempStorer.get(i);
                for(Hexagon b : enemyHexagons){
                    if(c.isNeighbor(b) && (!tempStorer.contains(b))){
                        tempStorer.add(b);
                    }
                }
            }
            maxGroup = Math.max(maxGroup, tempStorer.size());
            arrayOfArrayLists[numSubGroups] = new ArrayList<>(tempStorer);
            numSubGroups ++;
            tempStorer.clear();
        }

        if(!checkMoveValid(maxGroup)){
            return;
        }

        System.out.println("Capture Move Played: Max Group Size: " + maxGroup + "New Group Size: " + friendlyNeighbour.size());

        /*If checks correct draw circle */
        Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
        root.getChildren().add(circle);
        //adds new hexagon to appropriate array based on current player turn
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            blueCircles.add(hexagon);
        } else {
            redCircles.add(hexagon);
        }
        capture(arrayOfArrayLists, numSubGroups);

        //check for victory
        if(enemyHexagons.isEmpty()){
            System.out.println(HexMap.currentPlayer + " won in " + HexMap.turnCount + " turns");
            ExtendedPlay.endGameText = makeText("Game Over! " + HexMap.currentPlayer + " won!", new Point(820, 310));
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

    public boolean isValidClick(){
        /* Check if hexagon already has circle */
        for (Node node : root.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                if ((int) circle.getCenterX() == (int) hexagon.getCentre().getX() && (int) circle.getCenterY() == (int) hexagon.getCentre().getY()) {
                    return false;
                }
            }
        }
        return true;
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
        System.out.println("Non-capturing move performed");
        Circle circle = drawCircle(new Point(hexagon.getCentre().getX(), hexagon.getCentre().getY()));
        root.getChildren().add(circle);
        //adds new hexagon to appropriate array based on current player turn
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            blueCircles.add(hexagon);
        } else {
            redCircles.add(hexagon);
        }
        changePlayer();
    }

    public void capture(ArrayList[] a, int count){
        Point centre = null;
        for (int i = 0; i < count; i++) {
            ArrayList<Hexagon> d = a[i];
            for (Hexagon b : d) {
                if (b instanceof Hexagon) {
                    if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
                        redCircles.remove(b);
                    } else {
                        blueCircles.remove(b);
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
        }
    }

}
