package com.breakmc.DeathBan.listener;

import com.breakmc.DeathBan.objects.*;
import java.util.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

public class ForDrugsYo implements Listener, Yo
{
    Random rand;
    
    public ForDrugsYo() {
        super();
        this.rand = new Random();
    }
    
    @EventHandler
    public void drugs(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location location = player.getLocation();
        final Block block = location.getBlock();
        if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
            final Block relative = block.getRelative(BlockFace.DOWN);
            if (relative.getType() == Material.WATER || relative.getType() == Material.STATIONARY_WATER) {
                final int randInt = this.rand.nextInt(100);
                if (randInt == 5) {
                    player.setFoodLevel(player.getFoodLevel() - 1);
                }
            }
        }
    }
}
