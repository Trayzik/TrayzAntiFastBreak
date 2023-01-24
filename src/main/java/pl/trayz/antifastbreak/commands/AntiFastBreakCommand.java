package pl.trayz.antifastbreak.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.trayz.antifastbreak.AntiFastBreakPlugin;
import pl.trayz.antifastbreak.cache.Cache;
import pl.trayz.antifastbreak.configuration.Config;

/**
 * @Author: Trayz
 **/

public class AntiFastBreakCommand implements CommandExecutor {

    private final AntiFastBreakPlugin plugin;

    public AntiFastBreakCommand(AntiFastBreakPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender,Command command, String s, String[] args) {
        if(!commandSender.isOp()) return false;

        if(args.length <1) {
            commandSender.sendMessage("§cUsage: /antifastbreak <reload/switchcheck/ignoreplayer/ignoredplayers>");
            return true;
        }

        switch (args[0]) {
            case "reload": {
                plugin.reloadConfig();
                Config.load(plugin);
                commandSender.sendMessage("§aReloaded plugin!");
                break;
            }
            case "switchcheck": {
                Config.FastBreak.enabled = !Config.FastBreak.enabled;
                commandSender.sendMessage("§aChanged fastbreakcheck to: " + Config.FastBreak.enabled);
                plugin.getConfig().set("FastBreak.enabled", Config.FastBreak.enabled);
                plugin.saveConfig();
                break;
            }
            case "ignoreplayer": {
                if (args.length < 2) {
                    commandSender.sendMessage("§cUsage: /antifastbreak ignoreplayer <nick>");
                    return true;
                }
                String nick = args[1].toLowerCase();
                if (Cache.ignoredPlayers.containsKey(nick)) {
                    Cache.ignoredPlayers.remove(nick);
                    commandSender.sendMessage("§aRemoved player from ignored list!");
                } else {
                    Cache.ignoredPlayers.put(nick, (byte) 0);
                    commandSender.sendMessage("§aAdded player to ignored list!");
                }
                break;
            }
            case "ignoredplayers": {
                commandSender.sendMessage("§aIgnored players: §f" + Cache.ignoredPlayers.keySet());
                break;
            }
            default: {
                commandSender.sendMessage("§cUsage: /antifastbreak <reload/switchcheck/ignoreplayer/ignoredplayers>");
                break;
            }
        }
        return false;
    }

}
