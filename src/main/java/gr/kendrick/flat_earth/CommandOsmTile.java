package gr.kendrick.flat_earth;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOsmTile implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Location location = player.getLocation();
                MinecraftCoordinates minecraftCoords = new MinecraftCoordinates(location.getBlockX(), location.getBlockZ());
                int zoomLevel = App.config.getInt("osm_zoom_level");
                int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
                OSMCoordinates osmCoordinates = minecraftCoords.osm(zoomLevel, tileSizeInPx);
                int z = osmCoordinates.zoomLevel;
                int x = osmCoordinates.osmTileX;
                int y = osmCoordinates.osmTileY;
                player.sendMessage("You're currently inside OSM tile " + z + "/" + x + "/" + y);
                return true;
            }
        }
        return false;
    }
}
