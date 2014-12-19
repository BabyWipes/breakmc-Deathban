package com.breakmc.DeathBan.serialization;

import org.bukkit.entity.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;

public class WolfSerialization
{
    public static JSONObject serializeWolf(final Wolf wolf) {
        try {
            final JSONObject root = LivingEntitySerialization.serializeEntity((LivingEntity)wolf);
            if (shouldSerialize("collar-color")) {
                root.put("collar-color", ColorSerialization.serializeColor(wolf.getCollarColor().getColor()));
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeWolfAsString(final Wolf wolf) {
        return serializeWolfAsString(wolf, false);
    }
    
    public static String serializeWolfAsString(final Wolf wolf, final boolean pretty) {
        return serializeWolfAsString(wolf, pretty, 5);
    }
    
    public static String serializeWolfAsString(final Wolf wolf, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeWolf(wolf).toString(indentFactor);
            }
            return serializeWolf(wolf).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Wolf spawnWolf(final Location location, final String stats) {
        try {
            return spawnWolf(location, new JSONObject(stats));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Wolf spawnWolf(final Location location, final JSONObject stats) {
        try {
            final Wolf wolf = (Wolf)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("collar-color")) {
                wolf.setCollarColor(DyeColor.getByColor(ColorSerialization.getColor(stats.getString("collar-color"))));
            }
            return wolf;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean shouldSerialize(final String key) {
        return SerializationConfig.getShouldSerialize("wolf." + key);
    }
}
