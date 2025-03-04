import java.util.*;

public class NonCapturing {
    private int[][] placementGrid;
    private List<Hexagon> redHexagons;
    private List<Hexagon> blueHexagons;
    private static final int[][] HEX_NEIGHBOR_OFFSETS = {
            {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 0}, {-1, -1}
    };

    // TO FIX:
    /*
    OFFSETS ARE DIFFERENT FOR IF HEXAGON IN DESCENDING (LEFT HAND) SECTION
    OR MID (CENTER AXIS) SECTION
    OR ASCENDING (RIGHT HAND) SECTION

    TO DO:
    IMPLEMENT OTHER OFFSETS, HAVE CHECK FOR COLUMN > / < / =
    ALSO HAVE CHECK TO PREVENT OUT OF INDEX (IF OFFSETS APPLIED LEADS TO -1)
     */

    public NonCapturing(int size) {
        this.placementGrid = new int[2 * size - 1][2 * size - 1];
        this.redHexagons = new ArrayList<>();
        this.blueHexagons = new ArrayList<>();
    }

    public void updatePlacement(Hexagon hex, int q, int r, HexMap.PlayerTurn currentPlayer) {
        int playerValue = (currentPlayer == HexMap.PlayerTurn.RED) ? 1 : -1;

        if (currentPlayer == HexMap.PlayerTurn.RED) {
            redHexagons.add(hex);
        } else {
            blueHexagons.add(hex);
        }

        for (int[] offset : HEX_NEIGHBOR_OFFSETS) {
            int neighborQ = q + offset[0];
            int neighborR = r + offset[1];
            // System.out.println(neighborQ + " " + neighborR);

            if (isValidHex(neighborQ, neighborR)) {
                if (placementGrid[neighborQ][neighborR] == 0) {
                    placementGrid[neighborQ][neighborR] = playerValue;
                } else if (placementGrid[neighborQ][neighborR] != playerValue) {
                    placementGrid[neighborQ][neighborR] += playerValue;
                }
            }
        }
    }

    public boolean isValidPlacement(int q, int r, HexMap.PlayerTurn currentPlayer) {
        int value = placementGrid[q][r];
        // System.out.println(value + " Hexagon: " + q + " " + r + " " + currentPlayer);
        if (value == 0 || Math.abs(value) == 2) {
            return true;
        }
        return (currentPlayer == HexMap.PlayerTurn.RED && value == -1) ||
                (currentPlayer == HexMap.PlayerTurn.BLUE && value == 1);
    }

    private boolean isValidHex(int q, int r) {
        return q >= 0 && q < placementGrid.length && r >= 0 && r < placementGrid[0].length;
    }
}
