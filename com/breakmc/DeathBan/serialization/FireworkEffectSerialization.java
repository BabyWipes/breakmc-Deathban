package com.breakmc.DeathBan.serialization;

import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import java.util.*;

public class FireworkEffectSerialization
{
    public static FireworkEffect getFireworkEffect(final String json) {
        return getFireworkEffect(json);
    }
    
    public static FireworkEffect getFireworkEffect(final JSONObject json) {
        try {
            final FireworkEffect.Builder builder = FireworkEffect.builder();
            final JSONArray colors = json.getJSONArray("colors");
            for (int j = 0; j < colors.length(); ++j) {
                builder.withColor(ColorSerialization.getColor(colors.getJSONObject(j)));
            }
            final JSONArray fadeColors = json.getJSONArray("fade-colors");
            for (int i = 0; i < fadeColors.length(); ++i) {
                builder.withFade(ColorSerialization.getColor(colors.getJSONObject(i)));
            }
            if (json.getBoolean("flicker")) {
                builder.withFlicker();
            }
            if (json.getBoolean("trail")) {
                builder.withTrail();
            }
            builder.with(FireworkEffect.Type.valueOf(json.getString("type")));
            return builder.build();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (JSONException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static JSONObject serializeFireworkEffect(final FireworkEffect effect) {
        try {
            final JSONObject root = new JSONObject();
            final JSONArray colors = new JSONArray();
            for (final Color c : effect.getColors()) {
                colors.put(ColorSerialization.serializeColor(c));
            }
            root.put("colors", colors);
            final JSONArray fadeColors = new JSONArray();
            for (final Color c2 : effect.getFadeColors()) {
                fadeColors.put(ColorSerialization.serializeColor(c2));
            }
            root.put("fade-colors", fadeColors);
            root.put("flicker", effect.hasFlicker());
            root.put("trail", effect.hasTrail());
            root.put("type", effect.getType().name());
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeFireworkEffectAsString(final FireworkEffect effect) {
        return serializeFireworkEffectAsString(effect, false);
    }
    
    public static String serializeFireworkEffectAsString(final FireworkEffect effect, final boolean pretty) {
        return serializeFireworkEffectAsString(effect, false, 5);
    }
    
    public static String serializeFireworkEffectAsString(final FireworkEffect effect, final boolean pretty, final int indentFactor) {
        return Serializer.toString(serializeFireworkEffect(effect), pretty, indentFactor);
    }
}
