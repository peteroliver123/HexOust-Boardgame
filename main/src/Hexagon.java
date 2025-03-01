import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    double x;
    double y;

    public Hexagon(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
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
        int xDiff = (int)Math.abs(hex.getX() - this.x);
        int yDiff = (int)Math.abs(hex.getY() - this.y);
        //the hex is directly above or below the current one
        if(xDiff == 0 && yDiff == 51){
            toReturn = true;
        }
        //hex is to the side of the current one
        if(xDiff == 45 && yDiff == 25){
            toReturn = true;
        }
        return toReturn;
    }
}
