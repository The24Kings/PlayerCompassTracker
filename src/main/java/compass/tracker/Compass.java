package compass.tracker;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Compass {
    public static UUID prey = null;

    private static ItemStack getCompass(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();

        compassMeta.displayName(Component.text(ChatColor.GOLD + player.getName() + ChatColor.RESET + " Tracker"));

        compass.setItemMeta(compassMeta);

        return compass;
    }

    public static void giveCompass(Player hunter, Player prey) {
        if(hunter.getInventory().firstEmpty() == -1) {
            Location location = hunter.getLocation();
            World world = hunter.getWorld();

            world.dropItemNaturally(location, getCompass(prey));
        } else {
            hunter.getInventory().addItem(getCompass(prey));
        }

        hunter.sendMessage(ChatColor.DARK_GREEN + "Successfully tracked: " + ChatColor.RESET + prey.getName());
    }

    public static void addPrey(Player player) {
        prey = player.getUniqueId();
    }

    public static UUID getPrey() {
        return prey;
    }

    public static void clearInv() {
        ItemStack replace = new ItemStack(Material.AIR);
        Bukkit.getScheduler().runTaskAsynchronously(CompassTracker.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(!player.equals(Bukkit.getPlayer(Compass.getPrey()))) {
                    Inventory inventory = player.getInventory();
                    if (!inventory.isEmpty()) {
                        for (int item = 0; item < inventory.getSize(); item++) {
                            if (inventory.getItem(item) != null && inventory.getItem(item).getType().equals(Material.COMPASS)) {
                                inventory.setItem(item, replace);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void reset() {
        prey = null;
    }
}
