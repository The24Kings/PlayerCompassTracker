package compass.tracker;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.UUID;

public class Compass {
    public static UUID prey = null;

    private static ItemStack getCompass(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();

        //Adds 'player' custom tag to the compass for currently tracked player (Thanks https://github.com/Rayn322)
        PersistentDataContainer data = compassMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING, player.getName());

        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(player.getLocation());
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
        Bukkit.getScheduler().runTaskAsynchronously(CompassTracker.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getInventory();
                inventory.clear();
            }
        });
    }
    public static void reset() {
        prey = null;
    }
}
