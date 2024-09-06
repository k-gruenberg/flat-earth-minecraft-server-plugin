package gr.kendrick.flat_earth;

// cf. https://www.spigotmc.org/wiki/create-a-simple-command/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWhereIs implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) { // /whereis <player>
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage("Player " + playerName + " not found.");
        } else {
            Location location = player.getLocation();
            MinecraftCoordinates minecraftCoords = new MinecraftCoordinates(location.getBlockX(), location.getBlockZ());
            int zoomLevel = App.config.getInt("osm_zoom_level");
            int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
            LatLongCoordinates latLongCoords = minecraftCoords.latLong(zoomLevel, tileSizeInPx);
            if (latLongCoords == null) {
                sender.sendMessage("Player " + player.getName() + " is currently outside the world map.");
            } else {
                String placeName = NominatimAPI.reverseGeocode(latLongCoords.latitude, latLongCoords.longitude);
                sender.sendMessage("Player " + player.getName() + " is currently @\n" +
                        "Place name: " + placeName + "\n" +
                        "Latitude: " + latLongCoords.latitude + "\n" +
                        "Longitude: " + latLongCoords.longitude);
            }
        }

        return true;

    }
}
