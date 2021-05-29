package compass.tracker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class EffectsUtil {
    public static void startHunt(Player player) {
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        player.getInventory().setItemInOffHand(totem);
        player.setHealth(0.5);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 2));
        new BukkitRunnable() {
            @Override
            public void run() {
                for(PotionEffect effect : player.getActivePotionEffects())
                {
                    player.removePotionEffect(effect.getType());
                }
            }
        }.runTaskLater(CompassTracker.getPlugin(),5);
    }
}
