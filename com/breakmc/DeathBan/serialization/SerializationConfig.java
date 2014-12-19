package com.breakmc.DeathBan.serialization;

import org.bukkit.configuration.file.*;
import org.bukkit.*;
import java.util.logging.*;
import java.io.*;

public class SerializationConfig
{
    private static YamlConfiguration config;
    
    public static File getDataFolder() {
        final File pluginFolder = Bukkit.getServer().getPluginManager().getPlugins()[0].getDataFolder();
        return new File(pluginFolder.getParentFile() + "/TacoSerialization/");
    }
    
    public static File getConfigFile() {
        return new File(getDataFolder() + "/config.yml");
    }
    
    public static void reload() {
        SerializationConfig.config = YamlConfiguration.loadConfiguration(getConfigFile());
        setDefaults();
        save();
        Logger.getLogger("Minecraft").log(Level.INFO, "[TacoSerialization] Config reloaded");
    }
    
    public static void save() {
        try {
            SerializationConfig.config.save(getConfigFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void setDefaults() {
        addDefault("horse.color", true);
        addDefault("horse.inventory", true);
        addDefault("horse.jump-stength", true);
        addDefault("horse.style", true);
        addDefault("horse.variant", true);
        addDefault("living-entity.age", true);
        addDefault("living-entity.health", true);
        addDefault("living-entity.name", true);
        addDefault("living-entity.potion-effects", true);
        addDefault("ocelot.type", true);
        addDefault("player.ender-chest", true);
        addDefault("player.inventory", true);
        addDefault("player.stats", true);
        addDefault("player-stats.can-fly", true);
        addDefault("player-stats.display-name", true);
        addDefault("player-stats.exhaustion", true);
        addDefault("player-stats.exp", true);
        addDefault("player-stats.food", true);
        addDefault("player-stats.flying", true);
        addDefault("player-stats.health", true);
        addDefault("player-stats.level", true);
        addDefault("player-stats.potion-effects", true);
        addDefault("player-stats.saturation", true);
        addDefault("wolf.collar-color", true);
    }
    
    public static void addDefault(final String path, final Object value) {
        if (!getConfig().contains(path)) {
            getConfig().set(path, value);
        }
    }
    
    private static YamlConfiguration getConfig() {
        if (SerializationConfig.config == null) {
            reload();
        }
        return SerializationConfig.config;
    }
    
    public static boolean getShouldSerialize(final String path) {
        return getConfig().getBoolean(path);
    }
}
