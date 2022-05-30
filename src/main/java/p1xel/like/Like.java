package p1xel.like;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import p1xel.like.Command.Cmd;
import p1xel.like.Hook.HPAPI;
import p1xel.like.Listeners.DataCreate;
import p1xel.like.Storage.Config;
import p1xel.like.Storage.Data;
import p1xel.like.Storage.Locale;
import p1xel.like.bStats.Metrics;

public class Like extends JavaPlugin {

    private static Like instance;

    public static Like getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Locale.createFile();
        Data.createFile();
        Bukkit.getPluginManager().registerEvents(new DataCreate(), this);
        Bukkit.getServer().getPluginCommand("Like").setExecutor(new Cmd());

        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI"));
            }
        }

        if (Config.isPlaceholderAPIEnabled()) {
            new HPAPI().register();
        }

        // bStats
        int pluginId = 15349;
        new Metrics(this, pluginId);
        //

        getLogger().info("PVPMode " + Config.getVersion() + " loaded!");

        getLogger().info("Plugin loaded!");
    }

}
