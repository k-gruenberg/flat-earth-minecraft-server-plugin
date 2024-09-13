package gr.kendrick.flat_earth;

import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class App extends JavaPlugin {
    public static FileConfiguration config;
    public static ColorToMaterialMappings colorToMaterialMappings;

    @Override
    public void onEnable() {
        getLogger().info("Enabling FlatEarth plugin...");

        this.getCommand("tpcoords").setExecutor(new CommandTpCoords());
        this.getCommand("tpplace").setExecutor(new CommandTpPlace());
        this.getCommand("whereami").setExecutor(new CommandWhereAmI());
        this.getCommand("whereis").setExecutor(new CommandWhereIs());
        this.getCommand("osmtile").setExecutor(new CommandOsmTile());

        config = this.getConfig();
        // OSM zoom level 17 has 1.194 meters per pixel (on the Equator)
        // and therefore most closely creates a 1:1 scale.
        // Note that this is only true for 256-pixel wide tiles and at
        // the Equator. For other latitudes, multiply by the cosine of
        // the latitude, for 45° this would be:
        // 1.194 * cos(45°) = 1.194 * 0.707 = 0.844 meters per pixel
        // => see: https://wiki.openstreetmap.org/wiki/Zoom_levels
        config.addDefault("osm_zoom_level", 17);
        // For more tile server URLs,
        // see: https://wiki.openstreetmap.org/wiki/Raster_tile_providers
        config.addDefault("osm_tile_server_url", "https://a.tile.openstreetmap.de/${z}/${x}/${y}.png");
        config.addDefault("nominatim_url", "https://nominatim.openstreetmap.org/search?q={QUERY}&format=xml&limit=1");
        config.addDefault("nominatim_reverse_url", "https://nominatim.openstreetmap.org/reverse?format=xml&lat={lat}&lon={long}&zoom=5&addressdetails=0");
        config.addDefault("osm_tile_size_in_px", 256);
        config.addDefault("user_agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
        config.addDefault("user_agent_nominatim", "Mozilla/5.0 (Java HttpURLConnection; FlatEarth Minecraft server plugin)");
        config.addDefault("world_name", "flat_earth");
        config.addDefault("color_to_material_mappings", ColorToMaterialMappings.DEFAULT);
        config.addDefault("ground_level", 64);
        config.addDefault("underground_block", "STONE");
        config.options().copyDefaults(true);
        saveConfig();

        colorToMaterialMappings = new ColorToMaterialMappings(
                Optional.ofNullable(config.getString("color_to_material_mappings"))
                        .orElse(ColorToMaterialMappings.DEFAULT)
        );

        String worldName = Optional.ofNullable(config.getString("world_name")).orElse("flat_earth");
        int osmZoomLevel = config.getInt("osm_zoom_level");
        getLogger().info("Creating world '" + worldName + "' with OSM zoom level " + osmZoomLevel + " ...");

        WorldCreator worldCreator = new WorldCreator(worldName);
        worldCreator.generator(new FlatEarthWorldGenerator(
                osmZoomLevel,
                config.getString("osm_tile_server_url"),
                config.getInt("osm_tile_size_in_px"),
                config.getString("user_agent")
        ));
        getServer().createWorld(worldCreator);

        getLogger().info("FlatEarth plugin enabled.");
    }
    @Override
    public void onDisable() {
        getLogger().info("FlatEarth plugin disabled.");
    }
}