package compass.tracker.commands;

import compass.tracker.Compass;
import compass.tracker.GUIHandler;
import compass.tracker.WinConditions;
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

            if(args[0].equals("enable")) {
                if (args[1].equalsIgnoreCase("mine_diamond_ore")) {
                    WinConditions.setCondition(0);
                } else if (args[1].equalsIgnoreCase("enter_nether")) {
                    WinConditions.setCondition(1);
                } else if (args[1].equalsIgnoreCase("enter_end")) {
                    WinConditions.setCondition(2);
                } else if (args[1].equalsIgnoreCase("kill_all_hunters")) {
                    WinConditions.setCondition(3);
                } else if (args[1].equalsIgnoreCase("kill_ender_dragon")) {
                    WinConditions.setCondition(4);
                } else sender.sendMessage(ChatColor.RED + "The condition listed does not exist!");
            }
        }
        return true;
    }
}
