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
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

//TODO Change this to check when the prey is moving and hunter is holding a compass to update the CompassMeta
public class PlayerEventHandler implements Listener {
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
}
