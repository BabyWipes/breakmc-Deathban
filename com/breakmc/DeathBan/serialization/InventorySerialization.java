package com.breakmc.DeathBan.serialization;

import com.breakmc.DeathBan.json.*;
import java.util.*;
import java.io.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class InventorySerialization
{
    public static JSONArray serializeInventory(final Inventory inv) {
        final JSONArray inventory = new JSONArray();
        for (int i = 0; i < inv.getSize(); ++i) {
            final JSONObject values = SingleItemSerialization.serializeItemInInventory(inv.getItem(i), i);
            if (values != null) {
                inventory.put(values);
            }
        }
        return inventory;
    }
    
    public static JSONObject serializePlayerInventory(final PlayerInventory inv) {
        try {
            final JSONObject root = new JSONObject();
            final JSONArray inventory = serializeInventory((Inventory)inv);
            final JSONArray armor = serializeInventory(inv.getArmorContents());
            root.put("inventory", inventory);
            root.put("armor", armor);
            return root;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializePlayerInventoryAsString(final PlayerInventory inv) {
        return serializePlayerInventoryAsString(inv, false);
    }
    
    public static String serializePlayerInventoryAsString(final PlayerInventory inv, final boolean pretty) {
        return serializePlayerInventoryAsString(inv, pretty, 5);
    }
    
    public static String serializePlayerInventoryAsString(final PlayerInventory inv, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializePlayerInventory(inv).toString(indentFactor);
            }
            return serializePlayerInventory(inv).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeInventoryAsString(final Inventory inventory) {
        return serializeInventoryAsString(inventory, false);
    }
    
    public static String serializeInventoryAsString(final Inventory inventory, final boolean pretty) {
        return serializeInventoryAsString(inventory, pretty, 5);
    }
    
    public static String serializeInventoryAsString(final Inventory inventory, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeInventory(inventory).toString(indentFactor);
            }
            return serializeInventory(inventory).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String serializeInventoryAsString(final ItemStack[] contents) {
        return serializeInventoryAsString(contents, false);
    }
    
    public static String serializeInventoryAsString(final ItemStack[] contents, final boolean pretty) {
        return serializeInventoryAsString(contents, pretty, 5);
    }
    
    public static String serializeInventoryAsString(final ItemStack[] contents, final boolean pretty, final int indentFactor) {
        try {
            if (pretty) {
                return serializeInventory(contents).toString(indentFactor);
            }
            return serializeInventory(contents).toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONArray serializeInventory(final ItemStack[] contents) {
        final JSONArray inventory = new JSONArray();
        for (int i = 0; i < contents.length; ++i) {
            final JSONObject values = SingleItemSerialization.serializeItemInInventory(contents[i], i);
            if (values != null) {
                inventory.put(values);
            }
        }
        return inventory;
    }
    
    public static ItemStack[] getInventory(final String json, final int size) {
        try {
            return getInventory(new JSONArray(json), size);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack[] getInventory(final JSONArray inv, final int size) {
        try {
            final ItemStack[] contents = new ItemStack[size];
            for (int i = 0; i < inv.length(); ++i) {
                final JSONObject item = inv.getJSONObject(i);
                final int index = item.getInt("index");
                if (index > size) {
                    throw new IllegalArgumentException("index found is greator than expected size (" + index + ">" + size + ")");
                }
                if (index > contents.length || index < 0) {
                    throw new IllegalArgumentException("Item " + i + " - Slot " + index + " does not exist in this inventory");
                }
                final ItemStack stuff = SingleItemSerialization.getItem(item);
                contents[index] = stuff;
            }
            return contents;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static ItemStack[] getInventory(final File jsonFile, final int size) {
        String source = "";
        try {
            final Scanner x = new Scanner(jsonFile);
            while (x.hasNextLine()) {
                source = source + x.nextLine() + "\n";
            }
            x.close();
            return getInventory(source, size);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setInventory(final InventoryHolder holder, final String inv) {
        setInventory(holder.getInventory(), inv);
    }
    
    public static void setInventory(final InventoryHolder holder, final JSONArray inv) {
        setInventory(holder.getInventory(), inv);
    }
    
    public static void setInventory(final Inventory inventory, final String inv) {
        try {
            setInventory(inventory, new JSONArray(inv));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static void setInventory(final Inventory inventory, final JSONArray inv) {
        final ItemStack[] items = getInventory(inv, inventory.getSize());
        inventory.clear();
        for (int i = 0; i < items.length; ++i) {
            final ItemStack item = items[i];
            if (item != null) {
                inventory.setItem(i, item);
            }
        }
    }
    
    public static void setPlayerInventory(final Player player, final String inv) {
        try {
            setPlayerInventory(player, new JSONObject(inv));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public static void setPlayerInventory(final Player player, final JSONObject inv) {
        try {
            final PlayerInventory inventory = player.getInventory();
            final ItemStack[] armor = getInventory(inv.getJSONArray("armor"), 4);
            inventory.clear();
            inventory.setArmorContents(armor);
            setInventory((InventoryHolder)player, inv.getJSONArray("inventory"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
