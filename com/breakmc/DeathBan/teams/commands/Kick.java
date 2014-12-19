package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.breakmc.DeathBan.teams.*;

public class Kick extends TeamSubCommand
{
    public Kick() {
        super("kick", true, Arrays.asList("k"), false);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 1) {
            p.sendMessage("§c/team kick <Player>");
            return;
        }
        if (!TeamManager.getInstance().isOnTeam(p.getName())) {
            p.sendMessage("§cYou are not on a team!");
        }
        else if (TeamManager.getInstance().isLeader(p) || TeamManager.getInstance().isManager(p)) {
            if (!TeamManager.getInstance().isOnTeam(args[0]) || !TeamManager.getInstance().getPlayerTeam(args[0]).equals(TeamManager.getInstance().getPlayerTeam(p))) {
                p.sendMessage("§cThis player is not on your team!");
            }
            else if (TeamManager.getInstance().getPlayerTeam(p).getLeader().equals(args[0])) {
                p.sendMessage("§cYou cannot kick the team leader!");
            }
            else if (p.getName().equals(args[0])) {
                p.sendMessage("§cYou cannot kick yourself!");
            }
            else {
                TeamManager.getInstance().getPlayerTeam(p).getManagers().remove(args[0]);
                TeamManager.getInstance().getPlayerTeam(p).getMembers().remove(args[0]);
                TeamManager.getInstance().messageTeam(TeamManager.getInstance().getPlayerTeam(p), "§6" + p.getName() + " §ehas kicked §6" + args[0] + " §efrom the team!");
                TeamManager.getInstance().saveTeam(TeamManager.getInstance().getPlayerTeam(p));
                TeamManager.getInstance().getInTeam().remove(args[0]);
                TeamManager.getInstance().saveInTeam();
                TeamManager.getInstance().getTeamChat().remove(args[0]);
                if (Bukkit.getPlayer(args[0]) != null) {
                    Bukkit.getPlayer(args[0]).sendMessage("§eYou have been kicked from the team!");
                }
                TeamManager.getInstance().updateTeam(TeamManager.getInstance().getPlayerTeam(p), TeamAction.UPDATE);
                TeamManager.getInstance().removePlayer(args[0]);
            }
        }
        else {
            p.sendMessage("§cYou must be at least a manager to perform this command!");
        }
    }
}
