package com.breakmc.DeathBan.teams.listeners;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;
import org.bukkit.event.*;

public class FriendlyFireListener implements Listener
{
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            final Player hitted = (Player)event.getDamager();
            final Player hit = (Player)event.getEntity();
            if (TeamManager.getInstance().isOnTeam(hit.getName()) && TeamManager.getInstance().isOnTeam(hitted.getName())) {
                final Team hittedTeam = TeamManager.getInstance().getPlayerTeam(hitted);
                final Team hitTeam = TeamManager.getInstance().getPlayerTeam(hit);
                if (hitTeam.equals(hittedTeam) && !hitTeam.isFriendlyFire()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
