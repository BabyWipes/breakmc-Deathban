package com.breakmc.DeathBan.serialization;

import org.bukkit.*;

public class Util
{
    public static boolean isNum(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isLeatherArmor(final Material material) {
        return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS;
    }
    
    public static boolean keyFound(final String[] array, final String key) {
        for (final String s : array) {
            if (!s.equalsIgnoreCase(key)) {}
        }
        return false;
    }
}
