package compass.tracker;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

//todo add pages to the list of player to increase to allotted amount fo players
public class GUIHandler implements Listener {
    private static final Component guiName = Component.text("Who would you like to hunt?");

    public static void openGUI(Player player) {
        int playerAmount = player.getWorld().getPlayers().size();
        int guiSize;

        // sets gui to the correct multiple of 9
        if (playerAmount <= 54) {
            guiSize = playerAmount + (9 - playerAmount % 9); //adds how many more players til full row of 9
        } else {
            player.sendMessage("Too many players!");
            return;
        }

        Inventory gui = Bukkit.createInventory(player, guiSize, guiName);

        //Run task asynchronously to reduce lag on larger player sets
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player playerI : player.getWorld().getPlayers()) {
                    if (!playerI.getName().equals(player.getName())) {
                        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

                        headMeta.displayName(Component.text(playerI.getName()));
                        headMeta.setOwningPlayer(playerI);
                        head.setItemMeta(headMeta);
                        gui.addItem(head);
                    }
                }
            }
        }.runTaskAsynchronously(CompassTracker.getPlugin());
        player.openInventory(gui);
    }

    @EventHandler
    public static void onMenuClick(InventoryClickEvent event) {
        if (event.getView().title().equals(guiName) && event.getCurrentItem() != null) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            player.closeInventory();

            if (event.getClickedInventory().getType().equals(InventoryType.CHEST) && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                try {
                    Player clickedPlayer = Bukkit.getPlayer(PlainComponentSerializer.plain().serialize(event.getCurrentItem().getItemMeta().displayName()));
                    Compass.giveCompass(player, clickedPlayer);
                } catch (NullPointerException exception) {
                    player.sendMessage(ChatColor.RED + "That player could not be found");
                }
            }
        }
    }
}
