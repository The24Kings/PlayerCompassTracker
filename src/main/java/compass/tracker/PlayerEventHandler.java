package compass.tracker;

import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

//TODO Change this to check every tick for when the prey is moving while the hunter is holding a compass to update the CompassMeta
public class PlayerEventHandler implements Listener {
    /*
    @EventHandler
    public void onUpdateCompass(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (!event.hasItem() || !event.getItem().getItemMeta().hasDisplayName()) return;

            if (event.getMaterial() == Material.COMPASS && PlainComponentSerializer.plain().serialize(event.getItem().getItemMeta().displayName()).contains("Tracker")) {
                CompassMeta compassMeta = (CompassMeta) event.getItem().getItemMeta();
                PersistentDataContainer data = compassMeta.getPersistentDataContainer();
                Player player = event.getPlayer();

                if (data.has(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING)) {
                    Player prey = Bukkit.getPlayerExact(data.get(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING));

                    if (prey != null && prey.isOnline()) {
                        compassMeta.setLodestone(prey.getLocation());
                        event.getItem().setItemMeta(compassMeta);
                        player.sendMessage(ChatColor.DARK_GREEN + "Updated location of " + ChatColor.RESET + PlainComponentSerializer.plain().serialize(prey.displayName()));
                    } else player.sendMessage(ChatColor.DARK_RED + "Could not find player!");
                } else player.sendMessage(ChatColor.RED + "Error finding your prey, please try /hunt again!");
            }
        }
    }
*/
    @EventHandler
    public void upDateCompass(PlayerMoveEvent event) {
        if (event.getPlayer().getActiveItem().equals(new ItemStack(Material.AIR)) || !event.getPlayer().getActiveItem().getItemMeta().hasDisplayName()) return;

        //First IF statement throws null pointer exception
        if (event.getPlayer().getActiveItem().equals(new ItemStack(Material.COMPASS)) && PlainComponentSerializer.plain().serialize(event.getPlayer().getActiveItem().getItemMeta().displayName()).contains("Tracker")) {
            CompassTracker.getPlugin().getLogger().warning("made is past first if statement"); //log
            CompassMeta compassMeta = (CompassMeta) event.getPlayer().getActiveItem().getItemMeta();
            PersistentDataContainer data = compassMeta.getPersistentDataContainer();
            Player player = event.getPlayer();

            if (data.has(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING)) {
                CompassTracker.getPlugin().getLogger().warning("made is past second if statement"); //log
                Player prey = Bukkit.getPlayerExact(data.get(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING));

                if (prey != null && prey.isOnline() && (event.getPlayer().equals(prey) && event.getTo() != event.getFrom())) {
                    CompassTracker.getPlugin().getLogger().warning("made is past third if statement"); //log
                    compassMeta.setLodestone(prey.getLocation());
                    event.getPlayer().getActiveItem().setItemMeta(compassMeta);
                } else player.sendMessage(ChatColor.DARK_RED + "Could not find player!");
            } else player.sendMessage(ChatColor.RED + "Error finding your prey, please try /hunt again!");
        }
    }
}
