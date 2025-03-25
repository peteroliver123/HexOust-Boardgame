import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.robot.Robot;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit.ApplicationTest;

public class TestFetch extends ApplicationTest {
    public static HexMap hexMap;
    public static Pane root;
    public static Robot robot;
    @BeforeAll
    public static void initial() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("java.awt.headless", "true");
        hexMap = new HexMap();
        root = hexMap.initialize();
    }

    @Test
    public void testHexagonClass() {
        Hexagon[][] hx = HexMap.hexagons;
        assertNotNull(root);
        assertEquals(100, hx[0][0].getX());
        assertEquals(520, hx[0][0].getY());
    }

    @Test
    public void testMakeCircle(){
        hexMap.reset();
        simulateClick(1,1);
        assertEquals(1,HexMap.redCircles.size());
    }

    /*This test makes sure the user is unable to place a circle on a hexagon that already containsn one. */
    @Test
    public void spaceFull(){
        hexMap.reset();
        simulateClick(1, 1);
        simulateClick(1, 1);
        assertEquals(1, HexMap.redCircles.size());
        assertEquals(0, HexMap.blueCircles.size());
    }

    /*This test makes sure user isn't allowed to place on an invalid tile. */
    @Test
    public void invalidMove(){
        hexMap.reset();
        simulateClick(1, 1);
        simulateClick(5,5);
        simulateClick(1,2);
        assertEquals(1, HexMap.redCircles.size());
        assertEquals(1, HexMap.blueCircles.size());
    }

    /*This function makes sure capturing moves operate as intended */
    @Test
    public void capturing(){
        hexMap.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC
        simulateClick(2,1);//RED C
        assertEquals(1, HexMap.blueCircles.size());
        assertEquals(3, HexMap.redCircles.size());
    }

    /*This function makes sure that a caputuring move doesn't always end the game */
    @Test
    public void capturingButGameNotOver(){
        hexMap.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC
        simulateClick(2,1);//RED C
        assertFalse(HexMap.gameOver);
    }

    @Test
    public void testWin(){
        hexMap.reset();
        simulateClick(1,1);//RED NC
        simulateClick(1,3);//BLUE NC
        simulateClick(1,2);//RED C
        assertTrue(HexMap.gameOver);
        assertEquals(HexMap.PlayerTurn.RED, HexMap.currentPlayer);
    }

    @Test
    public void testWinLong(){
        hexMap.reset();
        HexMap.PlayerTurn start = HexMap.currentPlayer;
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(3,1);//RED NC
        simulateClick(5,3);//BLUE NC
        simulateClick(4,2);//RED C
        simulateClick(3,2);//RED C RED WINS!
        assertTrue(HexMap.gameOver);
        /*Red Was Winner */
        assertEquals(HexMap.PlayerTurn.RED, HexMap.currentPlayer);
        assertEquals(5,HexMap.turnCount);
    }

    /*Testing Hexagon.java*/
    /*This function tests the Hexagon class handles error when we hand it a negative value for the centre of the Hexagon*/
    @Test
    public void negativeCenter(){
        hexMap.reset();
        try {
            Hexagon h = new Hexagon(-1, 1, 1, 1);
            fail("Allowed negative center!");
        } catch (IllegalArgumentException ex){

        }
        try {
            Hexagon h = new Hexagon(1, -1, 1, 1);
            fail("Allowed negative center!");
        } catch (IllegalArgumentException ex){

        }
    }

    /*This function checks Hexagon class handles errors when we hand
    it a negative q and r position.
     */
    @Test
    public void negativePosition(){
        hexMap.reset();
        try {
            Hexagon h = new Hexagon(1, 1, -1, 1);
            fail("Allowed negative position!");
        } catch (IllegalArgumentException ex){

        }
        try {
            Hexagon h = new Hexagon(1, 1, 1, -1);
            fail("Allowed negative position!");
        } catch (IllegalArgumentException ex){

        }
    }

    /*This test checks that hexagon class correctly handles errors when we try
    to create hexagon with values of q and r that are too big */
    @Test
    public void invalidPosition(){
        /*Valid Position are q = 0...12, when q = 0..6 r = 0..q + 6 when q = 7..12 r = 0..18-q*/
        hexMap.reset();
        try {
            Hexagon h = new Hexagon(1, 1, 13, 1);
            fail("Allowed Invalid Q!");
        } catch (IllegalArgumentException ex){

        }
        try {
            Hexagon h = new Hexagon(1, 1, 1, 8);
            fail("Allowed invalid R!");
        } catch (IllegalArgumentException ex){

        }
        try {
            Hexagon h = new Hexagon(1, 1, 8, 19);
            fail("Allowed invalid R!");
        } catch (IllegalArgumentException ex){

        }
    }

    /*This Test checks whether the first two hexagons are correctly treated as neighbours
    and another case where they are not neighbours also works correctly */
    @Test
    public void isNeighbour(){
        hexMap.reset();
        Hexagon[][] hx = HexMap.hexagons;
        Hexagon a = hx[0][0];
        Hexagon b = hx[0][1];
        Hexagon c = hx[2][2];
        assertTrue(a.isNeighbor(b));
        assertFalse(a.isNeighbor(c));
    }

    public void simulateClick(int i, int j){

        /*Negative Obviously Wrong*/
        if(i < 0 || j < 0){
            throw new IllegalArgumentException("Positions of Hexagons must be within the board!");
        }
        /*If Q is 0 - 6 goes up to Q + 6*/
        if(i > 12){
            throw new IllegalArgumentException("There are not more than 12 columns of hexagons");
        }

        if(0 <= i && i <= 6){
            if(j > i + 6){
                throw new IllegalArgumentException("For first 7 columns of hexagons, where i is the column, there is no more than i + 6 rows");
            }
        }
        else {
            if(j > 18 - i){
                throw new IllegalArgumentException("For columns 7 to 12, where i is the column, there is no more than 18 - i rows");
            }
        }

        Hexagon hex = HexMap.hexagons[i][j];
        MouseClickHandler clickHandler = new MouseClickHandler(root, hex);
        MouseEvent click = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                hex.getX(), hex.getY(), 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false, true,
                false, false, false, false, false, null);
        clickHandler.handle(click);
    }
}