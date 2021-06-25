package compass.tracker.commands;

import compass.tracker.Compass;
import compass.tracker.GUIHandler;
import compass.tracker.utils.NickUtil;
import org.bukkit.ChatColor;
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
                NickUtil.resetPreyNick();
                Compass.reset();
                Compass.clearInv();
            }
            //TODO: Add enable number and set to 1-5 depending on which command the player sends
            if(args[0].equals("enable")) {
                if (args[1].equalsIgnoreCase("mine_diamond_ore")) {

                } else if (args[1].equalsIgnoreCase("enter_nether")) {

                } else if (args[1].equalsIgnoreCase("enter_end")) {

                } else if (args[1].equalsIgnoreCase("kill_all_hunters")) {

                } else if (args[1].equalsIgnoreCase("kill_ender_dragon")) {

                } else sender.sendMessage(ChatColor.RED + "The condition listed does not exist!");
            }
        }
        return true;
    }
}
