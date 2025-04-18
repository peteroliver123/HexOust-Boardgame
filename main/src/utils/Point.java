package utils;

/*
This is a helper class which describes a point on the pane with an x co-ordinate
and a y co-ordinate. The function error checks the users input for new points. It
has three methods: getX, getY (basic default getters) and coordinateCheck (Error
Checking the given input variables but only in the case that x,y represent co-ordinates).
 */
public class Point {
    /*Variables */
    private final double x;
    private final double y;

    /*Constructor */
    public Point(double x, double y){
        /*Error Checking */
        if(x < 0 || y < 0){//Check if user input is off-screen
            throw new IllegalArgumentException("utils.Point cannot be outside screen!");
        }

        this.x = x;
        this.y = y;
    }

    /*Getter Methods */
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    /*Error Checking Function for Co-ordinate implementation of Point class */
    public void coordinateCheck(){
        if(x > 12){
            throw new IllegalArgumentException("There are not more than 12 columns of hexagons!");
        }
        /*If x is 0 - 6 goes up to x + 6*/
        if(x < 7){
            if(y > x + 6){
                throw new IllegalArgumentException("For first 7 columns of hexagons, where i is the column, there is no more than i + 6 rows!");
            }
        }
        /*If x is 7 - 12 goes up to 18 - x */
        else {
            if(y > 18 - x){
                throw new IllegalArgumentException("For columns 7 to 12, where i is the column, there is no more than 18 - i rows!");
            }
        }
    }

}

