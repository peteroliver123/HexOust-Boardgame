package utils;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static utils.Utility.*;

import java.util.ArrayList;

public class Possibilities {

    public static void getBoardState(){
        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 13; j++){
                state[i][j] = 0;
                try{
                    isValidHexagon(i, j);
                    if(isValidClick2(i, j)){
                        if(isNonCapturing(i, j)){
                            state[i][j] = 1;

                        } else if (isCapturing(i, j, null)){
                            state[i][j] = 2;
                        }
                    }
                } catch (IllegalArgumentException _){

                }
            }
        }

        for(int i = 0; i < 13; i++){
            for(int j = 0; j < 13; j++){
                try{
                    isValidHexagon(i, j);
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
        ArrayList<Hexagon> playerHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.redCircles : HexMap.blueCircles;
        ArrayList<Hexagon> enemyHexagons = (HexMap.currentPlayer == HexMap.PlayerTurn.RED) ? HexMap.blueCircles : HexMap.redCircles;
        ArrayList<Hexagon> tempStorer = new ArrayList<>();
        friendlyNeighbour.clear();
        enemyNeighbour.clear();
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
            if(z != null){
                z[numSubGroups] = new ArrayList<>(tempStorer);
                numSubGroups ++;
            }
            tempStorer.clear();
        }

        if(enemyNeighbour.isEmpty()){
            return false;
        }
        else {
            return maxGroup < friendlyNeighbour.size();
        }
    }

    public static boolean isValidClick2(int i, int j){
        Hexagon hex = hexagons[i][j];
        /* Check if hexagon already has circle */
        for (Node node : HexMap.root.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                if ((int) circle.getCenterX() == (int) hex.getCentre().getX() && (int) circle.getCenterY() == (int) hex.getCentre().getY()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void isValidHexagon(int i, int j){
        /*Negative Obviously Wrong*/
        if(i < 0 || j < 0){
            throw new IllegalArgumentException("Positions of Hexagons must be within the board!");
        }
        /*If Q is 0 - 6 goes up to Q + 6*/
        if(i > 12){
            throw new IllegalArgumentException("There are not more than 12 columns of hexagons");
        }

        if(i < 7){
            if(j > i + 6){
                throw new IllegalArgumentException("For first 7 columns of hexagons, where i is the column, there is no more than i + 6 rows");
            }
        }
        else {
            if(j > 18 - i){
                throw new IllegalArgumentException("For columns 7 to 12, where i is the column, there is no more than 18 - i rows");
            }
        }
    }
}
