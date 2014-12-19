package com.breakmc.DeathBan.utils;

import com.breakmc.DeathBan.*;
import java.util.*;

public class DBUtils
{
    HashMap<String, Integer> lives;
    private Main main;
    
    public DBUtils(final Main main) {
        super();
        this.lives = new HashMap<String, Integer>();
        this.main = main;
    }
    
    public void loadLives() {
        if (!this.main.getJedis().hgetAll("lives").isEmpty() && this.main.getJedis().exists("lives")) {
            for (final Map.Entry<String, String> dblives : this.main.getJedis().hgetAll("lives").entrySet()) {
                this.lives.put(dblives.getKey(), Integer.valueOf(dblives.getValue()));
            }
        }
    }
    
    public void saveLives() {
        for (final Map.Entry<String, Integer> live : this.lives.entrySet()) {
            this.main.getJedis().hset("lives", live.getKey(), live.getValue().toString());
        }
    }
    
    public void addLive(final String player) {
        if (this.lives.containsKey(player)) {
            final int livecount = this.lives.get(player) + 1;
            this.lives.put(player, livecount);
        }
        else {
            this.lives.put(player, 1);
        }
    }
    
    public void addLives(final String player, final int amount) {
        if (amount < 0) {
            return;
        }
        if (this.lives.containsKey(player)) {
            final int livecount = this.lives.get(player) + amount;
            this.lives.put(player, livecount);
        }
        else {
            this.lives.put(player, amount);
        }
    }
    
    public void takeLive(final String player) {
        if (this.lives.containsKey(player)) {
            final int livecount = this.lives.get(player) - 1;
            if (livecount <= 0) {
                this.lives.remove(player);
                return;
            }
            this.lives.put(player, livecount);
        }
    }
    
    public void takeLives(final String player, final int amount) {
        if (this.lives.containsKey(player)) {
            final int livecount = this.lives.get(player) - amount;
            if (livecount <= 0) {
                this.lives.remove(player);
                return;
            }
            this.lives.put(player, livecount);
        }
    }
    
    public int getPlayerLives(final String name) {
        return this.lives.containsKey(name) ? this.lives.get(name) : 0;
    }
    
    public HashMap<String, Integer> getLives() {
        return this.lives;
    }
}
