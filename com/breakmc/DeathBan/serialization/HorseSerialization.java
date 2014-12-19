package com.breakmc.DeathBan.serialization;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;

public class HorseSerialization
{
    public static JSONObject serializeHorse(final Horse horse) {
        try {
            final JSONObject root = LivingEntitySerialization.serializeEntity((LivingEntity)horse);
            if (shouldSerialize("color")) {
                root.put("color", horse.getColor().name());
            }
            if (shouldSerialize("inventory")) {
                root.put("inventory", InventorySerialization.serializeInventory((Inventory)horse.getInventory()));
            }
            if (shouldSerialize("jump-strength")) {
                root.put("jump-strength", horse.getJumpStrength());
            }
            if (shouldSerialize("style")) {
                root.put("style", horse.getStyle());
            }
            if (shouldSerialize("variant")) {
                root.put("variant", horse.getVariant());
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeHorseAsString(final Horse horse) {
        return serializeHorseAsString(horse, false);
    }
    
    public static String serializeHorseAsString(final Horse horse, final boolean pretty) {
        return serializeHorseAsString(horse, pretty, 5);
    }
    
    public static String serializeHorseAsString(final Horse horse, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeHorse(horse).toString(indentFactor);
            }
            return serializeHorse(horse).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Horse spawnHorse(final Location location, final String stats) {
        try {
            return spawnHorse(location, new JSONObject(stats));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Horse spawnHorse(final Location location, final JSONObject stats) {
        try {
            final Horse horse = (Horse)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("color")) {
                horse.setColor(Horse.Color.valueOf(stats.getString("color")));
            }
            if (stats.has("jump-strength")) {
                horse.setCustomName(stats.getString("name"));
            }
            if (stats.has("style")) {
                horse.setStyle(Horse.Style.valueOf(stats.getString("style")));
            }
            if (stats.has("inventory")) {
                PotionEffectSerialization.addPotionEffects(stats.getString("potion-effects"), (LivingEntity)horse);
            }
            if (stats.has("variant")) {
                horse.setVariant(Horse.Variant.valueOf(stats.getString("variant")));
            }
            return horse;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean shouldSerialize(final String key) {
        return SerializationConfig.getShouldSerialize("horse." + key);
    }
}
