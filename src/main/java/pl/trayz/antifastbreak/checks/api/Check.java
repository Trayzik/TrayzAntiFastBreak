package pl.trayz.antifastbreak.checks.api;

import io.netty.channel.ChannelHandlerContext;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * @Author: Trayz
 **/

public abstract class Check {

    public final Player player;
    public final CraftPlayer craftPlayer;

    public Check(Player player) {
        this.player = player;
        this.craftPlayer = (CraftPlayer) player;
    }

    public abstract void packetReceived(ChannelHandlerContext channelHandlerContext, Object packet);

}
