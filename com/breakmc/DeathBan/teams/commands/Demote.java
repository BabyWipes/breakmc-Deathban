package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Demote extends TeamSubCommand
{
    public Demote() {
        super("demote", true, Arrays.asList("d"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 1) {
            p.sendMessage("§c/team demote <Player>");
            return;
        }
        if (!TeamManager.getInstance().isOnTeam(p.getName())) {
            p.sendMessage("§cYou are not on a team!");
            return;
        }
        if (!TeamManager.getInstance().isLeader(p) && !TeamManager.getInstance().isManager(p)) {
            p.sendMessage("§cYou must be at least a manager to perform this command.");
            return;
        }
        final Team playerTeam = TeamManager.getInstance().getPlayerTeam(p);
        final Team argsTeam = TeamManager.getInstance().getPlayerTeam(args[0]);
        if (argsTeam == null) {
            p.sendMessage("§cThat player is not on a team.");
            return;
        }
        if (!playerTeam.equals(argsTeam)) {
            p.sendMessage("§cThat player is not on your team!");
            return;
        }
        if (p.getName().equals(args[0])) {
            p.sendMessage("§cYou cannot demote your self!");
            return;
        }
        if (playerTeam.getLeader().equals(args[0])) {
            p.sendMessage("§cYou cannot demote the leader!");
            return;
        }
        if (playerTeam.getMembers().contains(args[0])) {
            p.sendMessage("§cPlayer '" + args[0] + "' is already a member!");
            return;
        }
        playerTeam.getManagers().remove(args[0]);
        playerTeam.getMembers().add(args[0]);
        TeamManager.getInstance().saveTeam(playerTeam);
        TeamManager.getInstance().messageTeam(playerTeam, "§6" + p.getName() + " §ehas demoted §6" + args[0] + "§e.");
        TeamManager.getInstance().updateTeam(playerTeam, TeamAction.UPDATE);
    }
}
