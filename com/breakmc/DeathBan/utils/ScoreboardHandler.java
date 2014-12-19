package com.breakmc.DeathBan.utils;

import com.breakmc.DeathBan.objects.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler
{
    public static void setupScoreboard(final User user) {
        final ScoreboardManager sbmanager = Bukkit.getScoreboardManager();
        final Scoreboard mainScoreboard = sbmanager.getNewScoreboard();
        final Objective objective = mainScoreboard.registerNewObjective("gold", "dummy");
        objective.setDisplayName("§c" + user.getPlayer().getName() + "'s Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        final Score gold = objective.getScore(Bukkit.getOfflinePlayer("§6§lGold"));
        gold.setScore(user.getGold());
        user.getPlayer().setScoreboard(mainScoreboard);
    }
}
