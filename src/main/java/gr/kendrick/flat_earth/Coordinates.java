package gr.kendrick.flat_earth;

public interface Coordinates {
    MinecraftCoordinates minecraft(int zoomLevel, int tileSizeInPx);
    LatLongCoordinates latLong(int zoomLevel, int tileSizeInPx);
    OSMCoordinates osm(int zoomLevel, int tileSizeInPx);
}
