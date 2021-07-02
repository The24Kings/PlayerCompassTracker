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

                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
                        if (Compass.getPrey() != null) { //Checks if Prey died during countdown
                            setColor(online, ChatColor.GREEN, count, finalNum <= 10 && finalNum >= 7);
                            setColor(online, ChatColor.YELLOW, count, finalNum <= 6 && finalNum >= 4);
                            setColor(online, ChatColor.RED, count, finalNum <= 3);

                            online.getWorld().playSound(online.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 5, 1);
                        }
                    }, delay);
                }
                Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
                    if (Compass.getPrey() != null) { //Checks if Prey died during countdown
                        online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "BEGIN"));
                        online.getWorld().playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 1);
                    }
                }, 20 * 10); //Waits til count down is done
            }
        });
    }

    public static void setColor(Player online, ChatColor color, String count, boolean condition) {
        if(condition) {
            online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color + count));
        }
    }
}
