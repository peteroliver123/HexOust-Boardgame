import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    Point centre;
    int q, r;

    public Hexagon(Point centre, int q, int r) {
        /*Q AND R ERROR CHECKING */
        /*Negative Obviously Wrong*/
        if(q < 0 || r < 0){
            throw new IllegalArgumentException("Positions of Hexagons must be within the board!");
        }
        /*If Q is 0 - 6 goes up to Q + 6*/
        if(q > 12){
            throw new IllegalArgumentException("There are not more than 12 columns of hexagons");
        }

        if(q <= 6){
            if(r > q + 6){
                throw new IllegalArgumentException("For first 7 columns of hexagons, where i is the column, there is no more than i + 6 rows");
            }
        }
        else {
            if(r > 18 - q){
                throw new IllegalArgumentException("For columns 7 to 12, where i is the column, there is no more than 18 - i rows");
            }
        }

        this.centre = centre;
        this.q = q;
        this.r = r;
    }

    public Point getCentre(){
        return this.centre;
    }

    public int getQ() {
        return this.q; // Column index
    }

    public int getR() {
        return this.r; // Row index
    }

    public boolean contains(double px, double py) {
        Double[] points = this.getPoints().toArray(new Double[0]);

        // Point-In-Polygon Algorithm
        int n = points.length / 2;
        boolean inside = false;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = points[2 * i], yi = points[2 * i + 1];
            double xj = points[2 * j], yj = points[2 * j + 1];

            boolean intersect = ((yi > py) != (yj > py)) &&
                    (px < (xj - xi) * (py - yi) / (yj - yi) + xi);
            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }

    //returns whether the given hex is a neighbor of this one
    public boolean isNeighbor(Hexagon hex){
        boolean toReturn = false;
        int xDiff = (int)Math.abs(hex.getCentre().getX() - this.centre.getX());
        int yDiff = (int)Math.abs(hex.getCentre().getY() - this.centre.getY());
        //the hex is directly above or below the current one
        if(xDiff == 0 && yDiff == 51){
            toReturn = true;
        }

        //hex is to the side of the current one
        int height = (int)((Math.sqrt(0.75)) * 30);

        if(xDiff == 45 && yDiff == height){
            toReturn = true;
        }

        return toReturn;
    }
}
