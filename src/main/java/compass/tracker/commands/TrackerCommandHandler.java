package compass.tracker.commands;

import compass.tracker.Compass;
import compass.tracker.GUIHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackerCommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0 && label.equalsIgnoreCase("hunt")) {
            Compass.reset();
            GUIHandler.openGUI(player);
        }

        if(args.length != 0) {
            if(args[0].equals("cancel")) {
                Compass.reset();
            }
        }
        return true;
    }
}
