/*Imports */
import static org.junit.jupiter.api.Assertions.*;
import static utils.Utility.*;
import utils.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;

public class TestGame {
    public static HexMap hexMap;
    public static Pane root;

    @BeforeAll
    public static void initial() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("java.awt.headless", "true");
        hexMap = new HexMap();
        root = hexMap.initialize();
    }

    /*GUI TESTING */
    @Test
    public void testHexagonClass() {
        ExtendedPlay.reset();
        Hexagon[][] hx = hexagons;

        assertNotNull(root);
        assertEquals(100, hx[0][0].getCentre().getX());
        assertEquals(515.88, hx[0][0].getCentre().getY());
    }

    @Test
    public void testMakeText(){
        Text newText = makeText("To Make a Move", new Point (0, 0));

        assertNotNull(newText);
    }

    @Test
    public void testMakeTextOffScreen(){
        try {
            makeText("To Make a Move", new Point(-1000, -1000));
            fail("Allowed text off-screen");
        } catch (IllegalArgumentException e){
        }
    }

    @Test
    public void testMakeCircle(){
        ExtendedPlay.reset();
        simulateClick(1,1);

        assertEquals(1, HexMap.getRedHexagons().size());
    }

    @Test
    public void testMakeCircleInvalid(){
        ExtendedPlay.reset();
        try {
            simulateClick(-1,-1);
        } catch (IllegalArgumentException e){
        }

        assertEquals(0,HexMap.getRedHexagons().size());
    }

    /*LOGIC TESTING */
    /*This test makes sure the user is unable to place a circle on a hexagon that already contains one. */
    @Test
    public void testSpaceFull(){
        ExtendedPlay.reset();
        simulateClick(1, 1);
        simulateClick(1, 1);

        assertEquals(1, HexMap.getRedHexagons().size());
        assertEquals(0, HexMap.getBlueHexagons().size());
    }

    /*This test makes sure user isn't allowed to place on an invalid tile. */
    @Test
    public void testInvalidMove(){
        ExtendedPlay.reset();
        simulateClick(1, 1);
        simulateClick(5,5);
        simulateClick(1,2);

        assertEquals(1, HexMap.getRedHexagons().size());
        assertEquals(1, HexMap.getBlueHexagons().size());
    }

    @Test
    public void testNonCapturing(){
        ExtendedPlay.reset();
        simulateClick(1, 1);
        simulateClick(5, 5);

        assertEquals(1, HexMap.getRedHexagons().size());
        assertEquals(1, HexMap.getBlueHexagons().size());
    }

    /*This function makes sure capturing moves operate as intended */
    @Test
    public void testCapturing(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC
        simulateClick(2,1);//RED C

        assertEquals(1, HexMap.getBlueHexagons().size());
        assertEquals(3, HexMap.getRedHexagons().size());
    }

    @Test
    public void testGroupNotBigEnough(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(4, 3);//RED NC
        simulateClick(3, 2);//BLUE C
        simulateClick(7,7);//BLUE NC

        assertEquals(0, state[1][2]);
        simulateClick(1,2);
        assertEquals(3, HexMap.getBlueHexagons().size());
        assertEquals(1, HexMap.getRedHexagons().size());
    }

    /*This function makes sure that a capturing move doesn't always end the game */
    @Test
    public void testCapturingButGameNotOver(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC
        simulateClick(2,1);//RED C

        assertFalse(ExtendedPlay.getGameOverStatus());
    }

    /*STATE TESTING */
    @Test
    public void testCapturingState(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC

        assertEquals(2, state[2][1]);
    }

    @Test
    public void testNonCapturingState(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC

        assertEquals(1, state[7][7]);
    }

    @Test
    public void testOccupiedState(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC

        assertEquals(0, state[1][1]);
    }

    @Test
    public void testInvalidState(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC

        assertEquals(0, state[5][3]);
    }

    @Test
    public void testOutOfBoundsState(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(5, 4);//RED NC
        simulateClick(1, 2);//BLUE NC

        assertEquals(0, state[12][12]);
    }

    @Test
    public void testStateArray(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC

        int[] expectedRowOne1 = {1,0,1,1,1,1,1,1,0,0,0,0,0};
        assertArrayEquals(expectedRowOne1, state[1]);

        simulateClick(1, 3);//BLUE NC

        int[] expectedRowOne2 = {0,0,2,0,1,1,1,1,0,0,0,0,0};
        assertArrayEquals(expectedRowOne2, state[1]);
    }

    /*ENDGAME TESTING */
    @Test
    public void testWin(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(1,3);//BLUE NC
        simulateClick(1,2);//RED C

        assertTrue(ExtendedPlay.getGameOverStatus());
        assertEquals(HexMap.PlayerTurn.RED, HexMap.getCurrentPlayer());
    }

    @Test
    public void testWinLong(){
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(3,1);//RED NC
        simulateClick(5,3);//BLUE NC
        simulateClick(4,2);//RED C
        simulateClick(3,2);//RED C RED WINS!

        assertTrue(ExtendedPlay.getGameOverStatus());
        /*Red Was Winner */
        assertEquals(HexMap.PlayerTurn.RED, HexMap.getCurrentPlayer());
        assertEquals(5,HexMap.getTurnCount());
    }

    @Test
    public void testPlayRound2(){
        hexMap.initialize();
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(1,3);//BLUE NC
        simulateClick(1,2);//RED C

        ExtendedPlay.reset();
        assertEquals(2,ExtendedPlay.extendedPlay.getTurnCount());
    }

    @Test
    public void testCountWins(){
        hexMap.initialize();
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(1,3);//BLUE NC
        simulateClick(1,2);//RED Wins

        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(1,3);//BLUE NC
        simulateClick(1,2);//RED Wins

        assertEquals(2,ExtendedPlay.extendedPlay.getRedWin());
    }

    /*HEXAGON TESTING */
    /*This function tests the utils.Hexagon class handles error when we hand it a negative value for the centre of the utils.utils.Hexagon*/
    @Test
    public void testNegativeCenter(){
        ExtendedPlay.reset();

        try {
            new Hexagon(new Point(-1, 1), new Point(1, 1));
            fail("Allowed negative center!");
        } catch (IllegalArgumentException e){
        }
        try {
            new Hexagon(new Point(1, -1), new Point(1, 1));
            fail("Allowed negative center!");
        } catch (IllegalArgumentException e){
        }
    }

    /*This function checks utils.Hexagon class handles errors when we hand
    it a negative q and r position.
     */
    @Test
    public void testNegativePosition(){
        ExtendedPlay.reset();

        try {
            new Hexagon(new Point(1, 1), new Point(-1, 1));
            fail("Allowed negative position!");
        } catch (IllegalArgumentException e){
        }
        try {
            new Hexagon(new Point(1, 1), new Point(1, -1));
            fail("Allowed negative position!");
        } catch (IllegalArgumentException e){
        }
    }

    /*This test checks that hexagon class correctly handles errors when we try
    to create hexagon with values of q and r that are too big */
    @Test
    public void testInvalidPosition(){
        /*Valid Position are q = 0...12, when q = 0..6 r = 0..q + 6 when q = 7..12 r = 0..18-q*/
        ExtendedPlay.reset();

        try {
            new Hexagon(new Point(1, 1), new Point(13, 1));
            fail("Allowed Invalid Q!");
        } catch (IllegalArgumentException e){
        }
        try {
            new Hexagon(new Point(1, 1), new Point(1, 8));
            fail("Allowed invalid R!");
        } catch (IllegalArgumentException e){
        }
        try {
            new Hexagon(new Point(1, 1), new Point(8, 19));
            fail("Allowed invalid R!");
        } catch (IllegalArgumentException e){
        }
    }

    /*This Test checks whether the first two hexagons are correctly treated as neighbours
    and another case where they are not neighbours also works correctly */
    @Test
    public void testIsNeighbour(){
        ExtendedPlay.reset();
        Hexagon[][] hx = hexagons;
        Hexagon a = hx[0][0];
        Hexagon b = hx[0][1];
        Hexagon c = hx[2][2];

        assertTrue(a.isNeighbor(b));
        assertFalse(a.isNeighbor(c));
    }

    @Test
    public void testHexagonContains(){
        ExtendedPlay.reset();
        Hexagon[][] hx = hexagons;

        assertTrue(hx[0][0].contains(hx[0][0].getCentre()));
    }

    private void simulateClick(int i, int j){
        /*Error Checking*/
        Point a = new Point(i, j);
        a.coordinateCheck();

        Hexagon hex = hexagons[i][j];
        MoveHandler clickHandler = new MoveHandler(root, hex);
        MouseEvent click = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                hex.getCentre().getX(), hex.getCentre().getY(), 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false, true,
                false, false, false, false, false, null);
        clickHandler.handle(click);
    }
}