package com.breakmc.DeathBan.serialization;

import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import java.util.*;

public class FireworkSerialization
{
    public static FireworkMeta getFireworkMeta(final String json) {
        return getFireworkMeta(json);
    }
    
    public static FireworkMeta getFireworkMeta(final JSONObject json) {
        try {
            final FireworkMeta dummy = (FireworkMeta)new ItemStack(Material.FIREWORK).getItemMeta();
            dummy.setPower(json.optInt("power", 1));
            final JSONArray effects = json.getJSONArray("effects");
            for (int i = 0; i < effects.length(); ++i) {
                final JSONObject effectDto = effects.getJSONObject(i);
                final FireworkEffect effect = FireworkEffectSerialization.getFireworkEffect(effectDto);
                if (effect != null) {
                    dummy.addEffect(effect);
                }
            }
            return dummy;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject serializeFireworkMeta(final FireworkMeta meta) {
        try {
            final JSONObject root = new JSONObject();
            root.put("power", meta.getPower());
            final JSONArray effects = new JSONArray();
            for (final FireworkEffect e : meta.getEffects()) {
                effects.put(FireworkEffectSerialization.serializeFireworkEffect(e));
            }
            root.put("effects", effects);
            return root;
        }
        catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }
    
    public static String serializeFireworkMetaAsString(final FireworkMeta meta) {
        return serializeFireworkMetaAsString(meta, false);
    }
    
    public static String serializeFireworkMetaAsString(final FireworkMeta meta, final boolean pretty) {
        return serializeFireworkMetaAsString(meta, false, 5);
    }
    
    public static String serializeFireworkMetaAsString(final FireworkMeta meta, final boolean pretty, final int indentFactor) {
        return Serializer.toString(serializeFireworkMeta(meta), pretty, indentFactor);
    }
}
