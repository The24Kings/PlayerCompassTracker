package compass.tracker.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TrackerTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        ArrayList<String> parameters = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            parameters.add(player.getName());
        }
        return parameters;
    }
}
