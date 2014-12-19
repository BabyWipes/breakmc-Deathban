package com.breakmc.DeathBan.serialization;

import org.bukkit.enchantments.*;
import java.util.*;
import org.bukkit.inventory.*;

public class EnchantmentSerialization
{
    public static String serializeEnchantments(final Map<Enchantment, Integer> enchantments) {
        String serialized = "";
        for (final Enchantment e : enchantments.keySet()) {
            serialized = serialized + e.getId() + ":" + enchantments.get(e) + ";";
        }
        return serialized;
    }
    
    public static Map<Enchantment, Integer> getEnchantments(final String serializedEnchants) {
        final HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
        if (serializedEnchants.isEmpty()) {
            return enchantments;
        }
        final String[] enchants = serializedEnchants.split(";");
        for (int i = 0; i < enchants.length; ++i) {
            final String[] ench = enchants[i].split(":");
            if (ench.length < 2) {
                throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + " (" + enchants[i] + "): split must at least have a length of 2");
            }
            if (!Util.isNum(ench[0])) {
                throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + " (" + enchants[i] + "): id is not an integer");
            }
            if (!Util.isNum(ench[1])) {
                throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + " (" + enchants[i] + "): level is not an integer");
            }
            final int id = Integer.parseInt(ench[0]);
            final int level = Integer.parseInt(ench[1]);
            final Enchantment e = Enchantment.getById(id);
            if (e == null) {
                throw new IllegalArgumentException(serializedEnchants + " - Enchantment " + i + " (" + enchants[i] + "): no Enchantment with id of " + id);
            }
            enchantments.put(e, level);
        }
        return enchantments;
    }
    
    public static Map<Enchantment, Integer> getEnchantsFromOldFormat(final String oldFormat) {
        final HashMap<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        if (oldFormat.length() == 0) {
            return enchants;
        }
        final String nums = Long.parseLong(oldFormat, 32) + "";
        System.out.println(nums);
        for (int i = 0; i < nums.length(); i += 3) {
            final int enchantId = Integer.parseInt(nums.substring(i, i + 2));
            final int enchantLevel = Integer.parseInt(nums.charAt(i + 2) + "");
            final Enchantment ench = Enchantment.getById(enchantId);
            enchants.put(ench, enchantLevel);
        }
        return enchants;
    }
    
    public static String convert(final String oldFormat) {
        final Map<Enchantment, Integer> enchants = getEnchantsFromOldFormat(oldFormat);
        return serializeEnchantments(enchants);
    }
    
    public static Map<Enchantment, Integer> convertAndGetEnchantments(final String oldFormat) {
        final String newFormat = convert(oldFormat);
        return getEnchantments(newFormat);
    }
    
    public static void addEnchantments(final String code, final ItemStack items) {
        items.addUnsafeEnchantments((Map)getEnchantments(code));
    }
}
