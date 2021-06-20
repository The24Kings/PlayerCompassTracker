package compass.tracker.utils;

import compass.tracker.Compass;
import compass.tracker.CompassTracker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CountdownUtil {
    public static void huntCountdown() {
        Bukkit.getScheduler().runTaskAsynchronously(CompassTracker.getPlugin(), () -> {
            for (Player online : CompassTracker.getPlugin().getServer().getOnlinePlayers()) {
                online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Test")); //Says Deprecated but works :)
                for (int num = 10; num > 0; num--) {
                    int delay = 20 * (10 - num); //Calc the number of secs for countdown delay (Starts at 10 then subtracts 1 every iteration)
                    int finalNum = num; //Runnable wants a "final" esc int so num cannot be used
                    String count = String.valueOf(num);

                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> { //Maybe find a better way to switch colors?
                        if (Compass.getPrey() != null) { //Checks if Prey died during countdown
                            if (finalNum > 6) {
                                online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + count));
                            } else if (finalNum > 3) {
                                online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + count));
                            } else {
                                online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + count));
                            }
                            online.getWorld().playSound(online.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 10, 1);
                        }
                    }, delay);
                }
                Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
                    if (Compass.getPrey() != null) { //Checks if Prey died during countdown
                        online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "BEGIN"));
                        online.getWorld().playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
                    }
                }, 20 * 10); //Waits til count down is done
            }
        });
    }
}
