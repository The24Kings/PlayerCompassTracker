package compass.tracker.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NickUtil {
    private static UUID nickedPrey = null; //Stores the current nicked Prey's UUID

    public static void setPreyNick(Player prey) {
        nickedPrey = prey.getUniqueId();
        prey.playerListName(Component.text("☠ " + prey.getName() + " ☠"));
        prey.displayName(Component.text("☠ " + prey.getName() + " ☠"));
    }

    public static void resetPreyNick() {
        if(Bukkit.getPlayer(nickedPrey) != null) {
            Bukkit.getPlayer(nickedPrey).playerListName(Component.text(Bukkit.getPlayer(nickedPrey).getName()));
            Bukkit.getPlayer(nickedPrey).displayName(Component.text(Bukkit.getPlayer(nickedPrey).getName()));
            nickedPrey = null;
        }
    }

    public static UUID getNickedPrey() {
        return nickedPrey;
    }
}
