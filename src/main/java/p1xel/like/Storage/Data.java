package p1xel.like.Storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import p1xel.like.Events.LikeLevelUpgradedEvent;
import p1xel.like.Like;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Data {

    public static void createFile() {

        File file = new File(Like.getInstance().getDataFolder(), "data.yml");
        if (!file.exists()) {
            Like.getInstance().saveResource("data.yml", false);
        }

    }

    public static FileConfiguration get() {
        File file = new File(Like.getInstance().getDataFolder(), "data.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void set(String path, Object value) {
        File file = new File(Like.getInstance().getDataFolder(), "data.yml");
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(path, value);
        try {
            yaml.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static boolean isCreate(String uuid) {
        return get().isSet(uuid);
    }

    public static void createPlayer(String uuid, String name) {

        set(uuid + ".name", name);
        set(uuid + ".level", 0);
        set(uuid + ".point", 0);
        set(uuid + ".list", "[]");

    }

    public static String getName(String uuid) {
        return get().getString(uuid + ".name");
    }

    public static int getLevel(String uuid) {
        return get().getInt(uuid + ".level");
    }

    public static int getPoint(String uuid) {
        return get().getInt(uuid + ".point");
    }

    public static List<String> getList(String uuid) {
        return get().getStringList(uuid + ".list");
    }

    public static boolean isInPlayerList(String targetUUID, String uuid) {
        return getList(targetUUID).contains(uuid);
    }

    public static void setLevel(String uuid, int level) {
        set(uuid + ".level", level);
    }

    public static void setPoint(String uuid, int point) {
        set(uuid + ".point", point);
    }

    public static void clearPoint(String uuid) {
        set(uuid + ".point", 0);
    }

    public static void addLevel(String uuid, int level) {
        setLevel(uuid, getLevel(uuid) + level);
    }

    public static void takeLevel(String uuid, int level) {
        setLevel(uuid, getLevel(uuid) - level);
    }

    public static void addPoint(String uuid, int point) {
        setPoint(uuid, getPoint(uuid) + point);
        checkPoint(uuid);
    }

    public static void takePoint(String uuid, int point) {
        setPoint(uuid, getPoint(uuid) - point);
    }

    public static void addUUIDToList(String targetUUID, String uuid) {
        List<String> list = getList(targetUUID);
        list.add(uuid);
        set(targetUUID + ".list", list);
    }

    public static void removeUUIDFromList(String targetUUID, String uuid) {
        List<String> list = getList(targetUUID);
        list.remove(uuid);
        set(targetUUID + ".list", list);
    }

    public static void clearList(String uuid) {
        List<String> list = getList(uuid);
        list.clear();
        set(uuid + ".list", list);
    }

    public static void checkPoint(String uuid) {
        if (getPoint(uuid) >= Config.getInt("points-to-level")) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            clearPoint(uuid);
            if (getLevel(uuid) < Config.getInt("max-level")) {
                LikeLevelUpgradedEvent event = new LikeLevelUpgradedEvent(uuid, 1);
                Bukkit.getServer().getPluginManager().callEvent(event);
                for (String cmd : Config.getStringList("liked-level-commands")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", p.getName()));
                }
                for (String key : Config.get().getConfigurationSection("rewards").getKeys(false)) {
                    if (key.equalsIgnoreCase(String.valueOf(getLevel(uuid)))) {
                        for (String cmd : Config.getStringList("rewards." + key + ".commands")) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", p.getName()));
                        }
                        p.sendMessage(Locale.getMessage("reward").replaceAll("%level%", key));
                    }
                }
                p.sendMessage(Locale.getMessage("upgrade").replaceAll("%level%", String.valueOf(getLevel(uuid))));
                Bukkit.getLogger().info(Locale.getMessage("console.player-upgrade").replaceAll("%player%", p.getName()).replaceAll("%level%", String.valueOf(getLevel(uuid))));
            } else {
                p.sendMessage(Locale.getMessage("max-level"));
                Bukkit.getLogger().info(Locale.getMessage("console.player-upgrade").replaceAll("%player%", p.getName()).replaceAll("%level%", String.valueOf(getLevel(uuid))));
            }
        }
    }

}
