package gr.kendrick.flat_earth;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class FlatEarthWorldGeneratorTest {
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
