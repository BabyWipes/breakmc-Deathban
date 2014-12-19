package com.breakmc.DeathBan.objects;

import org.bukkit.entity.*;

public class LoggedVillager
{
    Villager v;
    Player p;
    
    public LoggedVillager(final Villager v, final Player p) {
        super();
        this.v = v;
        this.p = p;
    }
    
    public Player getPlayer() {
        return this.p;
    }
    
    public Villager getVillager() {
        return this.v;
    }
    
    public String getPlayerName() {
        return this.getPlayer().getName();
    }
}
