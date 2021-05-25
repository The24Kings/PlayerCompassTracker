package compass.tracker.commands;

import compass.tracker.Compass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrackerCommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("hunt") && args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (Bukkit.getOnlinePlayers().contains(target)) {
                Compass.giveCompass(player, target);
            } else player.sendMessage(ChatColor.RED + "Target is not Online...");
        } else player.sendMessage(ChatColor.RED + "Please include a player's name!");
        return true;
    }
}
