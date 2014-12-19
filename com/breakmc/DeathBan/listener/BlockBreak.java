package com.breakmc.DeathBan.listener;

import org.bukkit.event.block.*;
import com.breakmc.DeathBan.objects.*;
import com.breakmc.DeathBan.utils.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import org.bukkit.event.*;

public class BlockBreak implements Listener
{
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        final User user = UserManager.getInstance().getUserCache().get(e.getPlayer().getName());
        final Material mat = e.getBlock().getType();
        if (mat.getId() == 37 || mat.getId() == 38 || mat.getId() == 31 || mat.getId() == 32 || mat.getId() == 39 || mat.getId() == 40 || mat.getId() == 6 || mat.getId() == 175) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        if (user.getGold() > 0) {
            user.setGold(user.getGold() - 1);
            final Scoreboard scoreboard = e.getPlayer().getScoreboard();
            final Objective objective = (scoreboard.getObjective("gold") == null) ? scoreboard.registerNewObjective("gold", "dummy") : scoreboard.getObjective("gold");
            objective.setDisplayName("§c" + e.getPlayer().getName() + "'s Stats");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            final Score gold = objective.getScore(Bukkit.getOfflinePlayer("§6§lGold"));
            gold.setScore(user.getGold());
            e.getPlayer().setScoreboard(scoreboard);
        }
        else {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You do not have enough gold to break blocks!");
        }
    }
}
