import javafx.application.Application;

public static void main(String[] args) {
    Application.launch(HexMap.class, args);
    System.out.println(HexMap.hexagons[0][0].getX());
    System.out.println(HexMap.hexagons[0][0].getY());
}
