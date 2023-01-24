package pl.trayz.antifastbreak.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import pl.trayz.antifastbreak.AntiFastBreakPlugin;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Trayz
 **/

public class Config {

    public static void load(AntiFastBreakPlugin plugin) {
        FastBreak.load(plugin.getConfig());
    }

    public static class FastBreak {

        public static boolean enabled;
        public static int warnsToPunish;
        public static List<String> punishCommands;
        public static boolean disallowBlockBreakWithPunish;
        public static List<String> commandsOnWarn;
        public static long timeToResetWarns;
        public static HashMap<Material,Double> blocks;
        public static int maxPlayerPing;
        public static double minTps;

        public static double minCheckValue;

        public static void load(FileConfiguration config) {
            enabled = config.getBoolean("FastBreak.enabled");
            warnsToPunish = config.getInt("FastBreak.warnsToPunish");
            punishCommands = config.getStringList("FastBreak.commandsOnPunish");
            disallowBlockBreakWithPunish = config.getBoolean("FastBreak.dissalowBlockBreakWithPunish");
            commandsOnWarn = config.getStringList("FastBreak.commandsOnWarn");
            timeToResetWarns = config.getLong("FastBreak.timeToResetWarns");
            maxPlayerPing = config.getInt("FastBreak.maxPlayerPing");
            minTps = config.getDouble("FastBreak.minTps");

            minCheckValue = config.getDouble("FastBreak.minCheckValue");

            blocks = new HashMap<>();
            config.getConfigurationSection("FastBreak.blocks").getKeys(false).forEach(key -> {
                blocks.put(Material.getMaterial(key), config.getDouble("FastBreak.blocks." + key+".hardness"));
            });
        }

    }
}
