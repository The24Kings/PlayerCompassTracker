package compass.tracker.utils;

import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;

//TODO: Fix dependencies, outdated, fern will give fixed jar tomorrow :3
public class NickUtil {
    public static void setPreyNick(Player prey) {
        NametagEdit.getApi().setPrefix(prey,"☠ ");
        NametagEdit.getApi().setSuffix(prey," ☠");
    }

    public static void resetPreyNick(Player prey) {
        NametagEdit.getApi().setPrefix(prey,"");
        NametagEdit.getApi().setSuffix(prey,"");
    }
}
