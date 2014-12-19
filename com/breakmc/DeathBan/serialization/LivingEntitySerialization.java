package com.breakmc.DeathBan.serialization;

import java.util.*;
import org.bukkit.potion.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public class LivingEntitySerialization
{
    public static JSONObject serializeEntity(final LivingEntity entity) {
        if (entity instanceof Player) {
            return PlayerSerialization.serializePlayer((Player)entity);
        }
        try {
            final JSONObject root = new JSONObject();
            if (shouldSerialize("age") && entity instanceof Ageable) {
                root.put("age", ((Ageable)entity).getAge());
            }
            if (shouldSerialize("health")) {
                root.put("health", ((Damageable)entity).getHealth());
            }
            if (shouldSerialize("name")) {
                root.put("name", entity.getCustomName());
            }
            if (shouldSerialize("potion-effects")) {
                root.put("potion-effects", PotionEffectSerialization.serializeEffects(entity.getActivePotionEffects()));
            }
            root.put("type", entity.getType().getTypeId());
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeEntityAsString(final LivingEntity entity) {
        return serializeEntityAsString(entity, false);
    }
    
    public static String serializeEntityAsString(final LivingEntity entity, final boolean pretty) {
        return serializeEntityAsString(entity, pretty, 5);
    }
    
    public static String serializeEntityAsString(final LivingEntity entity, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeEntity(entity).toString(indentFactor);
            }
            return serializeEntity(entity).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static LivingEntity spawnEntity(final Location location, final String stats) {
        try {
            return spawnEntity(location, new JSONObject(stats));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static LivingEntity spawnEntity(final Location location, final JSONObject stats) {
        try {
            if (!stats.has("type")) {
                throw new IllegalArgumentException("The type of the entity cannot be determined");
            }
            final LivingEntity entity = (LivingEntity)location.getWorld().spawnEntity(location, EntityType.fromId(stats.getInt("type")));
            if (stats.has("age") && entity instanceof Ageable) {
                ((Ageable)entity).setAge(stats.getInt("age"));
            }
            if (stats.has("health")) {
                entity.setHealth(stats.getDouble("health"));
            }
            if (stats.has("name")) {
                entity.setCustomName(stats.getString("name"));
            }
            while (true) {
                if (stats.has("potion-effects")) {
                    PotionEffectSerialization.addPotionEffects(stats.getString("potion-effects"), entity);
                    return entity;
                }
                continue;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean shouldSerialize(final String key) {
        return SerializationConfig.getShouldSerialize("living-entity." + key);
    }
}
