package gr.kendrick.flat_earth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class OSMTileTest {

    private OSMTile testOsmTile = new OSMTile(
            "https://a.tile.openstreetmap.de/${z}/${x}/${y}.png",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36",
            0,
            0,
            0);

    @Test
    public void testGetColorRelativeToUpperLeftOrigin() {
        //  +-------->
        //  |
        //  |
        // \|/
        Color c = testOsmTile.getColorRelativeToUpperLeftOrigin(0, 0); // ocean
        Assertions.assertEquals(170, c.getRed());
        Assertions.assertEquals(211, c.getGreen());
        Assertions.assertEquals(223, c.getBlue());

        c = testOsmTile.getColorRelativeToUpperLeftOrigin(255, 0); // ocean
        Assertions.assertEquals(170, c.getRed());
        Assertions.assertEquals(211, c.getGreen());
        Assertions.assertEquals(223, c.getBlue());

        c = testOsmTile.getColorRelativeToUpperLeftOrigin(0, 255); // landmass (Antarctica)
        Assertions.assertEquals(242, c.getRed());
        Assertions.assertEquals(239, c.getGreen());
        Assertions.assertEquals(233, c.getBlue());

        c = testOsmTile.getColorRelativeToUpperLeftOrigin(255, 255); // landmass (Antarctica)
        Assertions.assertEquals(242, c.getRed());
        Assertions.assertEquals(239, c.getGreen());
        Assertions.assertEquals(233, c.getBlue());
    }

    @Test
    public void testGetColorRelativeToLowerLeftOrigin() {
        // /|\
        //  |
        //  |_________\
        //            /
        Color c = testOsmTile.getColorRelativeToLowerLeftOrigin(0, 0); // landmass (Antarctica)
        Assertions.assertEquals(242, c.getRed());
        Assertions.assertEquals(239, c.getGreen());
        Assertions.assertEquals(233, c.getBlue());

        c = testOsmTile.getColorRelativeToLowerLeftOrigin(255, 0); // landmass (Antarctica)
        Assertions.assertEquals(242, c.getRed());
        Assertions.assertEquals(239, c.getGreen());
        Assertions.assertEquals(233, c.getBlue());

        c = testOsmTile.getColorRelativeToLowerLeftOrigin(0, 255); // ocean
        Assertions.assertEquals(170, c.getRed());
        Assertions.assertEquals(211, c.getGreen());
        Assertions.assertEquals(223, c.getBlue());

        c = testOsmTile.getColorRelativeToLowerLeftOrigin(255, 255); // ocean
        Assertions.assertEquals(170, c.getRed());
        Assertions.assertEquals(211, c.getGreen());
        Assertions.assertEquals(223, c.getBlue());
    }
}
