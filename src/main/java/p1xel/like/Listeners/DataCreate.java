package p1xel.like.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import p1xel.like.Events.LikePlayerDataCreateEvent;
import p1xel.like.Storage.Data;

public class DataCreate implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {

        String uuid = e.getPlayer().getUniqueId().toString();

        if (!Data.isCreate(uuid)) {

            LikePlayerDataCreateEvent event = new LikePlayerDataCreateEvent(uuid, e.getPlayer().getName());
            Bukkit.getServer().getPluginManager().callEvent(event);
            Bukkit.getLogger().info("Data created for " + e.getPlayer().getName() + " !");

        }

    }

}
