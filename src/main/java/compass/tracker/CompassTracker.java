package compass.tracker;

import compass.tracker.commands.TrackerCommandHandler;
import compass.tracker.commands.TrackerTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompassTracker extends JavaPlugin {
    public static CompassTracker plugin;

    public static CompassTracker getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getLogger().info("[PlayerCompassTracker] Ready to track who you want");
        RegisterCommandsAndEvents();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[PlayerCompassTracker] I'll be back");
    }

    private void RegisterCommandsAndEvents () {
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), plugin);

        getCommand("hunt").setExecutor(new TrackerCommandHandler());
        getCommand("hunt").setTabCompleter(new TrackerTabCompleter());
    }
}
