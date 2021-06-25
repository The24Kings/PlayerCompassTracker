package compass.tracker.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TrackerTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> parameters = new ArrayList<>();

        if(args.length == 1) {
            parameters.add("cancel");
            parameters.add("enable");
        }
        if(args.length > 1 && args[0].equalsIgnoreCase("enable")) {
            parameters.add("mine_diamond_ore");
            parameters.add("enter_nether");
            parameters.add("enter_end");
            parameters.add("kill_all_hunters");
            parameters.add("kill_ender_dragon");
        }

        return parameters;
    }
}
