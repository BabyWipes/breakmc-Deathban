package com.breakmc.DeathBan.teams;

import java.util.*;
import org.bukkit.*;
import com.breakmc.DeathBan.*;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class TeamClaimPlayerManager implements Listener
{
    Set<String> hasEntered;
    TeamClaim claim;
    
    public TeamClaimPlayerManager(final TeamClaim claim) {
        super();
        this.hasEntered = new HashSet<String>();
        this.claim = claim;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.getPlugin((Class)Main.class));
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (this.claim.contains(event.getTo()) && !this.hasEntered.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage("§6Entering §e" + this.claim.getOwner().getName() + "§6's claim.");
            this.hasEntered.add(event.getPlayer().getName());
        }
        else if (!this.claim.contains(event.getTo()) && this.hasEntered.contains(event.getPlayer().getName())) {
            event.getPlayer().sendMessage("§6Leaving §e" + this.claim.getOwner().getName() + "§6's claim.");
            this.hasEntered.remove(event.getPlayer().getName());
        }
    }
}
