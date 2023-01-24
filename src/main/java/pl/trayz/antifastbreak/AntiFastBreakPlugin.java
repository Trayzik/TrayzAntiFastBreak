package pl.trayz.antifastbreak;

import org.bukkit.plugin.java.JavaPlugin;
import pl.trayz.antifastbreak.commands.AntiFastBreakCommand;
import pl.trayz.antifastbreak.configuration.Config;
import pl.trayz.antifastbreak.listeners.block.BlockBreakListener;
import pl.trayz.antifastbreak.listeners.player.PlayerJoinListener;

/**
 * @Author: Trayz
 **/

public class AntiFastBreakPlugin extends JavaPlugin {

    private static AntiFastBreakPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        Config.load(this);

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        this.getCommand("antifastbreak").setExecutor(new AntiFastBreakCommand(this));
    }

    public static AntiFastBreakPlugin getInstance() {
        return instance;
    }
}
