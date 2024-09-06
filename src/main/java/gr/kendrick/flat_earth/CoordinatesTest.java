package gr.kendrick.flat_earth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordinatesTest {
    final int tileSizeInPx = 256;

    @Test
    public void testLatLongToOSM() {
        // __________
        // |        |
        // |        |
        // ----------
        int zoomLevel = 0;
        LatLongCoordinates latLongCoordinates = new LatLongCoordinates(0.0, 0.0); // Null island
        OSMCoordinates osmCoordinates = latLongCoordinates.osm(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0, osmCoordinates.osmTileX); // for zoomLevel=0, there's only 1 tile: (0,0,0)
        Assertions.assertEquals(0, osmCoordinates.osmTileY); // for zoomLevel=0, there's only 1 tile: (0,0,0)
        Assertions.assertEquals(tileSizeInPx/2, osmCoordinates.pixelXWithinTile);
        Assertions.assertEquals(tileSizeInPx/2, osmCoordinates.pixelYWithinTile);

        // ___________________
        // |        |        |
        // |        |        |
        // ---------+---------
        // |        |        |
        // |        |        |
        // -------------------
        zoomLevel = 1;
        latLongCoordinates = new LatLongCoordinates(0.0, 0.0); // Null island
        osmCoordinates = latLongCoordinates.osm(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(1, osmCoordinates.osmTileX);
        Assertions.assertEquals(1, osmCoordinates.osmTileY);
        Assertions.assertEquals(0, osmCoordinates.pixelXWithinTile);
        Assertions.assertEquals(0, osmCoordinates.pixelYWithinTile);
    }

    @Test
    public void testOSMToLatLong() {
        // __________
        // |        |
        // |        |
        // ----------
        int zoomLevel = 0;
        OSMCoordinates osmCoordinates = new OSMCoordinates(zoomLevel, 0, 0, tileSizeInPx/2, tileSizeInPx/2);
        LatLongCoordinates latLongCoordinates = osmCoordinates.latLong(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0.0, latLongCoordinates.latitude);
        Assertions.assertEquals(0.0, latLongCoordinates.longitude);

        // ___________________
        // |        |        |
        // |        |        |
        // ---------+---------
        // |        |        |
        // |        |        |
        // -------------------
        zoomLevel = 1;
        osmCoordinates = new OSMCoordinates(zoomLevel, 1, 1, 0, 0);
        latLongCoordinates = osmCoordinates.latLong(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0.0, latLongCoordinates.latitude);
        Assertions.assertEquals(0.0, latLongCoordinates.longitude);
    }

    @Test
    public void testMinecraftToOSM() {
        // __________
        // |        |
        // |        |
        // ----------
        int zoomLevel = 0;
        MinecraftCoordinates minecraftCoordinates = new MinecraftCoordinates(0, 0);
        OSMCoordinates osmCoordinates = minecraftCoordinates.osm(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0, osmCoordinates.osmTileX); // for zoomLevel=0, there's only 1 tile: (0,0,0)
        Assertions.assertEquals(0, osmCoordinates.osmTileY); // for zoomLevel=0, there's only 1 tile: (0,0,0)
        Assertions.assertEquals(tileSizeInPx/2, osmCoordinates.pixelXWithinTile);
        Assertions.assertEquals(tileSizeInPx/2, osmCoordinates.pixelYWithinTile);

        // ___________________
        // |        |        |
        // |        |        |
        // ---------+---------
        // |        |        |
        // |        |        |
        // -------------------
        zoomLevel = 1;
        minecraftCoordinates = new MinecraftCoordinates(0, 0);
        osmCoordinates = minecraftCoordinates.osm(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(1, osmCoordinates.osmTileX);
        Assertions.assertEquals(1, osmCoordinates.osmTileY);
        Assertions.assertEquals(0, osmCoordinates.pixelXWithinTile);
        Assertions.assertEquals(0, osmCoordinates.pixelYWithinTile);
    }

    @Test
    public void testOSMToMinecraft() {
        // __________
        // |        |
        // |        |
        // ----------
        int zoomLevel = 0;
        OSMCoordinates osmCoordinates = new OSMCoordinates(zoomLevel, 0, 0, tileSizeInPx/2, tileSizeInPx/2);
        MinecraftCoordinates minecraftCoordinates = osmCoordinates.minecraft(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0, minecraftCoordinates.minecraftX);
        Assertions.assertEquals(0, minecraftCoordinates.minecraftZ);

        // ___________________
        // |        |        |
        // |        |        |
        // ---------+---------
        // |        |        |
        // |        |        |
        // -------------------
        zoomLevel = 1;
        osmCoordinates = new OSMCoordinates(zoomLevel, 1, 1, 0, 0);
        minecraftCoordinates = osmCoordinates.minecraft(zoomLevel, tileSizeInPx);
        Assertions.assertEquals(0, minecraftCoordinates.minecraftX);
        Assertions.assertEquals(0, minecraftCoordinates.minecraftZ);
    }
}
