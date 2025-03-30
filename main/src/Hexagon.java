import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    Point centre;
    Point coordinatePosition;

    public Hexagon(Point centre, Point coordinatePosition) {
        /*Q AND R ERROR CHECKING */
        /*Negative Obviously Wrong*/
        this.centre = centre;
        this.coordinatePosition = coordinatePosition;
        coordinatePosition.coordinateCheck();
    }

    public Point getCentre(){
        return this.centre;
    }

    public Point getCoordinatePosition(){
        return this.coordinatePosition;
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
