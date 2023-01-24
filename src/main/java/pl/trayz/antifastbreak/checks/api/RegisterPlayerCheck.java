package pl.trayz.antifastbreak.checks.api;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.trayz.antifastbreak.checks.FastBreakCheck;

/**
 * @Author: Trayz
 **/

public class RegisterPlayerCheck {

    public RegisterPlayerCheck(Player player) {
        FastBreakCheck fastBreakCheck = new FastBreakCheck(player);

        ChannelDuplexHandler handler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);

                fastBreakCheck.packetReceived(channelHandlerContext, packet);
            }
        };

        ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), handler);
    }
}
