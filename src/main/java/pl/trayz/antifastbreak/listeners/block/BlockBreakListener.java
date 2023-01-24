package pl.trayz.antifastbreak.listeners.block;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pl.trayz.antifastbreak.cache.Cache;

/**
 * @Author: Trayz
 **/

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(!Cache.fastBreakPunishments.asMap().containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
    }
}
