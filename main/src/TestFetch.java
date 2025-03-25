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
        assertEquals(1,HexMap.redHexagons.size());
    }

    @Test
    public void testWin(){
        hexMap.reset();
        simulateClick(1,1);
        simulateClick(1,3);
        simulateClick(1,2);
        assertTrue(HexMap.gameOver);
    }

    @Test
    public void testWinLong(){
        hexMap.reset();
        HexMap.PlayerTurn start = HexMap.currentPlayer;
        simulateClick(1,1);
        simulateClick(3,3);
        simulateClick(3,1);
        simulateClick(5,3);
        simulateClick(4,2);
        simulateClick(3,2);
        assertTrue(HexMap.gameOver);
        if(start == HexMap.PlayerTurn.RED) {
            assertEquals(HexMap.PlayerTurn.RED, HexMap.currentPlayer);
        }
        else{
            assertEquals(HexMap.PlayerTurn.BLUE, HexMap.currentPlayer);
        }
        assertEquals(5,HexMap.turnCount);
    }


    private void simulateClick(int i, int j){
        Hexagon hex = HexMap.hexagons[i][j];
        MouseClickHandler clickHandler = new MouseClickHandler(root, hex);
        MouseEvent click = new MouseEvent(MouseEvent.MOUSE_PRESSED,
                hex.getX(), hex.getY(), 0, 0, MouseButton.PRIMARY, 1,
                false, false, false, false, true,
                false, false, false, false, false, null);
        clickHandler.handle(click);
    }
}