package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class SetFriendlyFire extends TeamSubCommand
{
    public SetFriendlyFire() {
        super("ff", true, Arrays.asList("friendlyfire", "sff", "setfriendlyfire", "setff"), false);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 1) {
            p.sendMessage("§c/team ff <on/off>");
            return;
        }
        if (!TeamManager.getInstance().isOnTeam(p.getName())) {
            p.sendMessage("§cYou are not on a team.");
            return;
        }
        if (!TeamManager.getInstance().isLeader(p) && !TeamManager.getInstance().isManager(p)) {
            p.sendMessage("§cYou must be at least a manager to perform this command.");
            return;
        }
        final Team team = TeamManager.getInstance().getPlayerTeam(p);
        if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("true")) {
            if (team.isFriendlyFire()) {
                p.sendMessage("§eFriendly fire is already turned on!");
                return;
            }
            team.setFriendlyFire(true);
            TeamManager.getInstance().messageTeam(team, "§6" + p.getName() + " §ehas §cenabled §efriendly fire!");
        }
        else {
            if (!args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("false")) {
                p.sendMessage("§c/team ff <on/off>");
                return;
            }
            TeamManager.getInstance().messageTeam(team, "§6" + p.getName() + " §ehas §adisabled §efriendly fire!");
        }
    }
}
