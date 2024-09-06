package gr.kendrick.flat_earth;

public class LatLongCoordinates implements Coordinates {
    public double latitude;
    public double longitude;

    public LatLongCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public MinecraftCoordinates minecraft(int zoomLevel, int tileSizeInPx) {
        OSMCoordinates osmCoordinates = this.osm(zoomLevel, tileSizeInPx);
        if (osmCoordinates == null) {
            return null;
        } else {
            return osmCoordinates.minecraft(zoomLevel, tileSizeInPx);
        }
    }

    @Override
    public LatLongCoordinates latLong(int zoomLevel, int tileSizeInPx) {
        return this;
    }

    @Override
    public OSMCoordinates osm(int zoomLevel, int tileSizeInPx) {
        // cf. https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
        double x = ((this.longitude + 180.0) / 360.0) * Math.pow(2, zoomLevel);
        double y = (1.0 - Math.log(Math.tan(this.latitude * (Math.PI/180.0)) + 1/Math.cos(this.latitude * (Math.PI/180.0))) / Math.PI)
                * Math.pow(2, zoomLevel-1);

        int osmTileX = (int)Math.floor(x);
        int osmTileY = (int)Math.floor(y);

        int pixelXWithinTile = (int)Math.round(tileSizeInPx * (x - osmTileX));
        int pixelYWithinTile = (int)Math.round(tileSizeInPx * (y - osmTileY));

        return new OSMCoordinates(zoomLevel, osmTileX, osmTileY, pixelXWithinTile, pixelYWithinTile);
    }
}
