package gr.kendrick.flat_earth;

public class OSMCoordinates implements Coordinates {
    public int zoomLevel;
    public int osmTileX;
    public int osmTileY;
    public int pixelXWithinTile;
    public int pixelYWithinTile;

    public OSMCoordinates(int zoomLevel, int osmTileX, int osmTileY,
                          int pixelXWithinTile, int pixelYWithinTile) {
        this.zoomLevel = zoomLevel;
        this.osmTileX = osmTileX;
        this.osmTileY = osmTileY;
        this.pixelXWithinTile = pixelXWithinTile;
        this.pixelYWithinTile = pixelYWithinTile;
    }

    @Override
    public MinecraftCoordinates minecraft(int zoomLevel, int tileSizeInPx) {
        int x = this.osmTileX * tileSizeInPx + this.pixelXWithinTile;
        int y = this.osmTileY * tileSizeInPx + this.pixelYWithinTile;

        int noOfTilesAlongOneAxis = (int) Math.pow(2, zoomLevel);
        int totalWorldWidthInBlocks = tileSizeInPx * noOfTilesAlongOneAxis; // = also the total world height

        if (x < 0 || y < 0 || x >= totalWorldWidthInBlocks || y >= totalWorldWidthInBlocks) {
            return null;
        }

        int minecraftX = x - (totalWorldWidthInBlocks/2); // transforms (0, w) range into (-w/2, +w/2) range
        int minecraftZ = y - (totalWorldWidthInBlocks/2);

        return new MinecraftCoordinates(minecraftX, minecraftZ);
    }

    @Override
    public LatLongCoordinates latLong(int zoomLevel, int tileSizeInPx) {
        double x = this.osmTileX + (double)this.pixelXWithinTile / tileSizeInPx;
        double y = this.osmTileY + (double)this.pixelYWithinTile / tileSizeInPx;

        // cf. https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
        double longitude = (x / Math.pow(2, zoomLevel)) * 360.0 - 180.0;
        double latitude = Math.atan(Math.sinh(Math.PI - (y / Math.pow(2, zoomLevel)) * 2 * Math.PI)) * (180 / Math.PI);

        return new LatLongCoordinates(latitude, longitude);
    }

    @Override
    public OSMCoordinates osm(int zoomLevel, int tileSizeInPx) {
        return this;
    }
}
