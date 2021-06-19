package compass.tracker.utils;

import compass.tracker.CompassTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CountdownUtil {
    public static void huntCountdown() {
        Bukkit.getScheduler().runTaskAsynchronously(CompassTracker.getPlugin(), () -> {
            for (Player online : CompassTracker.getPlugin().getServer().getOnlinePlayers()) {
                for (int num = 10; num > 0; num--) {
                    int delay = 20 * (10 - (num));
                    int finalNum = num;
                    String count = String.valueOf(num);

                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> { //Maybe find a better way to switch colors?
                        if(finalNum > 6) {
                            online.sendTitle(ChatColor.GREEN + count, "", 2, 20, 2);
                        } else if(finalNum > 3) {
                            online.sendTitle(ChatColor.YELLOW + count, "", 2, 20, 2);
                        } else {
                            online.sendTitle(ChatColor.RED + count, "", 2, 20, 2);
                        }
                    }, delay);
                }
                Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
                    online.sendTitle(ChatColor.GOLD + "BEGIN", "", 2, 20, 2);
                }, 20 * 10);
            }
        });
    }
}
