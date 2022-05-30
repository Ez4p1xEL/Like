package p1xel.like.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import p1xel.like.Storage.Config;
import p1xel.like.Storage.Data;

public class HPAPI extends PlaceholderExpansion  {

    public String getIdentifier() {
        return "like";
    }

    public String getAuthor() {
        return "p1xEL_mc";
    }

    public String getVersion() {
        return Config.getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {

        String uuid = player.getUniqueId().toString();

        if (identifier.equalsIgnoreCase("points")) {
            return String.valueOf(Data.getPoint(uuid));
        }

        if (identifier.equalsIgnoreCase("levels")) {
            return String.valueOf(Data.getLevel(uuid));
        }

        if (identifier.equalsIgnoreCase("maxpoints")) {
            return String.valueOf(Config.getInt("points-to-level"));
        }

        if (identifier.equalsIgnoreCase("maxlevels")) {
            return String.valueOf(Config.getInt("max-level"));
        }

        if (identifier.equalsIgnoreCase("points_need")) {
            return String.valueOf(Config.getInt("points-to-level") - Data.getPoint(uuid));
        }

        return null;
    }

}
