/*This class tells the program which hexagons are what type of moves. There are three
types of moves invalid moves (which have different types treated the same), valid
non-capturing, valid capturing. It has _ functions getBoardState which alters the global
multidimensional state array which holds the hexagons states. 0 for invalid, 1 for non-capturing,
2 for capturing. isNonCapturing returns true if for a given i, j in the hexagons array, the
hexagon having a circle for the current player turn would be non-capturing. isCapturing does the same
for capturing moves. */

package utils;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static utils.Utility.*;

import java.util.ArrayList;

public class Possibilities {

    public static void getBoardState(){
        int maximum = 0;
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 13; j++){
                try{
                    Point currentHexCoordinates = new Point(i, j);
                    currentHexCoordinates.coordinateCheck();
                    state[i][j] = 0;
                    if(isValidClick(currentHexCoordinates)){
                        if(isNonCapturing(currentHexCoordinates)){
                            state[i][j] = 1;

                        } else if (isCapturing(currentHexCoordinates, null)){
                            state[i][j] = 2;
                        }
                        maximum = Math.max(maximum, state[i][j]);
                    }
                    noValidMoves = maximum == 0;
                    if(state[i][j] == 1 || state[i][j] == 2){
                        hexagons[i][j].setFill(Color.GREEN);
                    }
                    else {
                        hexagons[i][j].setFill(Color.web("#DEE6E8"));
                    }
                } catch (IllegalArgumentException _){

                }
            }
        }
    }

    public static boolean isNonCapturing(Point hexCoordinates){
        ArrayList<Hexagon> playerHexagons = HexMap.getPlayerHexagons();
        Hexagon currentHex = hexagons[(int) hexCoordinates.getX()][(int) hexCoordinates.getY()];

        for(Hexagon friendlyHex : playerHexagons){
            if(currentHex.isNeighbor(friendlyHex)){
                return false;
            }
        }

        return true;
    }

    public static boolean isCapturing(Point hexCoordinates, ArrayList[] enemySubGroupsHolder){
        ArrayList<Hexagon> friendlyNeighbour = new ArrayList<>(); //arrayList of hexagons controlled by current player that touch current player or its neighbours.
        ArrayList<Hexagon> enemyNeighbour = new ArrayList<>(); //arrayList of hexagons not controlled by current player that touch current player or its neighbours.

        Hexagon currentHex = hexagons[(int) hexCoordinates.getX()][(int) hexCoordinates.getY()];

        updateNeighbours(currentHex, friendlyNeighbour, enemyNeighbour);

        findNewGroupSize(friendlyNeighbour, enemyNeighbour);

        if(enemyNeighbour.isEmpty()){
            return false;
        }
        else {
            int maxGroup = enemySubGroupCalculator(enemySubGroupsHolder, enemyNeighbour);
            return maxGroup < friendlyNeighbour.size();
        }
    }

    public static void findNewGroupSize(ArrayList<Hexagon> friendlyNeighbour, ArrayList<Hexagon> enemyNeighbour){
        ArrayList<Hexagon> playerHexagons = HexMap.getPlayerHexagons();
        ArrayList<Hexagon> enemyHexagons = HexMap.getEnemyHexagons();
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
    }

    public static void updateNeighbours(Hexagon currentHex, ArrayList<Hexagon> friendlyNeighbour, ArrayList<Hexagon> enemyNeighbour){
        ArrayList<Hexagon> enemyHexagons = HexMap.getEnemyHexagons();

        /*Add current Hexagon to friendlyNeighbour */
        friendlyNeighbour.add(currentHex);
        /*Add touching enemy hexagons to enemyNeighbour */
        for(Hexagon enemyHex : enemyHexagons){
            if(currentHex.isNeighbor(enemyHex)){
                enemyNeighbour.add(enemyHex);
            }
        }
    }


    public static int enemySubGroupCalculator(ArrayList[] enemySubGroupsHolder, ArrayList<Hexagon> enemyNeighbour){
        ArrayList<Hexagon> enemyHexagons = HexMap.getEnemyHexagons();
        ArrayList<Hexagon> enemySubGroup = new ArrayList<>();

        int maxGroup = 0;
        int numSubGroups = 0;

        for(Hexagon enemyHex : enemyNeighbour){
            enemySubGroup.add(enemyHex);
            for(int i = 0; i < enemySubGroup.size(); i++){
                Hexagon currentEnemyHex = enemySubGroup.get(i);
                for(Hexagon potentialTouchingEnemyHex : enemyHexagons){
                    if(currentEnemyHex.isNeighbor(potentialTouchingEnemyHex) && (!enemySubGroup.contains(potentialTouchingEnemyHex))){
                        enemySubGroup.add(potentialTouchingEnemyHex);
                    }
                }
            }
            maxGroup = Math.max(maxGroup, enemySubGroup.size());
            if(enemySubGroupsHolder != null){
                enemySubGroupsHolder[numSubGroups] = new ArrayList<>(enemySubGroup);
                numSubGroups ++;
            }
            enemySubGroup.clear();
        }
        return maxGroup;
    }


    public static boolean isValidClick(Point hexCoordinates){
        Hexagon hex = hexagons[(int) hexCoordinates.getX()][(int) hexCoordinates.getY()];
        /* Check if hexagon already has circle */
        for (Node node : HexMap.root.getChildren()) {
            if (node instanceof Circle circle) {
                if ((int) circle.getCenterX() == (int) hex.getCentre().getX() && (int) circle.getCenterY() == (int) hex.getCentre().getY()) {
                    return false;
                }
            }
        }
        return true;
    }
}
