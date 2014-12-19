package com.breakmc.DeathBan.utils;

import org.bukkit.plugin.*;
import java.io.*;
import org.bukkit.configuration.file.*;

public class ConfigurationManager
{
    static ConfigurationManager instance;
    File goldFile;
    FileConfiguration goldConf;
    
    public static ConfigurationManager getInstance() {
        return ConfigurationManager.instance;
    }
    
    public void setup(final Plugin p) {
        this.goldFile = new File(p.getDataFolder(), "gold.yml");
        if (!this.goldFile.exists()) {
            try {
                this.goldFile.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.goldConf = (FileConfiguration)YamlConfiguration.loadConfiguration(this.goldFile);
    }
    
    public FileConfiguration getGoldConfig() {
        return this.goldConf;
    }
    
    public void saveGold() {
        try {
            this.getGoldConfig().save(this.goldFile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static {
        ConfigurationManager.instance = new ConfigurationManager();
    }
}
