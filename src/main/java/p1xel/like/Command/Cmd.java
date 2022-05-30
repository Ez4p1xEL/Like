package p1xel.like.Command;

import org.bukkit.Bukkit;
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
                if (sender.hasPermission("like.admin")) {
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

        }

        if (args.length == 2) {

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

                Data.addUUIDToList(targetUUID, senderUUID);
                LikePointAddEvent event = new LikePointAddEvent(targetUUID, Config.getInt("add"));
                Bukkit.getServer().getPluginManager().callEvent(event);
                sender.sendMessage(Locale.getMessage("like-success").replaceAll("%player%", args[1]));
                target.sendMessage(Locale.getMessage("liked").replaceAll("%player%", senderName));
                return true;

            }

        }

        return false;
    }


}
