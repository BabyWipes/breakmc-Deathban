package com.breakmc.DeathBan.utils;

import org.bukkit.entity.*;
import java.io.*;
import java.util.*;

public class MobHealManager
{
    static Map<EntityType, Integer> valueMap;
    
    public MobHealManager(final File f) {
        super();
        try {
            final Scanner scanner = new Scanner(new FileInputStream(f));
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                if (line.contains(",")) {
                    final String mobType = line.split(",")[0];
                    final int healCount = Integer.valueOf(line.split(",")[1]);
                    MobHealManager.valueMap.put(EntityType.valueOf(mobType.toUpperCase()), healCount);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Map<EntityType, Integer> getValueMap() {
        return MobHealManager.valueMap;
    }
    
    static {
        MobHealManager.valueMap = new HashMap<EntityType, Integer>();
    }
}
