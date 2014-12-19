package com.breakmc.DeathBan.teams;

import org.bukkit.*;
import org.bukkit.entity.*;

public class TeamClaim
{
    Team owner;
    Location loc1;
    Location loc2;
    
    public TeamClaim(final Team owner, final Location loc1, final Location loc2) {
        super();
        this.owner = owner;
        this.loc1 = loc1;
        this.loc2 = loc2;
        new TeamClaimPlayerManager(this);
    }
    
    public double getMaxX() {
        return Math.max(this.loc1.getX(), this.loc2.getX());
    }
    
    public double getMinX() {
        return Math.min(this.loc1.getX(), this.loc2.getX());
    }
    
    public double getMinZ() {
        return Math.min(this.loc1.getZ(), this.loc2.getZ());
    }
    
    public double getMaxZ() {
        return Math.max(this.loc1.getZ(), this.loc2.getZ());
    }
    
    public Location getLoc1() {
        return this.loc1;
    }
    
    public Location getLoc2() {
        return this.loc2;
    }
    
    public Team getOwner() {
        return this.owner;
    }
    
    public boolean contains(final Player player) {
        final Location location = player.getLocation();
        return location.getX() <= this.getMaxX() && location.getX() >= this.getMinX() && location.getZ() <= this.getMaxZ() && location.getZ() >= this.getMinZ();
    }
    
    public boolean contains(final Location location) {
        return location.getX() <= this.getMaxX() && location.getX() >= this.getMinX() && location.getZ() <= this.getMaxZ() && location.getZ() >= this.getMinZ();
    }
}
