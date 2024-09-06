package gr.kendrick.flat_earth;

// cf. https://www.spigotmc.org/wiki/create-a-simple-command/

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpPlace implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) { // /tpplace <place_name>
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String placeName = String.join(" ", args);
            LatLongCoordinates placeCoordinates = NominatimAPI.search(placeName);
            if (placeCoordinates == null) {
                player.sendMessage("Error locating place '" + placeName + "'");
            } else {
                // Teleport to place coordinates:
                int zoomLevel = App.config.getInt("osm_zoom_level");
                int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
                MinecraftCoordinates minecraftCoords = placeCoordinates.minecraft(zoomLevel, tileSizeInPx);
                World world = player.getWorld(); // assume that the player is already in the correct world
                Location location = new Location(world, minecraftCoords.minecraftX, 66, minecraftCoords.minecraftZ);
                player.teleport(location);
            }
        } else {
            System.out.println("Only players may use this command.");
        }

        return true;
    }
}