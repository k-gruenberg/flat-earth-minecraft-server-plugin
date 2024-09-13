package gr.kendrick.flat_earth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class ColorToMaterialMappingsTest {
    @Test
    public void testColorDistance() {
        Assertions.assertEquals(0.0, ColorToMaterialMappings.colorDistance(Color.BLACK, Color.BLACK));
        Assertions.assertEquals(0.0, ColorToMaterialMappings.colorDistance(Color.WHITE, Color.WHITE));
        Assertions.assertEquals(0.0, ColorToMaterialMappings.colorDistance(Color.RED, Color.RED));
        Assertions.assertEquals(0.0, ColorToMaterialMappings.colorDistance(Color.GREEN, Color.GREEN));
        Assertions.assertEquals(0.0, ColorToMaterialMappings.colorDistance(Color.BLUE, Color.BLUE));

        Assertions.assertNotEquals(0.0, ColorToMaterialMappings.colorDistance(Color.BLACK, Color.WHITE));
        Assertions.assertNotEquals(0.0, ColorToMaterialMappings.colorDistance(Color.WHITE, Color.BLACK));
        Assertions.assertNotEquals(0.0, ColorToMaterialMappings.colorDistance(Color.RED, Color.GREEN));
        Assertions.assertNotEquals(0.0, ColorToMaterialMappings.colorDistance(Color.RED, Color.BLUE));
        Assertions.assertNotEquals(0.0, ColorToMaterialMappings.colorDistance(Color.GREEN, Color.BLUE));

        Assertions.assertTrue(ColorToMaterialMappings.colorDistance(Color.RED, Color.ORANGE)
                < ColorToMaterialMappings.colorDistance(Color.RED, Color.GREEN));

        Assertions.assertTrue(ColorToMaterialMappings.colorDistance(Color.BLUE, Color.CYAN)
                < ColorToMaterialMappings.colorDistance(Color.BLUE, Color.RED));
    }

    @Test
    public void testClosestAvailableColor() {
        ColorToMaterialMappings colorToMaterialMappings = new ColorToMaterialMappings(ColorToMaterialMappings.DEFAULT);
        Assertions.assertEquals(Color.BLACK, colorToMaterialMappings.closestAvailableColor(Color.BLACK));
        Assertions.assertEquals(Color.WHITE, colorToMaterialMappings.closestAvailableColor(Color.WHITE));
        Assertions.assertEquals(Color.RED, colorToMaterialMappings.closestAvailableColor(Color.RED));

        Assertions.assertEquals(Color.BLACK, colorToMaterialMappings.closestAvailableColor(new Color(1,1,1)));
        Assertions.assertEquals(Color.WHITE, colorToMaterialMappings.closestAvailableColor(new Color(250,250,250)));
        Assertions.assertEquals(Color.RED, colorToMaterialMappings.closestAvailableColor(new Color(250,0,0)));

        Color osmOcean = new Color(170, 211, 223);
        Color osmLand = new Color(242, 239, 233);
        Assertions.assertNotEquals(colorToMaterialMappings.closestAvailableColor(osmOcean),
                colorToMaterialMappings.closestAvailableColor(osmLand)); // !!! VERY IMPORTANT CHECK !!!
    }
}
