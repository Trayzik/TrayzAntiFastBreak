package pl.trayz.antifastbreak.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trayz.antifastbreak.checks.api.RegisterPlayerCheck;

/**
 * @Author: Trayz
 **/

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!player.isOp())
            new RegisterPlayerCheck(player);
    }
}
