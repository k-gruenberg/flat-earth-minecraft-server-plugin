package gr.kendrick.flat_earth;

// cf. https://www.spigotmc.org/wiki/create-a-simple-command/

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandTpCoords implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) { // /tpcoords <latitude> <longitude>
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                double latitude = Double.parseDouble(args[0]);
                double longitude = Double.parseDouble(args[1]);
                LatLongCoordinates latLongCoords = new LatLongCoordinates(latitude, longitude);
                int zoomLevel = App.config.getInt("osm_zoom_level");
                int tileSizeInPx = App.config.getInt("osm_tile_size_in_px");
                MinecraftCoordinates minecraftCoords = latLongCoords.minecraft(zoomLevel, tileSizeInPx);
                World world = player.getWorld(); // assume that the player is already in the correct world
                Location location = new Location(world, minecraftCoords.minecraftX, 66, minecraftCoords.minecraftZ);
                player.teleport(location);
            } catch (NumberFormatException ex) {
                player.sendMessage("Invalid latitude/longitude value(s).");
                ex.printStackTrace();
            }
        } else {
            System.out.println("Only players may use this command.");
        }

        return true;
    }
}
