package compass.tracker;

import compass.tracker.utils.NickUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

public class WinConditions implements Listener {
    private static int condition = 0;

    public static void setCondition(int select) {
        condition = select;
    }

    public static int getCondition() {
        return condition;
    }

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

    //Announces a hunter's death
    public static void hunterDeath() {
        if(condition == 3) {
            if (Compass.getPrey() != null) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!player.getUniqueId().equals(Compass.getPrey())) {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.DARK_RED + "" + ChatColor.BOLD + "A hunter has died!"));
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 1);
                }
            }
        }
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
    public void preyDeath(PlayerDeathEvent event) {
        if (event.getEntity().equals(Bukkit.getServer().getPlayer(Compass.getPrey()))) {
            WinConditions.hunterWin();
        }
    }

    //Prey wins when they mine a diamond ore
    @EventHandler
    public void obtainDiamond(BlockBreakEvent event) {
        if(condition == 0) {
            if (Compass.getPrey() != null) {
                Player prey = Bukkit.getPlayer(Compass.getPrey());
                if (event.getPlayer().equals(prey)) {
                    if (event.getBlock().getBlockData().getMaterial().equals(Material.DIAMOND_ORE)) {
                        WinConditions.preyWin();
                    }
                }
            }
        }
    }

    //Prey wins when they reach the Nether/ End
    @EventHandler
    public void preyEnterNether(EntityPortalEvent event) {
        if(condition == 1 || condition == 2) {
            if (Compass.getPrey() != null) {
                Entity prey = Bukkit.getPlayer(Compass.getPrey());
                if (event.getEntity().equals(prey)) {
                    if (condition == 1) {
                        if (prey.getWorld().getEnvironment().equals(World.Environment.NETHER)) { //Check if entered Dimension is the Nether
                            Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), WinConditions::preyWin, 20);
                        }
                    }
                    if (condition == 2) {
                        if (prey.getWorld().getEnvironment().equals(World.Environment.THE_END)) { //Check if entered Dimension is the End
                            Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), WinConditions::preyWin, 20);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void hunterDeath(PlayerDeathEvent event) {
        if(condition == 3) {
            if(Compass.getPrey() != null) {
                if (Compass.getAliveHunters().isEmpty()) {
                    WinConditions.preyWin();
                }
                if (!event.getEntity().equals(Bukkit.getPlayer(Compass.getPrey()))) {
                    UUID hunter = event.getEntity().getUniqueId();
                    if (Compass.getAliveHunters().contains(hunter)) {
                        Compass.removeAliveHunter(hunter);
                    }
                }
            }
        }
    }

    //Prey wins if they kill the Ender Dragon
    //TODO: Test if this works, i have no clue...
    @EventHandler
    public void killEnderDragon(EntityDamageByEntityEvent event) {
        if(condition == 4) {
            if (Compass.getPrey() != null) {
                Entity prey = Bukkit.getPlayer(Compass.getPrey());
                if (event.getDamager() instanceof Player) {
                    if (event.getEntity() instanceof EnderDragon) {
                        if (event.getDamager().equals(prey)) {
                            if (event.getDamage() > ((EnderDragon) event.getEntity()).getHealth()) {
                                WinConditions.preyWin();
                            }
                        }
                    }
                }
            }
        }
    }
}
