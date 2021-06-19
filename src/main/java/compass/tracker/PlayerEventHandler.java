package compass.tracker;

import compass.tracker.utils.NickUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

// TODO Win conditions - Reach the Nether, Reach the End, Kill the Ender Dragon, Kill all hunters at least once, Get a diamond
public class PlayerEventHandler implements Listener {
    @EventHandler
    public void getPreyY(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (!event.hasItem() || !event.getItem().getItemMeta().hasDisplayName()) return;
            Player player = event.getPlayer();
            Player prey = Bukkit.getPlayer(Compass.getPrey());

            if (prey != null && prey.isOnline()) {
                player.sendMessage(ChatColor.DARK_GREEN + "Current Y" + ChatColor.RESET + ": " + (int) Math.floor(prey.getLocation().getY()));
            } else player.sendMessage(ChatColor.DARK_RED + "Could not find player!");
        }
    }

    //Updates Prey's location while Prey is moving
    @EventHandler
    public void updateCompass(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Player prey = Bukkit.getPlayer(Compass.getPrey());
        if (prey != null && prey.isOnline()) {
            if(prey.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                player.setCompassTarget(Bukkit.getPlayer(Compass.getPrey()).getLocation());
            }
        }
    }

    //Doesn't allow Prey to pickup Compass
    @EventHandler
    public void compassPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if(event.getPlayer().equals(Bukkit.getPlayer(Compass.getPrey()))) {
            if(item.getType().equals(Material.COMPASS) && item.getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void hunterRespawn(PlayerRespawnEvent event) {
        if(Compass.getPrey() != null && !event.getPlayer().equals(Bukkit.getServer().getPlayer(Compass.getPrey()))) {
            Compass.giveCompass(event.getPlayer(), Bukkit.getPlayer(Compass.getPrey()));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(Compass.getPrey() != null && event.getEntity().equals(Bukkit.getServer().getPlayer(Compass.getPrey()))) {
            NickUtil.resetPreyNick(Bukkit.getServer().getPlayer(Compass.getPrey()));
            Compass.reset();
            Compass.clearInv();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                        ChatColor.GOLD + "" + ChatColor.BOLD + "HUNTERS WIN",
                        "",
                        10,
                        20*3,
                        10);
            }
        }
    }
}
