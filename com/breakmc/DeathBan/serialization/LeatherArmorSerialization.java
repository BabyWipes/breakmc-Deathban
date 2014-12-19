package com.breakmc.DeathBan.serialization;

import org.bukkit.inventory.meta.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class LeatherArmorSerialization
{
    public static JSONObject serializeArmor(final LeatherArmorMeta meta) {
        try {
            final JSONObject root = new JSONObject();
            root.put("color", ColorSerialization.serializeColor(meta.getColor()));
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeArmorAsString(final LeatherArmorMeta meta) {
        return serializeArmorAsString(meta, false);
    }
    
    public static String serializeArmorAsString(final LeatherArmorMeta meta, final boolean pretty) {
        return serializeArmorAsString(meta, pretty, 5);
    }
    
    public static String serializeArmorAsString(final LeatherArmorMeta meta, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeArmor(meta).toString(indentFactor);
            }
            return serializeArmor(meta).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static LeatherArmorMeta getLeatherArmorMeta(final String json) {
        try {
            return getLeatherArmorMeta(new JSONObject(json));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static LeatherArmorMeta getLeatherArmorMeta(final JSONObject json) {
        try {
            final ItemStack dummyItems = new ItemStack(Material.LEATHER_HELMET, 1);
            final LeatherArmorMeta meta = (LeatherArmorMeta)dummyItems.getItemMeta();
            if (json.has("color")) {
                meta.setColor(ColorSerialization.getColor(json.getJSONObject("color")));
            }
            return meta;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
