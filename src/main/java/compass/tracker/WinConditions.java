package compass.tracker;

import compass.tracker.utils.NickUtil;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.ArrayList;
import java.util.UUID;

//TODO: Add in a toggle system for win conditions
public class WinConditions implements Listener {

    public static void hunterWin() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(Compass.getPrey() != null && player.getUniqueId().equals(Compass.getPrey())) { //Shouldn't be null but internal error could occur
                player.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                        ChatColor.RED + "" + ChatColor.BOLD + "HUNTERS WIN",
                        "",
                        10,
                        20 * 3,
                        10);
            } else {
                player.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                        ChatColor.GOLD + "" + ChatColor.BOLD + "HUNTERS WIN",
                        "",
                        10,
                        20 * 3,
                        10);
            }
            player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 10, 1);
        }
        NickUtil.resetPreyNick();
        Compass.reset();
        Compass.clearInv();
    }

    public static void preyWin() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if(Compass.getPrey() != null && player.getUniqueId().equals(Compass.getPrey())) { //Shouldn't be null but internal error could occur
                player.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                        ChatColor.GOLD + "" + ChatColor.BOLD + "PREY WINS",
                        "",
                        10,
                        20 * 3,
                        10);
            } else {
                player.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                        ChatColor.RED + "" + ChatColor.BOLD + "PREY WINS",
                        "",
                        10,
                        20 * 3,
                        10);
            }
        }
        NickUtil.resetPreyNick();
        Compass.reset();
        Compass.clearInv();
    }

    //Called when new prey is chosen
    public static ArrayList<UUID> aliveHunters() {
        ArrayList<UUID> lives = new ArrayList<>();
        if(Compass.getPrey() != null) {
            Player prey = Bukkit.getPlayer(Compass.getPrey());
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (!player.equals(prey)) {
                    lives.add(player.getUniqueId());
                }
            }
        }
        return lives;
    }

    //Hunters win if Prey dies
    @EventHandler
    public void onPreyDeath(PlayerDeathEvent event) {
        if(event.getEntity().equals(Bukkit.getServer().getPlayer(Compass.getPrey()))) {
            WinConditions.hunterWin();
        }
    }

    //Prey wins when they mine a diamond ore
    @EventHandler
    public void obtainDiamond(BlockBreakEvent event) {
        if(Compass.getPrey() != null) {
            Player prey = Bukkit.getPlayer(Compass.getPrey());
            if(event.getPlayer().equals(prey)) {
                if(event.getBlock().getBlockData().getMaterial().equals(Material.DIAMOND_ORE)) {
                    WinConditions.preyWin();
                }
            }
        }
    }

    //Prey wins when they reach the Nether/ End
    @EventHandler
    public void preyEnterNether(EntityPortalEvent event) {
        if(Compass.getPrey() != null) {
            Entity prey = Bukkit.getPlayer(Compass.getPrey());
            if(event.getEntity().equals(prey)) {
                if(prey.getWorld().getEnvironment().equals(World.Environment.NETHER)) { //Check if entered Dimension is the Nether
                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), WinConditions::preyWin,20);
                }
                if(prey.getWorld().getEnvironment().equals(World.Environment.THE_END)) { //Check if entered Dimension is the End
                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), WinConditions::preyWin,20);
                }
            }
        }
    }

    //Prey wins if they kill the Ender Dragon
    //TODO: Test if this works, i have no clue...
    @EventHandler
    public void killEnderDragon(EntityDamageByEntityEvent event) {
        if(Compass.getPrey() != null) {
            Entity prey = Bukkit.getPlayer(Compass.getPrey());
            if(event.getDamager() instanceof Player) {
                if (event.getEntity() instanceof EnderDragon) {
                    if(event.getDamager().equals(prey)) {
                        if (event.getDamage() > ((EnderDragon) event.getEntity()).getHealth()) {
                            WinConditions.preyWin();
                        }
                    }
                }
            }
        }
    }
}
