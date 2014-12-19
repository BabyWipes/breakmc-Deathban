package com.breakmc.DeathBan.objects;

import java.io.*;
import org.bukkit.entity.*;

public class User implements Serializable
{
    int gold;
    Player p;
    
    public User(final Player p, final int gold) {
        super();
        this.gold = gold;
        this.p = p;
    }
    
    public int getGold() {
        return this.gold;
    }
    
    public Player getPlayer() {
        return this.p;
    }
    
    public void setGold(final int gold) {
        this.gold = gold;
    }
    
    public void setPlayer(final Player p) {
        this.p = p;
    }
}
