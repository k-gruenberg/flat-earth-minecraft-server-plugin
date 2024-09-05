package gr.kendrick.flat_earth;

// cf. https://www.spigotmc.org/wiki/create-a-simple-command/

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpPlace implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String placeName = String.join(" ", args);
                // ToDo: use place search API, then teleport to coordinates
                return true;
            }
        }
        return false;
    }
}