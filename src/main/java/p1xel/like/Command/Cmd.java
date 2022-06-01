package p1xel.like.Command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import p1xel.like.Events.LikePointAddEvent;
import p1xel.like.Like;
import p1xel.like.Storage.Config;
import p1xel.like.Storage.Data;
import p1xel.like.Storage.Locale;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class Cmd implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Locale.getMessage("commands.help"));
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("help")) {

                sender.sendMessage(Locale.getMessage("commands.top"));
                sender.sendMessage(Locale.getMessage("commands.plugin").replaceAll("%version%", Config.getVersion()));
                sender.sendMessage(Locale.getMessage("commands.space-1"));
                sender.sendMessage(Locale.getMessage("commands.help"));
                sender.sendMessage(Locale.getMessage("commands.like"));
                sender.sendMessage(Locale.getMessage("commands.unlike"));
                sender.sendMessage(Locale.getMessage("commands.look"));
                if (sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("commands.level"));
                    sender.sendMessage(Locale.getMessage("commands.point"));
                    sender.sendMessage(Locale.getMessage("commands.remove"));
                    sender.sendMessage(Locale.getMessage("commands.clear"));
                    sender.sendMessage(Locale.getMessage("commands.clearall"));
                    sender.sendMessage(Locale.getMessage("commands.reload"));
                }
                sender.sendMessage(Locale.getMessage("commands.space-8"));
                sender.sendMessage(Locale.getMessage("commands.bottom"));
                return true;

            }

            if (args[0].equalsIgnoreCase("reload")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                Config.reloadConfig();
                sender.sendMessage(Locale.getMessage("reload-success"));
                return true;

            }

            if (args[0].equalsIgnoreCase("like")) {

                if (!sender.hasPermission("like.use")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                sender.sendMessage(Locale.getMessage("commands.like"));
                return true;

            }

            if (args[0].equalsIgnoreCase("clearall")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                for (String key : Data.get().getKeys(false)) {
                    Data.set(key, null);
                }

                sender.sendMessage(Locale.getMessage("clearall-success"));
                return true;

            }

        }

        if (args.length <= 2) {

            if (args[0].equalsIgnoreCase("look")) {

                if (!sender.hasPermission("like.use")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                if (args.length == 2) {

                    OfflinePlayer argsplayer = Bukkit.getServer().getOfflinePlayer(args[1]);
                    String uuid = argsplayer.getUniqueId().toString();

                    if (!argsplayer.hasPlayedBefore() || !Data.isCreate(uuid)) {
                        sender.sendMessage(Locale.getMessage("invalid-player"));
                        return true;
                    }

                    for (String m : Locale.get().getStringList("look-message")) {

                        m = m.replaceAll("%player%", args[1]);
                        m = m.replaceAll("%level%", String.valueOf(Data.getLevel(uuid)));
                        m = m.replaceAll("%point%", String.valueOf(Data.getPoint(uuid)));
                        m = m.replaceAll("%maxpoint%", String.valueOf(Config.getInt("points-to-level")));
                        m = Locale.translate(m);

                        sender.sendMessage(m);

                    }

                    return true;

                }

                Player p = (Player) sender;
                String uuid = p.getUniqueId().toString();

                if (!Data.isCreate(uuid)) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                for (String m : Locale.get().getStringList("look-message")) {

                    m = m.replaceAll("%player%", sender.getName());
                    m = m.replaceAll("%level%", String.valueOf(Data.getLevel(uuid)));
                    m = m.replaceAll("%point%", String.valueOf(Data.getPoint(uuid)));
                    m = m.replaceAll("%maxpoint%", String.valueOf(Config.getInt("points-to-level")));
                    m = Locale.translate(m);

                    sender.sendMessage(m);

                }

                return true;

            }

        }

        if (args.length <= 3) {

            if (args[0].equalsIgnoreCase("like")) {

                if (!sender.hasPermission("like.use")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                Player p = (Player) sender;
                String senderName = sender.getName();
                Player target = Bukkit.getPlayer(args[1]);
                String senderUUID = p.getUniqueId().toString();
                String targetUUID = target.getUniqueId().toString();

                if (target == null) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (!Data.isCreate(target.getUniqueId().toString())) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (senderName.equalsIgnoreCase(args[1])) {
                    sender.sendMessage(Locale.getMessage("like-self"));
                    return true;
                }

                if (Data.isInPlayerList(targetUUID, senderUUID)) {
                    sender.sendMessage(Locale.getMessage("like-already").replaceAll("%player%", args[1]));
                    return true;
                }

                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("-a")) {
                        senderName = Locale.getMessage("Anonymous");
                    }
                }

                Data.addUUIDToList(targetUUID, senderUUID);
                LikePointAddEvent event = new LikePointAddEvent(targetUUID, Config.getInt("add"));
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("like-success").replaceAll("%player%", args[1]));
                target.sendMessage(Locale.getMessage("liked").replaceAll("%player%", senderName));
                return true;

            }

            if (args[0].equalsIgnoreCase("unlike")) {

                if (!sender.hasPermission("like.use")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                Player p = (Player) sender;
                String senderName = sender.getName();
                Player target = Bukkit.getPlayer(args[1]);
                String senderUUID = p.getUniqueId().toString();
                String targetUUID = target.getUniqueId().toString();

                if (target == null) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (!Data.isCreate(target.getUniqueId().toString())) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (senderName.equalsIgnoreCase(args[1])) {
                    sender.sendMessage(Locale.getMessage("unlike-self"));
                    return true;
                }

                if (Data.isInPlayerList(targetUUID, senderUUID)) {
                    sender.sendMessage(Locale.getMessage("unlike-already").replaceAll("%player%", args[1]));
                    return true;
                }

                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("-a")) {
                        senderName = Locale.getMessage("Anonymous");
                    }
                }

                Data.addUUIDToList(targetUUID, senderUUID);
                sender.sendMessage(Locale.getMessage("unlike-success").replaceAll("%player%", args[1]));
                target.sendMessage(Locale.getMessage("unliked").replaceAll("%player%", senderName));
                return true;

            }

        }

        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("remove")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[1]);
                String targetUUID = target.getUniqueId().toString();
                OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[2]);
                String playerUUID = player.getUniqueId().toString();

                if (!target.hasPlayedBefore() || !Data.isCreate(targetUUID) || !player.hasPlayedBefore() || !Data.isCreate(playerUUID)) {
                    sender.sendMessage(Locale.getMessage("invalid-player"));
                    return true;
                }

                if (!Data.isInPlayerList(targetUUID, playerUUID)) {
                    sender.sendMessage(Locale.getMessage("not-in-list"));
                    return true;
                }

                Data.removeUUIDFromList(targetUUID, playerUUID);
                sender.sendMessage(Locale.getMessage("remove-success").replaceAll("%target%", args[1]).replaceAll("%player%", args[2]));
                return true;

            }

            if (args[0].equalsIgnoreCase("clear")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("point") || args[1].equalsIgnoreCase("level") || args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("whole")) {

                    OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[2]);
                    String uuid = p.getUniqueId().toString();

                    if (!p.hasPlayedBefore() || !Data.isCreate(uuid)) {
                        sender.sendMessage(Locale.getMessage("invalid-player"));
                        return true;
                    }

                    int level = Data.getLevel(uuid);
                    int point = Data.getPoint(uuid);

                    if (args[1].equalsIgnoreCase("list")) {

                        Data.clearList(uuid);
                        sender.sendMessage(Locale.getMessage("clear-success").replaceAll("%player%", args[2]).replaceAll("%type%", args[1]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("level")) {

                        Data.takeLevel(uuid, level);
                        sender.sendMessage(Locale.getMessage("clear-success").replaceAll("%player%", args[2]).replaceAll("%type%", args[1]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("point")) {

                        Data.takePoint(uuid, point);
                        sender.sendMessage(Locale.getMessage("clear-success").replaceAll("%player%", args[2]).replaceAll("%type%", args[1]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("all")) {

                        Data.takeLevel(uuid, level);
                        Data.takePoint(uuid, point);
                        sender.sendMessage(Locale.getMessage("clear-success").replaceAll("%player%", args[2]).replaceAll("%type%", args[1]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("whole")) {

                        Data.set(uuid, null);
                        sender.sendMessage(Locale.getMessage("clear-success").replaceAll("%player%", args[2]).replaceAll("%type%", args[1]));
                        return true;

                    }

                } else {
                    sender.sendMessage(Locale.getMessage("wrong-type"));
                    return true;
                }

            }

        }

        if (args.length == 4) {

            if (args[0].equalsIgnoreCase("level")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {

                    OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[2]);
                    String uuid = p.getUniqueId().toString();

                    if (!p.hasPlayedBefore() || !Data.isCreate(uuid)) {
                        sender.sendMessage(Locale.getMessage("invalid-player"));
                        return true;
                    }

                    int quantity = Integer.parseInt(args[3]);

                    if (quantity <= 0) {
                        sender.sendMessage(Locale.getMessage("wrong-int"));
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("add")) {

                        Data.addLevel(uuid, quantity);
                        sender.sendMessage(Locale.getMessage("level-add-success").replaceAll("%player%", args[2]).replaceAll("%level%", args[3]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("remove")) {

                        if (quantity > Data.getLevel(uuid)) {
                            sender.sendMessage(Locale.getMessage("too-much"));
                            return true;
                        }

                        Data.takeLevel(uuid, quantity);
                        sender.sendMessage(Locale.getMessage("level-remove-success").replaceAll("%player%", args[2]).replaceAll("%level%", args[3]));
                        return true;
                    }

                } else {
                    sender.sendMessage(Locale.getMessage("levelpoint-wrong"));
                    return true;
                }

            }

            if (args[0].equalsIgnoreCase("point")) {

                if (!sender.hasPermission("like.admin")) {
                    sender.sendMessage(Locale.getMessage("no-perm"));
                    return true;
                }

                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {

                    OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[2]);
                    String uuid = p.getUniqueId().toString();

                    if (!p.hasPlayedBefore() || !Data.isCreate(uuid)) {
                        sender.sendMessage(Locale.getMessage("invalid-player"));
                        return true;
                    }

                    int quantity = Integer.parseInt(args[3]);

                    if (quantity <= 0) {
                        sender.sendMessage(Locale.getMessage("wrong-int"));
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("add")) {

                        Data.addPoint(uuid, quantity);
                        sender.sendMessage(Locale.getMessage("point-add-success").replaceAll("%player%", args[2]).replaceAll("%point%", args[3]));
                        return true;

                    }

                    if (args[1].equalsIgnoreCase("remove")) {

                        if (quantity > Data.getPoint(uuid)) {
                            sender.sendMessage(Locale.getMessage("too-much"));
                            return true;
                        }

                        Data.takePoint(uuid, quantity);
                        sender.sendMessage(Locale.getMessage("point-remove-success").replaceAll("%player%", args[2]).replaceAll("%point%", args[3]));
                        return true;
                    }

                } else {
                    sender.sendMessage(Locale.getMessage("levelpoint-wrong"));
                    return true;
                }

            }

        }

        return false;
    }


}
