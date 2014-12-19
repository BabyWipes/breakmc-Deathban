package com.breakmc.DeathBan.listener;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;

public class SpawnListener implements Listener
{
    LinkedList<EntityType> hostileMobs;
    
    public SpawnListener() {
        super();
        (this.hostileMobs = new LinkedList<EntityType>()).add(EntityType.SPIDER);
        this.hostileMobs.add(EntityType.ZOMBIE);
        this.hostileMobs.add(EntityType.CREEPER);
        this.hostileMobs.add(EntityType.ENDERMAN);
        this.hostileMobs.add(EntityType.SKELETON);
    }
    
    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
    }
    
    @EventHandler
    public void onCombust(final EntityCombustEvent event) {
        if ((event.getEntity().getType() == EntityType.ZOMBIE || event.getEntity().getType() == EntityType.SKELETON) && this.isDay(event.getEntity().getLocation().getWorld().getTime())) {
            if (event.getEntity().getLocation().subtract(0.0, 1.0, 0.0).getBlock().equals(Material.LAVA) || event.getEntity().getLocation().subtract(0.0, 1.0, 0.0).getBlock().equals(Material.STATIONARY_LAVA)) {
                return;
            }
            event.setCancelled(true);
        }
    }
    
    public boolean isDay(final long time) {
        return time < 12000L;
    }
}
