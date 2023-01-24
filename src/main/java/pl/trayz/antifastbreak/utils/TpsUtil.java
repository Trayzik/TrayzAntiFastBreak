package pl.trayz.antifastbreak.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

/**
 * @Author: Trayz
 **/

public class TpsUtil {

    public static double getTPS() {

        String name1 = Bukkit.getServer().getClass().getPackage().getName();
        String version = name1.substring(name1.lastIndexOf('.') + 1);

        Class<?> mcsclass;

        Object si = null;
        Field tpsField = null;

        try {
            mcsclass = Class.forName("net.minecraft.server." + version + "." + "MinecraftServer");

            si = mcsclass.getMethod("getServer").invoke(null);

            tpsField = si.getClass().getField("recentTps");

        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        double[] tps = new double[]{};

        try {
            tps = ((double[]) tpsField.get(si));

        } catch (IllegalArgumentException | IllegalAccessException e) { e.printStackTrace(); }

        return RoundUtil.round(tps[0],1);
    }

}
