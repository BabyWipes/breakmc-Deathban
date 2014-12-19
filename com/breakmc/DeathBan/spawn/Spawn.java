package com.breakmc.DeathBan.spawn;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class Spawn implements Listener
{
    Location loc;
    
    public Spawn() {
        super();
        this.loc = new Location(Bukkit.getWorld("world"), 0.0, 90.0, 0.0);
        Bukkit.getServer().getWorld("world").setSpawnLocation(0, 90, 0);
    }
    
    @EventHandler
    public void onMobSpawn(final CreatureSpawnEvent e) {
        if (e.getLocation().getX() <= 10.0 && e.getLocation().getX() >= -10.0 && e.getLocation().getZ() <= 10.0 && e.getLocation().getZ() >= -10.0) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            e.getPlayer().teleport(this.loc);
        }
    }
    
    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().getLocation().getX() <= 10.0 && e.getEntity().getLocation().getX() >= -10.0 && e.getEntity().getLocation().getZ() <= 10.0 && e.getEntity().getLocation().getZ() >= -10.0) {
            e.setCancelled(true);
        }
    }
}
