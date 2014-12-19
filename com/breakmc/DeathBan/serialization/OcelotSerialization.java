package com.breakmc.DeathBan.serialization;

import org.bukkit.entity.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;

public class OcelotSerialization
{
    public static JSONObject serializeOcelot(final Ocelot ocelot) {
        try {
            final JSONObject root = LivingEntitySerialization.serializeEntity((LivingEntity)ocelot);
            if (shouldSerialize("type")) {
                root.put("type", ocelot.getCatType().name());
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeOcelotAsString(final Ocelot ocelot) {
        return serializeOcelotAsString(ocelot, false);
    }
    
    public static String serializeOcelotAsString(final Ocelot ocelot, final boolean pretty) {
        return serializeOcelotAsString(ocelot, pretty, 5);
    }
    
    public static String serializeOcelotAsString(final Ocelot ocelot, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeOcelot(ocelot).toString(indentFactor);
            }
            return serializeOcelot(ocelot).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Ocelot spawnOcelot(final Location location, final String stats) {
        try {
            return spawnOcelot(location, new JSONObject(stats));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Ocelot spawnOcelot(final Location location, final JSONObject stats) {
        try {
            final Ocelot ocelot = (Ocelot)LivingEntitySerialization.spawnEntity(location, stats);
            if (stats.has("type")) {
                ocelot.setCatType(Ocelot.Type.valueOf(stats.getString("type")));
            }
            return ocelot;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean shouldSerialize(final String key) {
        return SerializationConfig.getShouldSerialize("ocelot." + key);
    }
}
