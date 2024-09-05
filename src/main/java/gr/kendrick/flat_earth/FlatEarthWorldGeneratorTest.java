package gr.kendrick.flat_earth;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class FlatEarthWorldGeneratorTest {
    @Test
    public void testColorDistance() {
        Assertions.assertEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.BLACK, Color.BLACK));
        Assertions.assertEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.WHITE, Color.WHITE));
        Assertions.assertEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.RED, Color.RED));
        Assertions.assertEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.GREEN, Color.GREEN));
        Assertions.assertEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.BLUE, Color.BLUE));

        Assertions.assertNotEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.BLACK, Color.WHITE));
        Assertions.assertNotEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.WHITE, Color.BLACK));
        Assertions.assertNotEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.RED, Color.GREEN));
        Assertions.assertNotEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.RED, Color.BLUE));
        Assertions.assertNotEquals(0.0, FlatEarthWorldGenerator.colorDistance(Color.GREEN, Color.BLUE));

        Assertions.assertTrue(FlatEarthWorldGenerator.colorDistance(Color.RED, Color.ORANGE)
                                < FlatEarthWorldGenerator.colorDistance(Color.RED, Color.GREEN));

        Assertions.assertTrue(FlatEarthWorldGenerator.colorDistance(Color.BLUE, Color.CYAN)
                < FlatEarthWorldGenerator.colorDistance(Color.BLUE, Color.RED));
    }

    @Test
    public void testClosestAvailableColor() {
        Assertions.assertEquals(Color.BLACK, FlatEarthWorldGenerator.closestAvailableColor(Color.BLACK));
        Assertions.assertEquals(Color.WHITE, FlatEarthWorldGenerator.closestAvailableColor(Color.WHITE));
        Assertions.assertEquals(Color.RED, FlatEarthWorldGenerator.closestAvailableColor(Color.RED));

        Assertions.assertEquals(Color.BLACK, FlatEarthWorldGenerator.closestAvailableColor(new Color(1,1,1)));
        Assertions.assertEquals(Color.WHITE, FlatEarthWorldGenerator.closestAvailableColor(new Color(250,250,250)));
        Assertions.assertEquals(Color.RED, FlatEarthWorldGenerator.closestAvailableColor(new Color(250,0,0)));

        Color osmOcean = new Color(170, 211, 223);
        Color osmLand = new Color(242, 239, 233);
        Assertions.assertNotEquals(FlatEarthWorldGenerator.closestAvailableColor(osmOcean),
                FlatEarthWorldGenerator.closestAvailableColor(osmLand)); // !!! VERY IMPORTANT CHECK !!!
    }

    @Test
    public void testGetColorAt() {
        // __________
        // |        |
        // |        |
        // ----------
        int zoomLevel = 0;
        String tileServerTemplateURL = "https://a.tile.openstreetmap.de/${z}/${x}/${y}.png";
        int tileSizeInPx = 256;
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36";
        FlatEarthWorldGenerator worldGenerator = new FlatEarthWorldGenerator(zoomLevel, tileServerTemplateURL,
                tileSizeInPx, userAgent);
        // Ocean (top left and top right corner of map):
        Assertions.assertEquals(new Color(170, 211, 223), worldGenerator.getColorAt(-127, -127));
        Assertions.assertEquals(new Color(170, 211, 223), worldGenerator.getColorAt(+127, -127));
        // Landmass (bottom left and bottom right corner of map):
        Assertions.assertEquals(new Color(242, 239, 233), worldGenerator.getColorAt(-127, +127));
        Assertions.assertEquals(new Color(242, 239, 233), worldGenerator.getColorAt(+127, +127));
    }
}
