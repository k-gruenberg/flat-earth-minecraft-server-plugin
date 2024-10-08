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
        if (args.length > 0) { // /whereami
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            MinecraftCoordinates minecraftCoords = new MinecraftCoordinates(location.getBlockX(), location.getBlockZ());
            int zoomLevel = App.config.getInt("osm_zoom_level");
            int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
            LatLongCoordinates latLongCoords = minecraftCoords.latLong(zoomLevel, tileSizeInPx);
            if (latLongCoords == null) {
                player.sendMessage("You're currently outside the world map.");
            } else {
                String placeName = NominatimAPI.reverseGeocode(latLongCoords.latitude, latLongCoords.longitude);
                player.sendMessage("You're currently @\n" +
                        "Place name: " + placeName + "\n" +
                        "Latitude: " + latLongCoords.latitude + "\n" +
                        "Longitude: " + latLongCoords.longitude);
            }
        } else {
            System.out.println("Only players may use this command.");
        }

        return true;
    }
}
