package pl.trayz.antifastbreak.checks;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.trayz.antifastbreak.AntiFastBreakPlugin;
import pl.trayz.antifastbreak.cache.Cache;
import pl.trayz.antifastbreak.checks.api.Check;
import pl.trayz.antifastbreak.configuration.Config;
import pl.trayz.antifastbreak.utils.BlockUtil;
import pl.trayz.antifastbreak.utils.ColorUtil;
import pl.trayz.antifastbreak.utils.RoundUtil;
import pl.trayz.antifastbreak.utils.TpsUtil;

/**
 * @Author: Trayz
 **/

public class FastBreakCheck extends Check {
    public FastBreakCheck(Player player) {
        super(player);
    }

    private BlockPosition targetBlock = null;

    private double maximumBlockDamage = 0;
    private double blockBreakBalance = 0;
    private double blockDelayBalance = 0;

    private int warns = 0;

    private long lastFinishBreak = 0;
    private long startBreak = 0;
    private long lastWarn = 0;

    @Override
    public void packetReceived(ChannelHandlerContext channelHandlerContext, Object packet) {
        if(!Config.FastBreak.enabled || !player.getGameMode().equals(GameMode.SURVIVAL) || Cache.ignoredPlayers.containsKey(player.getName().toLowerCase())) return;

        if (packet instanceof PacketPlayInFlying && targetBlock != null) {
            maximumBlockDamage = Math.max(maximumBlockDamage, BlockUtil.getBlockDamage(player, targetBlock));
        }

        if(!(packet instanceof PacketPlayInBlockDig)) return;

        PacketPlayInBlockDig digPacket = (PacketPlayInBlockDig) packet;
        PacketPlayInBlockDig.EnumPlayerDigType type = digPacket.c();

        if(type == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
            startBreak = System.currentTimeMillis() - (targetBlock == null ? 50 : 0);
            targetBlock = digPacket.a();
            maximumBlockDamage = BlockUtil.getBlockDamage(player, targetBlock);
            if(maximumBlockDamage == -1) return;

            double breakDelay = System.currentTimeMillis() - lastFinishBreak;

            if (breakDelay >= 275) {
                blockDelayBalance *= 0.9;
            } else {
                blockDelayBalance += 300 - breakDelay;
            }

            if (blockDelayBalance > Config.FastBreak.minCheckValue) {
                punish(blockDelayBalance);
            }

            clampBalance();

        }

        if(type == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK && targetBlock != null) {
            double predictedTime = Math.ceil(1 / maximumBlockDamage) * 50;
            double realTime = System.currentTimeMillis() - startBreak;
            double diff = predictedTime - realTime;

            clampBalance();

            if (diff < 25) {
                blockBreakBalance *= 0.9;
            } else {
                blockBreakBalance += diff;
            }

            if (blockBreakBalance > Config.FastBreak.minCheckValue) {
                punish(blockBreakBalance);
            }

            lastFinishBreak = System.currentTimeMillis();
        }

        if (type == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
            targetBlock = null;
        }
    }

    private void punish(double balance) {
        if(craftPlayer.getHandle().ping > Config.FastBreak.maxPlayerPing || TpsUtil.getTPS() < Config.FastBreak.minTps) return;
        if(System.currentTimeMillis() - lastWarn > Config.FastBreak.timeToResetWarns) warns = 0;

        warns++;
        lastWarn = System.currentTimeMillis();

        if(warns > Config.FastBreak.warnsToPunish) {
            if(!Config.FastBreak.disallowBlockBreakWithPunish) {
                Cache.fastBreakPunishments.put(craftPlayer.getUniqueId(), (byte) 0);
            }

            if(Config.FastBreak.punishCommands.size() > 0) {
                Bukkit.getScheduler().runTask(AntiFastBreakPlugin.getInstance(), () -> Config.FastBreak.punishCommands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ColorUtil.colored(command.replace("%PLAYER%", craftPlayer.getName()).replace("%PING%", "" + craftPlayer.getHandle().ping).replace("%TPS%", "" + TpsUtil.getTPS()).replace("%WARN%", "" + warns).replace("%VALUE%", ""+RoundUtil.round(balance,1))))));
            }

            warns = 0;
        }

        craftPlayer.sendBlockChange(new Location(craftPlayer.getWorld(), targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()), Material.STONE, (byte) 0);

        if(Cache.fastBreakPunishments.asMap().containsKey(craftPlayer.getUniqueId())) return;

        if(Config.FastBreak.commandsOnWarn.size() > 0) {
            Bukkit.getScheduler().runTask(AntiFastBreakPlugin.getInstance(),()-> Config.FastBreak.commandsOnWarn.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ColorUtil.colored(command.replace("%PLAYER%", craftPlayer.getName()).replace("%PING%",""+craftPlayer.getHandle().ping).replace("%TPS%", ""+TpsUtil.getTPS()).replace("%WARN%", ""+warns).replace("%VALUE%", ""+RoundUtil.round(balance,1))))));
        }
    }


    private void clampBalance() {
        double balance = Math.max(1000, (craftPlayer.getHandle().ping / 1e6));
        blockBreakBalance = blockBreakBalance < -balance ? -balance : Math.min(blockBreakBalance, balance);
        blockDelayBalance = blockDelayBalance < -balance ? -balance : Math.min(blockDelayBalance, balance);
    }
}
