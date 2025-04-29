package utils.utils.test;

/*Imports */
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.*;
import utils.utils.*;

import static utils.utils.Utility.*;


/*Performance testing */


public class TestPerformance {
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

    /*PERFORMANCE TESTING */
    @Test
    public void testBoardLoadTime(){
        long startTime = System.nanoTime();
        hexMap.initialize();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        assertTrue(duration < 1_000_000_000L);
    }

    @Test
    public void testMoveSimpleTime(){
        long startTime = System.nanoTime();
        Possibilities.getBoardState();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        assertTrue(duration < 5_000_000_000L);
    }

    @Test
    public void testMoveComplexTime(){
        long startTime = System.nanoTime();
        ExtendedPlay.reset();
        simulateClick(1,1);//RED NC
        simulateClick(3,3);//BLUE NC
        simulateClick(3,1);//RED NC
        simulateClick(5,3);//BLUE NC
        simulateClick(4,2);//RED C
        Possibilities.getBoardState();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        assertTrue(duration < 5_000_000_000L);
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
