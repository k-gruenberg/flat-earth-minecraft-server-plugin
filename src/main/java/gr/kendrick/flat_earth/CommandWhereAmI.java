package gr.kendrick.flat_earth;

// cf. https://www.spigotmc.org/wiki/create-a-simple-command/

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWhereAmI implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Location location = player.getLocation();
                MinecraftCoordinates minecraftCoords = new MinecraftCoordinates(location.getBlockX(), location.getBlockZ());
                int zoomLevel = App.config.getInt("osm_zoom_level");
                int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
                LatLongCoordinates latLongCoords = minecraftCoords.latLong(zoomLevel, tileSizeInPx);
                player.sendMessage("You're currently @ lat=" + latLongCoords.latitude +
                        ", lon=" + latLongCoords.longitude); // ToDo: use reverse search API and print name of place
                return true;
            }
        }
        return false;
    }
}
