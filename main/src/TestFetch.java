import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.*;
import org.testfx.framework.junit.ApplicationTest;

public class TestFetch extends ApplicationTest {
    @BeforeAll
    public static void initial() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void testHexagonClass() {
        Hexagon[][] hx = HexMap.hexagons;
        HexMap hexMap = new HexMap();
        Pane root = hexMap.initialize();
        assertNotNull(root);
        assertEquals(100, hx[0][0].getX());
        assertEquals(520, hx[0][0].getY());
    }
}