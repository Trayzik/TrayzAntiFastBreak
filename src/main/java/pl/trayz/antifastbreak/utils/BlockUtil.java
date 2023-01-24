package pl.trayz.antifastbreak.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import pl.trayz.antifastbreak.configuration.Config;

/**
 * @Author: Trayz
 **/

public class BlockUtil {

    public static double getBlockDamage(Player player, BlockPosition position) {
        ItemStack tool = player.getInventory().getItemInHand();
        Block block = player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());

        Double hardness = Config.FastBreak.blocks.getOrDefault(block.getType(), null);
        if(hardness == null) return -1;

        float speedMultiplier;

        switch (tool.getType()) {
            case DIAMOND_PICKAXE:
                speedMultiplier = 8.0f;
                break;
            case GOLD_PICKAXE:
                speedMultiplier = 12.0f;
                break;
            case IRON_PICKAXE:
                speedMultiplier = 6.0f;
                break;
            case STONE_PICKAXE:
                speedMultiplier = 4.0f;
                break;
            case WOOD_PICKAXE:
                speedMultiplier = 2.0f;
                break;
            default:
                return -1;
        }

        int efficiencySpeed = tool.getEnchantmentLevel(Enchantment.DIG_SPEED);
        if (efficiencySpeed > 0) {
            speedMultiplier += efficiencySpeed * efficiencySpeed + 1;
        }


        Integer digSpeed = player.getActivePotionEffects().stream().filter(effect -> effect.getType().equals(PotionEffectType.FAST_DIGGING)).map(effect -> effect.getAmplifier() + 1).findFirst().orElse(0);
        speedMultiplier *= 1 + (0.2 * (digSpeed + 1));

        if (!player.isOnGround()) {
            speedMultiplier /= 5;
        }

        double damage = speedMultiplier / hardness;
        damage /= 30;

        return damage;
    }
}
