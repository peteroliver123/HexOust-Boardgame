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
                state[i][j] = 0;
                try{
                    Point a = new Point(i, j);
                    a.coordinateCheck();
                    if(isValidClick(i, j)){
                        if(isNonCapturing(i, j)){
                            state[i][j] = 1;

                        } else if (isCapturing(i, j, null)){
                            state[i][j] = 2;
                        }
                        maximum = Math.max(maximum, state[i][j]);
                    }
                    noValidMoves = maximum == 0;
                } catch (IllegalArgumentException _){

                }
            }
        }

        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 13; j++){
                try{
                    Point a = new Point(i, j);
                    a.coordinateCheck();
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

    public static boolean isNonCapturing(int i, int j){
        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.redCircles : HexMap.blueCircles;
        Hexagon hex = hexagons[i][j];

        for(Hexagon a : playerHexagons){
            if(hex.isNeighbor(a)){
                return false;
            }
        }

        return true;
    }

    public static boolean isCapturing(int x, int y, ArrayList[] z){
        ArrayList<Hexagon> friendlyNeighbour = new ArrayList<>(); //arrayList of hexagons controlled by current player that touch current player or its neighbours.
        ArrayList<Hexagon> enemyNeighbour = new ArrayList<>(); //arrayList of hexagons not controlled by current player that touch current player or its neighbours.

        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.redCircles : HexMap.blueCircles;
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.blueCircles : HexMap.redCircles;
        ArrayList<Hexagon> enemySubGroup = new ArrayList<>();
        Hexagon hex = hexagons[x][y];

        /*NON CAPTURING */
        for(Hexagon a : playerHexagons){
            if(hex.isNeighbor(a)){
                friendlyNeighbour.add(a);
            }
        }

        //Is not non-capturing so proceed.
        /*Find the enemies that are touching you */
        for(Hexagon a : enemyHexagons){
            if(hex.isNeighbor(a)){
                enemyNeighbour.add(a);
            }
        }

        /*Find size of new group */
        friendlyNeighbour.add(hex);
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

        for(Hexagon a : enemyNeighbour){
            enemySubGroup.add(a);
            for(int i = 0; i < enemySubGroup.size(); i++){
                Hexagon c = enemySubGroup.get(i);
                for(Hexagon b : enemyHexagons){
                    if(c.isNeighbor(b) && (!enemySubGroup.contains(b))){
                        enemySubGroup.add(b);
                    }
                }
            }
            maxGroup = Math.max(maxGroup, enemySubGroup.size());
            if(z != null){
                z[numSubGroups] = new ArrayList<>(enemySubGroup);
                numSubGroups ++;
            }
            enemySubGroup.clear();
        }

        if(enemyNeighbour.isEmpty()){
            return false;
        }
        else {
            return maxGroup < friendlyNeighbour.size();
        }
    }

    public static boolean isValidClick(int i, int j){
        Hexagon hex = hexagons[i][j];
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
