package p1xel.like.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import p1xel.like.Storage.Data;

import java.util.UUID;

public class LikeLevelUpgradedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final String u;
    private final int level;

    public LikeLevelUpgradedEvent(String uuid, int levels) {
        p = Bukkit.getPlayer(UUID.fromString(uuid));
        u = uuid;
        level = levels;

        Data.addLevel(uuid, levels);
    }

    public Player getPlayer() {
        return p;
    }

    public String getUUID() {
        return u;
    }

    public int getLevel() {
        return level;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
