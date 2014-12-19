package com.breakmc.DeathBan.serialization;

import org.bukkit.inventory.meta.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class SkullSerialization
{
    public static JSONObject serializeSkull(final SkullMeta meta) {
        try {
            final JSONObject root = new JSONObject();
            if (meta.hasOwner()) {
                root.put("owner", meta.getOwner());
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeSkullAsString(final SkullMeta meta) {
        return serializeSkullAsString(meta, false);
    }
    
    public static String serializeSkullAsString(final SkullMeta meta, final boolean pretty) {
        return serializeSkullAsString(meta, pretty, 5);
    }
    
    public static String serializeSkullAsString(final SkullMeta meta, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeSkull(meta).toString(indentFactor);
            }
            return serializeSkull(meta).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static SkullMeta getSkullMeta(final String meta) {
        try {
            return getSkullMeta(new JSONObject(meta));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static SkullMeta getSkullMeta(final JSONObject meta) {
        try {
            final ItemStack dummyItems = new ItemStack(Material.SKULL_ITEM);
            final SkullMeta dummyMeta = (SkullMeta)dummyItems.getItemMeta();
            if (meta.has("owner")) {
                dummyMeta.setOwner(meta.getString("owner"));
            }
            return dummyMeta;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
