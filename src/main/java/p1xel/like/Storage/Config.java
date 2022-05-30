package p1xel.like.Storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import p1xel.like.Like;

import java.util.List;

public class Config {

    public static String getString(String path) {
        return Like.getInstance().getConfig().getString(path);
    }

    public static int getInt(String path) {
        return Like.getInstance().getConfig().getInt(path);
    }

    public static List<String> getStringList(String path) {
        return Like.getInstance().getConfig().getStringList(path);
    }

    public static Configuration get() {
        return Like.getInstance().getConfig();
    }

    public static String getLanguage() {
        return getString("Language");
    }

    public static void reloadConfig() {
        Like.getInstance().reloadConfig();
    }

    public static String getVersion() { return getString("Version"); }

    public static boolean isPlaceholderAPIEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

}
