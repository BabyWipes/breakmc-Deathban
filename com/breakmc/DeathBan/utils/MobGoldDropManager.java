package com.breakmc.DeathBan.utils;

import org.bukkit.entity.*;
import java.io.*;
import java.util.*;

public class MobGoldDropManager
{
    static Map<EntityType, Integer> valueMap;
    
    public MobGoldDropManager(final File f) {
        super();
        try {
            final Scanner scanner = new Scanner(new FileInputStream(f));
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                if (line.contains(",")) {
                    final String mobType = line.split(",")[0];
                    final int goldCount = Integer.valueOf(line.split(",")[1]);
                    MobGoldDropManager.valueMap.put(EntityType.valueOf(mobType.toUpperCase()), goldCount);
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static Map<EntityType, Integer> getValueMap() {
        return MobGoldDropManager.valueMap;
    }
    
    static {
        MobGoldDropManager.valueMap = new HashMap<EntityType, Integer>();
    }
}
