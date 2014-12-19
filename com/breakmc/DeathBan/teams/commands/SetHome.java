package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class SetHome extends TeamSubCommand
{
    public SetHome() {
        super("sethome", true, Arrays.asList("sh", "seth", "shome"), false);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (TeamManager.getInstance().getPlayerTeam(p) == null) {
            p.sendMessage("§cYou are not a team!");
        }
        else if (TeamManager.getInstance().isLeader(p) || TeamManager.getInstance().isManager(p)) {
            TeamManager.getInstance().getPlayerTeam(p).setHome(p.getLocation());
            TeamManager.getInstance().saveTeam(TeamManager.getInstance().getPlayerTeam(p));
            TeamManager.getInstance().messageTeam(TeamManager.getInstance().getPlayerTeam(p), "§6" + p.getName() + " §ehas set the team home at " + TeamManager.getInstance().formatLocation(p.getLocation()));
        }
        else {
            p.sendMessage("§cYou must be at least a manager to perform this command.");
        }
    }
}
