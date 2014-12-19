package com.breakmc.DeathBan.serialization;

import org.bukkit.inventory.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import com.breakmc.DeathBan.json.*;

public class SingleItemSerialization
{
    public static JSONObject serializeItemInInventory(final ItemStack items, final int index) {
        return serializeItems(items, true, index);
    }
    
    public static JSONObject serializeItem(final ItemStack items) {
        return serializeItems(items, false, 0);
    }
    
    private static JSONObject serializeItems(final ItemStack items, final boolean useIndex, final int index) {
        try {
            final JSONObject values = new JSONObject();
            if (items == null) {
                return null;
            }
            final int id = items.getTypeId();
            final int amount = items.getAmount();
            final int data = items.getDurability();
            final boolean hasMeta = items.hasItemMeta();
            String name = null;
            String enchants = null;
            String[] lore = null;
            final Material mat = items.getType();
            JSONObject bookMeta = null;
            JSONObject armorMeta = null;
            JSONObject skullMeta = null;
            JSONObject fwMeta = null;
            if (mat == Material.BOOK_AND_QUILL || mat == Material.WRITTEN_BOOK) {
                bookMeta = BookSerialization.serializeBookMeta((BookMeta)items.getItemMeta());
            }
            else if (mat == Material.ENCHANTED_BOOK) {
                bookMeta = BookSerialization.serializeEnchantedBookMeta((EnchantmentStorageMeta)items.getItemMeta());
            }
            else if (Util.isLeatherArmor(mat)) {
                armorMeta = LeatherArmorSerialization.serializeArmor((LeatherArmorMeta)items.getItemMeta());
            }
            else if (mat == Material.SKULL_ITEM) {
                skullMeta = SkullSerialization.serializeSkull((SkullMeta)items.getItemMeta());
            }
            else if (mat == Material.FIREWORK) {
                fwMeta = FireworkSerialization.serializeFireworkMeta((FireworkMeta)items.getItemMeta());
            }
            if (hasMeta) {
                final ItemMeta meta = items.getItemMeta();
                if (meta.hasDisplayName()) {
                    name = meta.getDisplayName();
                }
                if (meta.hasLore()) {
                    lore = meta.getLore().toArray(new String[0]);
                }
                if (meta.hasEnchants()) {
                    enchants = EnchantmentSerialization.serializeEnchantments(meta.getEnchants());
                }
            }
            values.put("id", id);
            values.put("amount", amount);
            values.put("data", data);
            if (useIndex) {
                values.put("index", index);
            }
            if (name != null) {
                values.put("name", name);
            }
            if (enchants != null) {
                values.put("enchantments", enchants);
            }
            if (lore != null) {
                values.put("lore", lore);
            }
            if (bookMeta != null && bookMeta.length() > 0) {
                values.put("book-meta", bookMeta);
            }
            if (armorMeta != null && armorMeta.length() > 0) {
                values.put("armor-meta", armorMeta);
            }
            if (skullMeta != null && skullMeta.length() > 0) {
                values.put("skull-meta", skullMeta);
            }
            if (fwMeta != null && fwMeta.length() > 0) {
                values.put("firework-meta", fwMeta);
            }
            return values;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack getItem(final String item) {
        return getItem(item, 0);
    }
    
    public static ItemStack getItem(final String item, final int index) {
        try {
            return getItem(new JSONObject(item), index);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack getItem(final JSONObject item) {
        return getItem(item, 0);
    }
    
    public static ItemStack getItem(final JSONObject item, final int index) {
        try {
            final int id = item.getInt("id");
            final int amount = item.getInt("amount");
            final int data = item.getInt("data");
            String name = null;
            Map<Enchantment, Integer> enchants = null;
            ArrayList<String> lore = null;
            if (item.has("name")) {
                name = item.getString("name");
            }
            if (item.has("enchantments")) {
                enchants = EnchantmentSerialization.getEnchantments(item.getString("enchantments"));
            }
            if (item.has("lore")) {
                final JSONArray l = item.getJSONArray("lore");
                lore = new ArrayList<String>();
                for (int j = 0; j < l.length(); ++j) {
                    lore.add(l.getString(j));
                }
            }
            if (Material.getMaterial(id) == null) {
                throw new IllegalArgumentException("Item " + index + " - No Material found with id of " + id);
            }
            final Material mat = Material.getMaterial(id);
            final ItemStack stuff = new ItemStack(mat, amount, (short)data);
            if ((mat == Material.BOOK_AND_QUILL || mat == Material.WRITTEN_BOOK) && item.has("book-meta")) {
                final BookMeta meta = BookSerialization.getBookMeta(item.getJSONObject("book-meta"));
                stuff.setItemMeta((ItemMeta)meta);
            }
            else if (mat == Material.ENCHANTED_BOOK && item.has("book-meta")) {
                final EnchantmentStorageMeta meta2 = BookSerialization.getEnchantedBookMeta(item.getJSONObject("book-meta"));
                stuff.setItemMeta((ItemMeta)meta2);
            }
            else if (Util.isLeatherArmor(mat) && item.has("armor-meta")) {
                final LeatherArmorMeta meta3 = LeatherArmorSerialization.getLeatherArmorMeta(item.getJSONObject("armor-meta"));
                stuff.setItemMeta((ItemMeta)meta3);
            }
            else if (mat == Material.SKULL_ITEM && item.has("skull-meta")) {
                final SkullMeta meta4 = SkullSerialization.getSkullMeta(item.getJSONObject("skull-meta"));
                stuff.setItemMeta((ItemMeta)meta4);
            }
            else if (mat == Material.FIREWORK && item.has("firework-meta")) {
                final FireworkMeta meta5 = FireworkSerialization.getFireworkMeta(item.getJSONObject("firework-meta"));
                stuff.setItemMeta((ItemMeta)meta5);
            }
            final ItemMeta meta6 = stuff.getItemMeta();
            if (name != null) {
                meta6.setDisplayName(name);
            }
            if (lore != null) {
                meta6.setLore((List)lore);
            }
            stuff.setItemMeta(meta6);
            if (enchants != null) {
                stuff.addUnsafeEnchantments((Map)enchants);
            }
            return stuff;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeItemInInventoryAsString(final ItemStack items, final int index) {
        return serializeItemInInventoryAsString(items, index, false);
    }
    
    public static String serializeItemInInventoryAsString(final ItemStack items, final int index, final boolean pretty) {
        return serializeItemInInventoryAsString(items, index, pretty, 5);
    }
    
    public static String serializeItemInInventoryAsString(final ItemStack items, final int index, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeItemInInventory(items, index).toString(indentFactor);
            }
            return serializeItemInInventory(items, index).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeItemAsString(final ItemStack items) {
        return serializeItemAsString(items, false);
    }
    
    public static String serializeItemAsString(final ItemStack items, final boolean pretty) {
        return serializeItemAsString(items, pretty, 5);
    }
    
    public static String serializeItemAsString(final ItemStack items, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeItem(items).toString(indentFactor);
            }
            return serializeItem(items).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
