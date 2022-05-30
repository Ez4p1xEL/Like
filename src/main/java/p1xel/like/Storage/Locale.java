package p1xel.like.Storage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import p1xel.like.Like;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Locale {

    public static void createFile() {

        List<String> lang = Arrays.asList("en","zh_CN");
        for (String l : lang) {
            File file = new File(Like.getInstance().getDataFolder(), l + ".yml");
            if (!file.exists()) {
                Like.getInstance().saveResource(l + ".yml", false);
            }
        }

    }

    public static FileConfiguration get() {
        File file = new File(Like.getInstance().getDataFolder(), Config.getLanguage() + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void set(String path, Object value) {
        File file = new File(Like.getInstance().getDataFolder(), Config.getLanguage() + ".yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(path, value);
        try {
            yaml.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', get().getString(path).replaceAll("%prefix%", get().getString("Prefix")));
    }

}
