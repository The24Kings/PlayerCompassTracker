package compass.tracker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectsUtil {
    public static void startHunt(Player player) {
        double health = player.getHealth();
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        player.getInventory().setItemInOffHand(totem);
        player.setHealth(0.5);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 2));
        Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
            for(PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        },5);
        Bukkit.getScheduler().runTaskLater(CompassTracker.getPlugin(), () -> {
            player.setHealth(health);
        },6);
    }
}
