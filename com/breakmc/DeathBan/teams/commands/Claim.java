package com.breakmc.DeathBan.teams.commands;

import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;
import org.bukkit.*;

public class Claim extends TeamSubCommand
{
    public Claim() {
        super("claim", true);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (TeamManager.getInstance().isOnTeam(p.getName())) {
            final Location loc1 = p.getLocation().add(16.0, 0.0, 16.0);
            final Location loc2 = p.getLocation().subtract(16.0, 0.0, 16.0);
            final TeamClaim claim = new TeamClaim(TeamManager.getInstance().getPlayerTeam(p), loc1, loc2);
            p.sendMessage("You have claimed land!");
            return;
        }
        p.sendMessage("§cYou are not on a team.");
    }
}
