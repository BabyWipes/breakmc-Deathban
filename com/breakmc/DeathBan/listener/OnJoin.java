package com.breakmc.DeathBan.listener;

import org.bukkit.event.player.*;
import com.breakmc.DeathBan.utils.*;
import com.breakmc.DeathBan.objects.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import com.breakmc.DeathBan.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;

public class OnJoin implements Listener
{
    ConfigurationManager manager;
    
    public OnJoin() {
        super();
        this.manager = ConfigurationManager.getInstance();
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        User user;
        if (UserManager.getInstance().getUserCache().containsKey(e.getPlayer().getName())) {
            System.out.println("Loading scoreboard for " + e.getPlayer().getName());
            user = UserManager.getInstance().getUserCache().get(e.getPlayer().getName());
        }
        else {
            System.out.println("Loading scoreboard for " + e.getPlayer().getName());
            user = UserManager.getInstance().loadUser(e.getPlayer());
            UserManager.getInstance().getUserCache().put(e.getPlayer().getName(), user);
        }
        new BukkitRunnable() {
            public void run() {
                try {
                    final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    final Objective objective = (scoreboard.getObjective("gold") == null) ? scoreboard.registerNewObjective("gold", "dummy") : scoreboard.getObjective("gold");
                    objective.setDisplayName("§c" + e.getPlayer().getName() + "'s Stats");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    final Score gold = objective.getScore(Bukkit.getOfflinePlayer("§6§lGold"));
                    gold.setScore(user.getGold());
                    e.getPlayer().setScoreboard(scoreboard);
                    System.out.println("Loading scoreboard for " + e.getPlayer().getName());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskLater((Plugin)Main.getPlugin((Class)Main.class), 20L);
    }
}
