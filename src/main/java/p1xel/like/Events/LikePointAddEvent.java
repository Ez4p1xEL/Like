package p1xel.like.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import p1xel.like.Storage.Config;
import p1xel.like.Storage.Data;

import java.util.UUID;

public class LikePointAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final String u;
    private final int point;

    public LikePointAddEvent(String uuid, int points) {
        p = Bukkit.getPlayer(UUID.fromString(uuid));
        u = uuid;
        point = points;

        Data.addPoint(uuid, points);
    }

    public Player getPlayer() {
        return p;
    }

    public String getUUID() {
        return u;
    }

    public int getPoint() {
        return point;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
