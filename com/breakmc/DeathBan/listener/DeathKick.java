package com.breakmc.DeathBan.listener;

import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import com.breakmc.DeathBan.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;

public class DeathKick implements Listener
{
    static HashMap<String, Long> deathbanned;
    HashMap<String, Long> deathbannedPing;
    
    public DeathKick() {
        super();
        this.deathbannedPing = new HashMap<String, Long>();
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.getPlugin((Class)Main.class), (Runnable)new Runnable() {
            @Override
            public void run() {
                if (p.isDead()) {
                    DeathKick.deathbanned.put(p.getName(), System.currentTimeMillis());
                    DeathKick.this.deathbannedPing.put(p.getAddress().getAddress().getHostAddress(), System.currentTimeMillis());
                    p.kickPlayer("§fYou have died!\nYou may log back in after §b5 §fminutes");
                }
            }
        }, 5L);
    }
    
    @EventHandler
    public void onDeathBannedLogin(final AsyncPlayerPreLoginEvent e) {
        final long now = System.currentTimeMillis();
        final Long lastLogged = DeathKick.deathbanned.get(e.getName());
        if (lastLogged != null) {
            final long earliestNext = lastLogged + 300000L;
            if (now < earliestNext) {
                final long timeRemaining = earliestNext - now;
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You may log back in after §b" + this.formatTime(timeRemaining));
            }
        }
    }
    
    @EventHandler
    public void onDeathBanPing(final ServerListPingEvent e) throws Exception {
        final long now = System.currentTimeMillis();
        final Long lastChat = this.deathbannedPing.get(e.getAddress().getHostAddress());
        if (lastChat != null) {
            final long earliestNext = lastChat + 60000L;
            if (now < earliestNext) {
                final int timeRemaining = (int)((earliestNext - now) / 1000L) + 1;
                e.setMotd("You may log back in after §b" + this.formatTime(timeRemaining));
            }
        }
    }
    
    public String formatTime(final long time) {
        final long second = 1000L;
        final long minute = 60000L;
        final long minutes = time / minute;
        final long seconds = (time - minutes * minute) / second;
        if (seconds < 10L) {
            final String newSeconds = "0" + seconds;
            return minutes + ":" + newSeconds;
        }
        return minutes + ":" + seconds;
    }
    
    public static HashMap<String, Long> getDeathbanned() {
        return DeathKick.deathbanned;
    }
    
    static {
        DeathKick.deathbanned = new HashMap<String, Long>();
    }
}
