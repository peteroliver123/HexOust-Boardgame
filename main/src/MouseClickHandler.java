import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class MouseClickHandler implements EventHandler<MouseEvent> {
    Pane root;
    Hexagon hexagon;

    ArrayList<Hexagon> blueHexagons = HexMap.getBlueHexagons();
    ArrayList<Hexagon> redHexagons = HexMap.getRedHexagons();

    boolean isNonCapturing = true;
    boolean invalidMove = false;
    ArrayList<Hexagon> friendlyNeighbour = new ArrayList<>();
    ArrayList<Hexagon> enemyNeighbour = new ArrayList<>();
    ArrayList<Hexagon> tempStorer = new ArrayList<>();

    public MouseClickHandler(Pane root, Hexagon hexagon) {
        this.root = root;
        this.hexagon = hexagon;
    }

    @Override
    public void handle(MouseEvent event) {
        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? redHexagons : blueHexagons;
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? blueHexagons : redHexagons;
        friendlyNeighbour.clear();
        enemyNeighbour.clear();
        tempStorer.clear();
        double mouseX = event.getX();
        double mouseY = event.getY();

        /*PRE-CHECKS */
        if(!hexagon.contains(mouseX, mouseY)){
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

        System.out.println("here");

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

        /*We are placing against our own stones and only our own */
        if(enemyNeighbour.isEmpty()){
            System.out.println("Invalid move. Cant place by own stone");
            return;
        }

        /*Find size of new group */
        friendlyNeighbour.add(hexagon);
        for(Hexagon a : friendlyNeighbour){
            for(Hexagon b : playerHexagons){
                if(a.isNeighbor(b) && (!friendlyNeighbour.contains(b))){
                    friendlyNeighbour.add(b);
                }
            }
        }
        //size of friendlyNeighbour is now size of new group
        int max_group = 0;
        int numSubGroups = 0;
        int size = 100;
        ArrayList[] arrayOfArrayLists = new ArrayList[size];

        for(Hexagon a : enemyNeighbour){
            tempStorer.add(a);
            for(Hexagon c : tempStorer){
                for(Hexagon b : enemyHexagons){
                    if(c.isNeighbor(b) && (!tempStorer.contains(b))){
                        tempStorer.add(b);
                    }
                }
            }
            max_group = Math.max(max_group, tempStorer.size());
            arrayOfArrayLists[numSubGroups] = new ArrayList<>(tempStorer);
            numSubGroups ++;
            tempStorer.clear();
        }

        if (max_group >= friendlyNeighbour.size()) {
            System.out.println("Invalid move! Group not big Enough");
            return;
        }

        System.out.println("Capture Move Played: Max Group Size: " + max_group + "New Group Size: " + friendlyNeighbour.size());

        /*If checks correct draw circle */
        Circle circle = utility.drawCircle(hexagon.getX(), hexagon.getY());
        root.getChildren().add(circle);
        //adds new hexagon to appropriate array based on current player turn
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            blueHexagons.add(hexagon);
        } else {
            redHexagons.add(hexagon);
        }
        capture(arrayOfArrayLists, numSubGroups);
    }

    public boolean isValidClick(){
        /* Check if hexagon already has circle */
        for (Node node : root.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                if (circle.getCenterX() == hexagon.getX() && circle.getCenterY() == hexagon.getY()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void changePlayer(){
        HexMap.currentPlayer = HexMap.currentPlayer.next();

        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            HexMap.playerTurnCircle.setFill(Color.BLUE);
        } else {
            HexMap.playerTurnCircle.setFill(Color.RED);
        }
    }

    public void nonCapture(){
        System.out.println("Non-capturing move performed");
        Circle circle = utility.drawCircle(hexagon.getX(), hexagon.getY());
        root.getChildren().add(circle);
        //adds new hexagon to appropriate array based on current player turn
        if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
            blueHexagons.add(hexagon);
        } else {
            redHexagons.add(hexagon);
        }
        changePlayer();
    }

    public void capture(ArrayList[] a, int count){
        int xCent = 0;
        int yCent = 0;
        for (int i = 0; i < count; i++) {
            ArrayList<Hexagon> d = a[i];
            for (Hexagon b : d) {
                if (b instanceof Hexagon) {
                    if (HexMap.currentPlayer == HexMap.PlayerTurn.BLUE) {
                        redHexagons.remove(b);
                    } else {
                        blueHexagons.remove(b);
                    }
                    /*Con-current access exception can occur */
                    xCent = (int) b.getX();
                    yCent = (int) b.getY();
                }
            }
        }
        Circle target = null;
        System.out.println("XCent: " + xCent + "YCent: " + yCent);
        for(Node node : root.getChildren()){
            if(node instanceof Circle){
                Circle circle;
                circle = (Circle) node;
                if((int)circle.getCenterX() == xCent && (int)circle.getCenterY() == yCent){
                    target = circle;
                }
            }
        }
        if(target != null){
            root.getChildren().remove(target);
        }
    }

}
