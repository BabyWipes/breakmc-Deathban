package com.breakmc.DeathBan.serialization;

import java.util.*;
import org.bukkit.potion.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class PlayerStatsSerialization
{
    public static JSONObject serializePlayerStats(final Player player) {
        try {
            final JSONObject root = new JSONObject();
            if (shouldSerialize("can-fly")) {
                root.put("can-fly", player.getAllowFlight());
            }
            if (shouldSerialize("display-name")) {
                root.put("display-name", player.getDisplayName());
            }
            if (shouldSerialize("exhaustion")) {
                root.put("exhaustion", player.getExhaustion());
            }
            if (shouldSerialize("exp")) {
                root.put("exp", player.getExp());
            }
            if (shouldSerialize("flying")) {
                root.put("flying", player.isFlying());
            }
            if (shouldSerialize("food")) {
                root.put("food", player.getFoodLevel());
            }
            if (shouldSerialize("gamemode")) {
                root.put("gamemode", player.getGameMode().ordinal());
            }
            if (shouldSerialize("health")) {
                root.put("health", player.getHealthScale());
            }
            if (shouldSerialize("level")) {
                root.put("level", player.getLevel());
            }
            if (shouldSerialize("potion-effects")) {
                root.put("potion-effects", PotionEffectSerialization.serializeEffects(player.getActivePotionEffects()));
            }
            if (shouldSerialize("saturation")) {
                root.put("saturation", player.getSaturation());
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializePlayerStatsAsString(final Player player) {
        return serializePlayerStatsAsString(player, false);
    }
    
    public static String serializePlayerStatsAsString(final Player player, final boolean pretty) {
        return serializePlayerStatsAsString(player, pretty, 5);
    }
    
    public static String serializePlayerStatsAsString(final Player player, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializePlayerStats(player).toString(indentFactor);
            }
            return serializePlayerStats(player).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void applyPlayerStats(final Player player, final String stats) {
        try {
            applyPlayerStats(player, new JSONObject(stats));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static void applyPlayerStats(final Player player, final JSONObject stats) {
        try {
            if (stats.has("can-fly")) {
                player.setAllowFlight(stats.getBoolean("can-fly"));
            }
            if (stats.has("display-name")) {
                player.setDisplayName(stats.getString("display-name"));
            }
            if (stats.has("exhaustion")) {
                player.setExhaustion((float)stats.getDouble("exhaustion"));
            }
            if (stats.has("exp")) {
                player.setExp((float)stats.getDouble("exp"));
            }
            if (stats.has("flying")) {
                player.setFlying(stats.getBoolean("flying"));
            }
            if (stats.has("food")) {
                player.setFoodLevel(stats.getInt("food"));
            }
            if (stats.has("health")) {
                player.setHealth(stats.getDouble("health"));
            }
            if (stats.has("gamemode")) {
                player.setGameMode(GameMode.getByValue(stats.getInt("gamemode")));
            }
            if (stats.has("level")) {
                player.setLevel(stats.getInt("level"));
            }
            if (stats.has("potion-effects")) {
                PotionEffectSerialization.setPotionEffects(stats.getString("potion-effects"), (LivingEntity)player);
            }
            if (stats.has("saturation")) {
                player.setSaturation((float)stats.getDouble("saturation"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean shouldSerialize(final String key) {
        return SerializationConfig.getShouldSerialize("player-stats." + key);
    }
}
