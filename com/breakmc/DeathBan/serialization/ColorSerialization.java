package com.breakmc.DeathBan.serialization;

import org.bukkit.*;
import com.breakmc.DeathBan.json.*;

public class ColorSerialization
{
    public static JSONObject serializeColor(final Color color) {
        try {
            final JSONObject root = new JSONObject();
            root.put("red", color.getRed());
            root.put("green", color.getGreen());
            root.put("blue", color.getBlue());
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Color getColor(final String color) {
        try {
            return getColor(new JSONObject(color));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Color getColor(final JSONObject color) {
        try {
            int r = 0;
            int g = 0;
            int b = 0;
            if (color.has("red")) {
                r = color.getInt("red");
            }
            if (color.has("green")) {
                g = color.getInt("green");
            }
            if (color.has("blue")) {
                b = color.getInt("blue");
            }
            return Color.fromRGB(r, g, b);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeColorAsString(final Color color) {
        return serializeColorAsString(color, false);
    }
    
    public static String serializeColorAsString(final Color color, final boolean pretty) {
        return serializeColorAsString(color, pretty, 5);
    }
    
    public static String serializeColorAsString(final Color color, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeColor(color).toString(indentFactor);
            }
            return serializeColor(color).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
