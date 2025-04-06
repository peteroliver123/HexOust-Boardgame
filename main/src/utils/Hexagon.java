package utils;
/*
This is a helper class which describes a hexagon which is made up of two points, a centre of the hexagon
and its co-ordinate position in relation to the grid of hexagons we are creating. It has four functions:
getCentre, getCoordinatePosition (basic getter methods), contains and isNeighbour. Contains takes a utils.Point
that the mouse has clicked and checks whether the current hexagon contains that mousePoint within its
dimensions and hence returns true if the user has clicked inside the hexagon and else false. isNeighbour
takes a hexagon and checks whether the current hexagon borders the given hexagon returns true if yes else false.
 */

/*Imports */
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    /*Variables */
    Point centre;
    Point coordinatePosition;

    /*Constructor */
    public Hexagon(Point centre, Point coordinatePosition) {
        this.centre = centre;
        this.coordinatePosition = coordinatePosition;
        /*Additional Error Check for Co-ordinates*/
        coordinatePosition.coordinateCheck();
    }

    /*Getter Methods */
    public Point getCentre(){
        return this.centre;
    }

    public Point getCoordinatePosition(){
        return this.coordinatePosition;
    }

    /*
    This function takes a mousePoint that the user has clicked and
    determines whether it is inside the current hexagon. Returns true
    if yes and false otherwise. It implements utils.Point-In-Polygon Algorithm.
    Also makes sure that only allows placement if it is clear which
    hexagon user is placing in, i.e. User clicks exactly at pixel
    connecting multiple than don't place.
    Input: utils.Point
    Output: Boolean
     */
    public boolean contains(Point mousePoint) {
        Double[] points = this.getPoints().toArray(new Double[0]);
        int n = points.length / 2;
        boolean inside = false;

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point point1 = new Point(points[2 * i], points[2 * i + 1]);
            Point point2 = new Point(points[2 * j], points[2 * j + 1]);

            boolean intersect = ((point1.getY() > mousePoint.getY()) != (point2.getY() > mousePoint.getY())) &&
                    (mousePoint.getX() < (point2.getX() - point1.getX()) * (mousePoint.getY() - point1.getY()) / (point2.getY() - point1.getY()) + point1.getX());
            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }

    /*
    This function takes a hexagon and returns whether the given hexagon is
    touching the current hexagon. Returns true if yes and false otherwise.
    Input: utils.Hexagon
    Output: Boolean
     */
    public boolean isNeighbor(Hexagon hex){
        boolean toReturn = false;
        int xDiff = (int)Math.abs(hex.getCentre().getX() - this.centre.getX());
        int yDiff = (int)Math.abs(hex.getCentre().getY() - this.centre.getY());

        /*The hex is directly above or below the current one*/
        if(xDiff == 0 && yDiff == 51){
            toReturn = true;
        }

        /*The hex is to the side of the current one*/
        int height = (int)((Math.sqrt(0.75)) * 30);
        if(xDiff == 45 && yDiff == height){
            toReturn = true;
        }

        return toReturn;
    }
}
