package gr.kendrick.flat_earth;

import java.awt.*;

public class MinecraftCoordinates implements Coordinates {
    public final int minecraftX;
    public final int minecraftZ;

    // Minecraft coordinates:
    //
    //            NORTH = -Z
    //                /|\
    //                 |
    // WEST = -X <-----+-----> +X = EAST
    //                 |
    //                \|/
    //            SOUTH = +Z
    //

    public MinecraftCoordinates(int minecraftX, int minecraftZ) {
        this.minecraftX = minecraftX;
        this.minecraftZ = minecraftZ;
    }

    @Override
    public MinecraftCoordinates minecraft(int zoomLevel, int tileSizeInPx) {
        return this;
    }

    @Override
    public LatLongCoordinates latLong(int zoomLevel, int tileSizeInPx) {
        OSMCoordinates osmCoordinates = this.osm(zoomLevel, tileSizeInPx);
        if (osmCoordinates == null) {
            return null;
        } else {
            return osmCoordinates.latLong(zoomLevel, tileSizeInPx);
        }
    }

    @Override
    public OSMCoordinates osm(int zoomLevel, int tileSizeInPx) {
        // For zoom level 1 there are 4 tiles (z,x,y given):
        // ___________________________________
        // |  (1,0,0)        |  (1,1,0)      |
        // |  North America  |  Asia         |
        // |                 |               |
        // |-----------------+----------------  // <--- we want the Minecraft origin (0,0) to be at the little +
        // |  (1,0,1)        |  (1,1,1)      |
        // |  South America  |  Australia    |
        // |                 |               |
        // |__________________________________

        int noOfTilesAlongOneAxis = (int) Math.pow(2, zoomLevel);
        // zoom level | noOfTilesAlongOneAxis
        // -----------+----------------------
        //  0         | 1
        //  1         | 2 (see above)
        //  2         | 4
        // ...        | ...
        // 20         | 1.048.576
        // => cf. https://wiki.openstreetmap.org/wiki/Zoom_levels

        int totalWorldWidthInBlocks = tileSizeInPx * noOfTilesAlongOneAxis; // = also the total world height

        // Transform (minecraftX, minecraftZ) coordinates:
        //                   |
        //                   |
        //  -----------------+-----------------> +x
        //                   |
        //                  \|/
        //                  +z
        //
        // into a (x,y) coordinate system with non-negative coordinates and the origin in the upper-left corner:
        //  +----------------------------------> +x
        //  |
        //  |
        // \|/
        // +y
        int x = (totalWorldWidthInBlocks/2) + this.minecraftX; // transforms (-w/2, +w/2) range into (0, w) range
        int y = (totalWorldWidthInBlocks/2) + this.minecraftZ;

        if (x < 0 || y < 0 || x >= totalWorldWidthInBlocks || y >= totalWorldWidthInBlocks) {
            return null;
        }

        int osmTileX = x / tileSizeInPx;
        int osmTileY = y / tileSizeInPx;
        int pixelXWithinTile = x % tileSizeInPx;
        int pixelYWithinTile = y % tileSizeInPx;

        return new OSMCoordinates(zoomLevel, osmTileX, osmTileY, pixelXWithinTile, pixelYWithinTile);
    }
}
