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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

//todo add a win/ loose condition (if prey dies, hunters win, if the prey meets a win condition, prey wins)
//todo on death give hunter back their compass

// TODO Win conditions - Reach the Nether, Reach the End, Kill the Ender Dragon, Kill all hunters at least once, Survive 24 days, Get a diamond

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
                        if (!event.getPlayer().equals(prey)) {
                            compassMeta.setLodestone(prey.getLocation());
                            event.getItem().setItemMeta(compassMeta);
                            player.sendMessage(ChatColor.DARK_GREEN + "Updated location of " + ChatColor.RESET + PlainComponentSerializer.plain().serialize(prey.displayName()));
                        } else player.sendMessage(ChatColor.RED + "Cannot track yourself!");
                    } else player.sendMessage(ChatColor.DARK_RED + "Could not find player!");
                } else CompassTracker.getPlugin().getLogger().severe(ChatColor.RED + "Error finding your prey, please try /hunt again!");
            }
        }
    }

    @EventHandler
    public void onHunterRespawn(PlayerRespawnEvent event) {
        if(Compass.getPrey() != null && !event.getPlayer().equals(Bukkit.getServer().getPlayer(Compass.getPrey()))) {
            Compass.giveCompass(event.getPlayer(), Bukkit.getServer().getPlayer(Compass.getPrey()));
        }
    }

    @EventHandler
    public void updateCompass(PlayerMoveEvent event) {
        ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
        int delay = 20 * 5; /* Seconds delay */

        if (held.getType().equals(Material.AIR) || !held.getItemMeta().hasDisplayName()) return;

        if (held.getType().equals(Material.COMPASS) && PlainComponentSerializer.plain().serialize(held.getItemMeta().displayName()).contains("Tracker")) {
            CompassMeta compassMeta = (CompassMeta) held.getItemMeta();
            PersistentDataContainer data = compassMeta.getPersistentDataContainer();

            if (data.has(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING)) {
                Player prey = Bukkit.getPlayerExact(data.get(new NamespacedKey(CompassTracker.getPlugin(), "player"), PersistentDataType.STRING));

                if (prey != null && prey.isOnline()) {
                    Bukkit.getScheduler().runTaskTimer(CompassTracker.getPlugin(), () -> { //Funky Lambda Laurie suggested
                        if (!event.getPlayer().equals(prey) && event.getTo() != event.getFrom()) {
                            compassMeta.setLodestone(prey.getLocation());
                            held.setItemMeta(compassMeta);
                            //CompassTracker.getPlugin().getLogger().info(ChatColor.GREEN + "Now pointing to: " + ChatColor.GOLD + prey.getName() + ChatColor.RESET + " at [" + prey.getLocation().getX() + ", " + prey.getLocation().getY() + ", " + prey.getLocation().getZ() + "]");
                        }
                    }, delay,0);
                } //else CompassTracker.getPlugin().getLogger().severe(ChatColor.DARK_RED + "Could not find player!");
            } else CompassTracker.getPlugin().getLogger().severe(ChatColor.RED + "Error finding your prey, please try /hunt again!");
        }
    }
}
