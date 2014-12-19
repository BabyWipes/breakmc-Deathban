package com.breakmc.DeathBan.serialization;

import org.bukkit.*;
import org.bukkit.inventory.*;
import com.breakmc.DeathBan.json.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.enchantments.*;
import java.util.*;

public class BookSerialization
{
    public static BookMeta getBookMeta(final String json) {
        try {
            return getBookMeta(new JSONObject(json));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static BookMeta getBookMeta(final JSONObject json) {
        try {
            final ItemStack dummyItems = new ItemStack(Material.WRITTEN_BOOK, 1);
            final BookMeta meta = (BookMeta)dummyItems.getItemMeta();
            String title = null;
            String author = null;
            JSONArray pages = null;
            if (json.has("title")) {
                title = json.getString("title");
            }
            if (json.has("author")) {
                author = json.getString("author");
            }
            if (json.has("pages")) {
                pages = json.getJSONArray("pages");
            }
            if (title != null) {
                meta.setTitle(title);
            }
            if (author != null) {
                meta.setAuthor(author);
            }
            if (pages != null) {
                final String[] allPages = new String[pages.length()];
                for (int i = 0; i < pages.length(); ++i) {
                    String page = pages.getString(i);
                    if (page.isEmpty() || page == null) {
                        page = "";
                    }
                    allPages[i] = page;
                }
                meta.setPages(allPages);
            }
            return meta;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject serializeBookMeta(final BookMeta meta) {
        try {
            final JSONObject root = new JSONObject();
            if (meta.hasTitle()) {
                root.put("title", meta.getTitle());
            }
            if (meta.hasAuthor()) {
                root.put("author", meta.getAuthor());
            }
            if (meta.hasPages()) {
                final String[] pages = meta.getPages().toArray(new String[0]);
                root.put("pages", pages);
            }
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeBookMetaAsString(final BookMeta meta) {
        return serializeBookMetaAsString(meta, false);
    }
    
    public static String serializeBookMetaAsString(final BookMeta meta, final boolean pretty) {
        return serializeBookMetaAsString(meta, pretty, 5);
    }
    
    public static String serializeBookMetaAsString(final BookMeta meta, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeBookMeta(meta).toString(indentFactor);
            }
            return serializeBookMeta(meta).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static EnchantmentStorageMeta getEnchantedBookMeta(final String json) {
        try {
            return getEnchantedBookMeta(new JSONObject(json));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static EnchantmentStorageMeta getEnchantedBookMeta(final JSONObject json) {
        try {
            final ItemStack dummyItems = new ItemStack(Material.ENCHANTED_BOOK, 1);
            final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)dummyItems.getItemMeta();
            if (json.has("enchantments")) {
                final Map<Enchantment, Integer> enchants = EnchantmentSerialization.getEnchantments(json.getString("enchantments"));
                for (final Enchantment e : enchants.keySet()) {
                    meta.addStoredEnchant(e, (int)enchants.get(e), true);
                }
            }
            return meta;
        }
        catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject serializeEnchantedBookMeta(final EnchantmentStorageMeta meta) {
        try {
            final JSONObject root = new JSONObject();
            final String enchants = EnchantmentSerialization.serializeEnchantments(meta.getStoredEnchants());
            root.put("enchantments", enchants);
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeEnchantedBookMetaAsString(final EnchantmentStorageMeta meta) {
        return serializeEnchantedBookMetaAsString(meta, false);
    }
    
    public static String serializeEnchantedBookMetaAsString(final EnchantmentStorageMeta meta, final boolean pretty) {
        return serializeEnchantedBookMetaAsString(meta, pretty, 5);
    }
    
    public static String serializeEnchantedBookMetaAsString(final EnchantmentStorageMeta meta, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeEnchantedBookMeta(meta).toString(indentFactor);
            }
            return serializeEnchantedBookMeta(meta).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
