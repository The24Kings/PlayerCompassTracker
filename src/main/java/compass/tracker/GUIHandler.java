package compass.tracker;

import compass.tracker.utils.CountdownUtil;
import compass.tracker.utils.EffectsUtil;
import compass.tracker.utils.NickUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

//todo add pages to the list of player to increase to allotted amount of players
public class GUIHandler implements Listener {
    private static final Component guiName = Component.text("Who would you like to hunt?");

    public static void openGUI(Player player) {
        int playerAmount = player.getWorld().getPlayers().size();
        int guiSize;

        // Sets gui to the correct multiple of 9 (Limit 54)
        if (playerAmount <= 54) {
            guiSize = playerAmount + (9 - playerAmount % 9); //Adds how many more players til full row of 9
        } else {
            player.sendMessage("Too many players!");
            return;
        }

        Inventory gui = Bukkit.createInventory(player, guiSize, guiName);

        //Run task asynchronously to reduce lag on larger player sets
        Bukkit.getScheduler().runTaskAsynchronously(CompassTracker.getPlugin(), () -> {
            for (Player playerI : player.getWorld().getPlayers()) {
                if (!playerI.getName().equals(player.getName()) && playerI.getGameMode().equals(GameMode.SURVIVAL) && !playerI.isDead()) { //Does not add Prey, Non-survival players and dead players
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta headMeta = (SkullMeta) head.getItemMeta();

                    headMeta.displayName(Component.text(playerI.getName()));
                    headMeta.setOwningPlayer(playerI);
                    head.setItemMeta(headMeta);
                    gui.addItem(head);
                }
            }
        });
        if(NickUtil.getNickedPrey() != null) {
            NickUtil.resetPreyNick();
        }
        player.openInventory(gui);
        Compass.clearInv();
    }

    @EventHandler
    public static void onMenuClick(InventoryClickEvent event) {
        if (event.getView().title().equals(guiName) && event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();

            if (event.getClickedInventory().getType().equals(InventoryType.CHEST) && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                try {
                    Player clickedPlayer = Bukkit.getPlayer(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()));
                    for(Player online : Bukkit.getServer().getOnlinePlayers()) {
                        if(!online.equals(clickedPlayer)) {
                            Compass.giveCompass(online, clickedPlayer);
                        }
                    }
                    assert clickedPlayer != null;
                    NickUtil.setPreyNick(clickedPlayer);
                    EffectsUtil.startHunt(clickedPlayer);
                    Compass.addPrey(clickedPlayer);
                    clickedPlayer.sendTitle( //(fadeIn, stay, fadeOut) - in ticks
                            ChatColor.RED + "You have been Chosen...",
                            ChatColor.DARK_RED + "" + ChatColor.BOLD + "RUN",
                            10,
                            20*3,
                            10);
                    Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), CountdownUtil::huntCountdown,40); //Waits for Prey's Warning to fade away
                } catch (NullPointerException exception) {
                    player.sendMessage(ChatColor.RED + "That player could not be found");
                }
            }
        }
    }
}
